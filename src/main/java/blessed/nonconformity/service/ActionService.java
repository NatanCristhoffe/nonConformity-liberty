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
import blessed.user.service.query.UserQuery;
import blessed.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ActionService {

    private final UserQuery userQuery;
    private final ActionQuery actionQuery;
    private final NonConformityQuery nonConformityQuery;
    private final S3FileStorageService s3Service;

    public ActionService(
            UserQuery userQuery,
            ActionQuery actionQuery,
            NonConformityQuery nonConformityQuery,
            S3FileStorageService s3Service
    ) {
        this.userQuery = userQuery;
        this.actionQuery = actionQuery;
        this.nonConformityQuery = nonConformityQuery;
        this.s3Service = s3Service;
    }


    @PreAuthorize("@ncAuth.isDispositionOwnerOrAdmin(#nonconformityId, authentication)")
    @Transactional
    public Action create(Long nonconformityId, ActionRequestDTO data, User userRequest){
        NonConformity nc = nonConformityQuery.byId(nonconformityId, userRequest.getCompany().getId());
        User responsibleUser = userQuery.byId(data.responsibleUserId());

        Action action = new Action(data);
        nc.addAction(action, responsibleUser, userRequest);
        actionQuery.save(action);
        return action;
    }

    @PreAuthorize("@actionAuth.isResponsibleOrAdmin(#actionId, authentication)")
    @Transactional
    public ActionResponseDTO completeAction(
            Long actionId, ActionCompletedRequestDTO data,
            User user, MultipartFile file) {
        var context = prepareActionContext(actionId, user, file);
        context.nonConformity().completeAction(context.action(), data, context.user(), context.urlEvidence());
        return new ActionResponseDTO(context.action());
    }

    @PreAuthorize("@actionAuth.isResponsibleOrAdmin(#actionId, authentication)")
    @Transactional
    public ActionResponseDTO markAsNotExecuted(Long actionId, ActionNotExecutedRequestDTO data, User user, MultipartFile file) {
        var context = prepareActionContext(actionId, user, file);
        context.nonConformity().notExecutedAction(context.action(), data, context.user(), context.urlEvidence());

        return new ActionResponseDTO(context.action());
    }

    @PreAuthorize("@ncAuth.isDispositionOwnerOrAdmin(#nonconformityId, authentication)")
    @Transactional
    public NonConformity closeActionStage(Long nonconformityId, User userRequest){
        NonConformity nc = nonConformityQuery.byId(nonconformityId, userRequest.getCompany().getId());
        nc.closedAction(userRequest);
        nc.closedDisposition(userRequest);
        return nc;
    }



    private record ActionContext(Action action, NonConformity nonConformity, User user, String urlEvidence){};

    private ActionContext prepareActionContext(Long actionId, User authUser, MultipartFile file){
        String urlEvidence = null;
        if (file != null && !file.isEmpty()) {
            urlEvidence = s3Service.uploadFile(file, "actions-evidence", FileType.EVIDENCE);
        }

        User user = userQuery.byId(authUser.getId());
        Action action = actionQuery.getActionPendingById(actionId);
        NonConformity nonConformity = action.getNonconformity();

        return new ActionContext(action, nonConformity, user, urlEvidence);

    }
}
