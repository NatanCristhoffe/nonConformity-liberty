package blessed.nonconformity.service;


import blessed.exception.BusinessException;
import blessed.exception.ResourceNotFoundException;
import blessed.nonconformity.dto.ActionCompletedRequestDTO;
import blessed.nonconformity.dto.ActionNotExecutedRequestDTO;
import blessed.nonconformity.dto.ActionRequestDTO;
import blessed.nonconformity.dto.ActionResponseDTO;
import blessed.nonconformity.entity.Action;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.enums.ActionStatus;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.repository.ActionRepository;
import blessed.nonconformity.repository.NonconformityRepository;
import blessed.nonconformity.service.query.ActionQuery;
import blessed.nonconformity.service.query.NonConformityQuery;
import blessed.user.service.query.UserQuery;
import blessed.user.entity.User;
import blessed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class ActionService {

    private final UserQuery userQuery;
    private final ActionQuery actionQuery;
    private final NonConformityQuery nonConformityQuery;

    public ActionService(
            UserQuery userQuery,
            ActionQuery actionQuery,
            NonConformityQuery nonConformityQuery
    ) {
        this.userQuery = userQuery;
        this.actionQuery = actionQuery;
        this.nonConformityQuery = nonConformityQuery;
    }


    @PreAuthorize("@ncAuth.isDispositionOwnerOrAdmin(#nonconformityId, authentication)")
    @Transactional
    public Action create(Long nonconformityId, ActionRequestDTO data, User userRequest){
        NonConformity nc = nonConformityQuery.byId(nonconformityId);
        User responsibleUser = userQuery.byId(data.responsibleUserId());

        Action action = new Action(data);
        nc.addAction(action, responsibleUser, userRequest);
        actionQuery.save(action);
        return action;
    }

    @PreAuthorize("@actionAuth.isResponsibleOrAdmin(#actionId, authentication)")
    @Transactional
    public ActionResponseDTO completedAction(Long actionId, ActionCompletedRequestDTO data, User completedBy){
        User managedUser = userQuery.byId(completedBy.getId());
        Action action = actionQuery.getActionPendingById(actionId);

        NonConformity nonConformity = action.getNonconformity();
        nonConformity.completeAction(action, data, managedUser);
        return new ActionResponseDTO(action);
    }

    @PreAuthorize("@actionAuth.isResponsibleOrAdmin(#actionId, authentication)")
    @Transactional
    public Action notExecutedAction(Long actionId, User userRequest, ActionNotExecutedRequestDTO data){

        User user = userQuery.byId(userRequest.getId());
        Action action = actionQuery.getActionPendingById(actionId);

        NonConformity nonConformity = action.getNonconformity();
        nonConformity.notExecutedAction(action, data, user);

        return action;
    }

    @PreAuthorize("@ncAuth.isDispositionOwnerOrAdmin(#nonconformityId, authentication)")
    @Transactional
    public NonConformity closeActionStage(Long nonconformityId, User userRequest){
        NonConformity nc = nonConformityQuery.byId(nonconformityId);
        nc.closedAction(userRequest);
        nc.closedDisposition(userRequest);
        return nc;
    }
}
