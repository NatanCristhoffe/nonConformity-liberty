package blessed.nonconformity.service;


import blessed.auth.utils.CurrentUser;
import blessed.infra.enums.FileType;
import blessed.infra.storage.S3FileStorageService;
import blessed.nonconformity.dto.*;
import blessed.nonconformity.entity.Action;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.service.query.ActionQuery;
import blessed.nonconformity.service.query.NonConformityQuery;
import blessed.notification.enums.NotificationType;
import blessed.notification.service.NotificationService;
import blessed.user.service.query.UserQuery;
import blessed.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class ActionService {

    private final UserQuery userQuery;
    private final ActionQuery actionQuery;
    private final NonConformityQuery nonConformityQuery;
    private final S3FileStorageService s3Service;
    private final NotificationService notificationService;
    private final CurrentUser currentUser;


    public ActionService(
            UserQuery userQuery,
            ActionQuery actionQuery,
            NonConformityQuery nonConformityQuery,
            S3FileStorageService s3Service,
            NotificationService notificationService,
            CurrentUser currentUser
    ) {
        this.userQuery = userQuery;
        this.actionQuery = actionQuery;
        this.nonConformityQuery = nonConformityQuery;
        this.s3Service = s3Service;
        this.notificationService = notificationService;
        this.currentUser = currentUser;
    }


    @PreAuthorize("@ncAuth.isDispositionOwnerOrAdmin(#nonconformityId)")
    @Transactional
    public Action create(Long nonconformityId, ActionRequestDTO data){
        NonConformity nc = nonConformityQuery.byId(nonconformityId, currentUser.getCompanyId());
        User responsibleUser = userQuery.byId(currentUser.getCompanyId(), data.responsibleUserId());

        User userRequest = userQuery.byId(currentUser.getCompanyId(),currentUser.getId());

        Action action = new Action(data);
        nc.addAction(action, responsibleUser, userRequest);
        actionQuery.save(action);

        Set<UUID> notifyUser = Set.of(
            data.responsibleUserId()
        );

        notificationService.notifyIfNotSameUser(
                notifyUser,
                currentUser.getId(),
                currentUser.getCompanyId(),
                NotificationType.ACTION_ASSIGNED,
                action.getTitle()
        );


        return action;
    }

    @PreAuthorize("@actionAuth.isResponsibleOrAdmin(#actionId)")
    @Transactional
    public ActionResponseDTO completeAction(
            Long actionId,
            ActionCompletedRequestDTO data,
            MultipartFile file
    ) {
        Action action = actionQuery.getActionPendingById(actionId);
        User user = userQuery.byId(currentUser.getCompanyId(), currentUser.getId());

        var context = prepareActionContext(action, user, file);
        context.nonConformity().completeAction(
                context.action(),
                data,
                context.user(),
                context.urlEvidence()
        );

        Set<UUID> notifyUser = Set.of(
                action.getNonconformity().getDispositionOwner().getId()
        );
        notificationService.notifyIfNotSameUser(
                notifyUser,
                user.getId(),
                currentUser.getCompanyId(),
                NotificationType.ACTION_COMPLETED,
                action.getNonconformity().getTitle()
        );
        return new ActionResponseDTO(context.action());
    }

    @PreAuthorize("@actionAuth.isResponsibleOrAdmin(#actionId)")
    @Transactional
    public ActionResponseDTO markAsNotExecuted(Long actionId, ActionNotExecutedRequestDTO data, MultipartFile file) {
        Action action = actionQuery.getActionPendingById(actionId);
        User user = userQuery.byId(currentUser.getCompanyId(), currentUser.getId());

        var context = prepareActionContext(action, user, file);
        context.nonConformity().notExecutedAction(
                context.action(), data, context.user(), context.urlEvidence()
        );

        Set<UUID> notifyUser = Set.of(
                action.getNonconformity().getDispositionOwner().getId()
        );

        notificationService.notifyIfNotSameUser(
                notifyUser,
                currentUser.getId(),
                currentUser.getCompanyId(),
                NotificationType.ACTION_NOT_COMPLETED,
                action.getNonconformity().getTitle()
        );

        return new ActionResponseDTO(context.action());
    }

    @PreAuthorize("@ncAuth.isDispositionOwnerOrAdmin(#nonconformityId)")
    @Transactional
    public NonconformityResponseDTO closeActionStage(Long nonconformityId){

        UUID companyId = currentUser.getCompanyId();
        User user = userQuery.byId(companyId, currentUser.getId());

        NonConformity nc = nonConformityQuery.byId(nonconformityId, companyId);
        nc.closedAction(user);
        nc.closedDisposition(user);

        Set<UUID> notifyUser = new HashSet<UUID>();
        notifyUser.add(nc.getCreatedBy().getId());
        notifyUser.add(nc.getEffectivenessAnalyst().getId());

        notificationService.notifyIfNotSameUser(
                notifyUser,
                currentUser.getId(),
                companyId,
                NotificationType.DISPOSITION_COMPLETED,
                nc.getTitle()
        );

        return new NonconformityResponseDTO(nc);
    }



    private record ActionContext(Action action, NonConformity nonConformity, User user, String urlEvidence){};

    private ActionContext prepareActionContext(Action action, User user, MultipartFile file){
        String urlEvidence = null;
        if (file != null && !file.isEmpty()) {
            urlEvidence = s3Service.uploadFile(file, "actions-evidence", FileType.EVIDENCE);
        }

        NonConformity nonConformity = action.getNonconformity();

        return new ActionContext(action, nonConformity, user, urlEvidence);

    }
}
