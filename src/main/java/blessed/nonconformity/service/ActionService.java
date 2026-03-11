package blessed.nonconformity.service;


import blessed.infra.enums.FileType;
import blessed.infra.storage.S3FileStorageService;
import blessed.nonconformity.dto.ActionCompletedRequestDTO;
import blessed.nonconformity.dto.ActionNotExecutedRequestDTO;
import blessed.nonconformity.dto.ActionRequestDTO;
import blessed.nonconformity.dto.ActionResponseDTO;
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


    public ActionService(
            UserQuery userQuery,
            ActionQuery actionQuery,
            NonConformityQuery nonConformityQuery,
            S3FileStorageService s3Service,
            NotificationService notificationService
    ) {
        this.userQuery = userQuery;
        this.actionQuery = actionQuery;
        this.nonConformityQuery = nonConformityQuery;
        this.s3Service = s3Service;
        this.notificationService = notificationService;
    }


    @PreAuthorize("@ncAuth.isDispositionOwnerOrAdmin(#nonconformityId, authentication)")
    @Transactional
    public Action create(Long nonconformityId, ActionRequestDTO data, User userRequest){
        NonConformity nc = nonConformityQuery.byId(nonconformityId, userRequest.getCompany().getId());
        User responsibleUser = userQuery.byId(userRequest.getCompany().getId(),data.responsibleUserId());

        Action action = new Action(data);
        nc.addAction(action, responsibleUser, userRequest);
        actionQuery.save(action);

        Set<UUID> notifyUser = Set.of(
            data.responsibleUserId()
        );

        notificationService.notifyIfNotSameUser(
                notifyUser,
                userRequest.getId(),
                userRequest.getCompany().getId(),
                NotificationType.ACTION_ASSIGNED,
                action.getTitle()
        );


        return action;
    }

    @PreAuthorize("@actionAuth.isResponsibleOrAdmin(#actionId, authentication)")
    @Transactional
    public ActionResponseDTO completeAction(
            Long actionId, ActionCompletedRequestDTO data,
            User user, MultipartFile file) {

        Action action = actionQuery.getActionPendingById(actionId);


        var context = prepareActionContext(action, user, file);
        context
                .nonConformity()
                .completeAction(context.action(), data, context.user(), context.urlEvidence());
        Set<UUID> notifyUser = Set.of(
                action.getNonconformity().getDispositionOwner().getId()
        );
        notificationService.notifyIfNotSameUser(
                notifyUser,
                user.getId(),
                user.getCompany().getId(),
                NotificationType.ACTION_COMPLETED,
                action.getNonconformity().getTitle()
        );
        return new ActionResponseDTO(context.action());
    }

    @PreAuthorize("@actionAuth.isResponsibleOrAdmin(#actionId, authentication)")
    @Transactional
    public ActionResponseDTO markAsNotExecuted(Long actionId, ActionNotExecutedRequestDTO data, User user, MultipartFile file) {
        Action action = actionQuery.getActionPendingById(actionId);

        var context = prepareActionContext(action, user, file);
        context.nonConformity().notExecutedAction(context.action(), data, context.user(), context.urlEvidence());

        Set<UUID> notifyUser = Set.of(
                action.getNonconformity().getDispositionOwner().getId()
        );
        notificationService.notifyIfNotSameUser(
                notifyUser,
                user.getId(),
                user.getCompany().getId(),
                NotificationType.ACTION_NOT_COMPLETED,
                action.getNonconformity().getTitle()
        );

        return new ActionResponseDTO(context.action());
    }

    @PreAuthorize("@ncAuth.isDispositionOwnerOrAdmin(#nonconformityId, authentication)")
    @Transactional
    public NonConformity closeActionStage(Long nonconformityId, User userRequest){
        UUID companyId = userRequest.getCompany().getId();
        NonConformity nc = nonConformityQuery.byId(nonconformityId, companyId);
        nc.closedAction(userRequest);
        nc.closedDisposition(userRequest);

        Set<UUID> notifyUser = new HashSet<UUID>();
        notifyUser.add(nc.getCreatedBy().getId());
        notifyUser.add(nc.getEffectivenessAnalyst().getId());

        notificationService.notifyIfNotSameUser(
                notifyUser,
                userRequest.getId(),
                companyId,
                NotificationType.DISPOSITION_COMPLETED,
                nc.getTitle()
        );

        return nc;
    }



    private record ActionContext(Action action, NonConformity nonConformity, User user, String urlEvidence){};

    private ActionContext prepareActionContext(Action action, User authUser, MultipartFile file){
        String urlEvidence = null;
        if (file != null && !file.isEmpty()) {
            urlEvidence = s3Service.uploadFile(file, "actions-evidence", FileType.EVIDENCE);
        }

        User user = userQuery.byId(authUser.getCompany().getId(),authUser.getId());
        NonConformity nonConformity = action.getNonconformity();

        return new ActionContext(action, nonConformity, user, urlEvidence);

    }
}
