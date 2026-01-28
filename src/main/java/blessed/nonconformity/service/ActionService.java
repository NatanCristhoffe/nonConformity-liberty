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
import org.springframework.stereotype.Service;

@Service
public class ActionService {

    @Autowired
    UserQuery userQuery;
    @Autowired
    ActionQuery actionQuery;
    @Autowired
    NonConformityQuery nonConformityQuery;

    @Transactional
    public Action create(Long ncId, ActionRequestDTO data){
        NonConformity nc = nonConformityQuery.byId(ncId);
        User responsibleUser = userQuery.byId(data.responsibleUserId());

        if (nc.getStatus() != NonConformityStatus.WAITING_ACTIONS){
            throw new BusinessException(
                    "A não conformidade não está em um status que permita o cadastro de ações."
            );
        }

        Action action = new Action(data);
        nc.addAction(action, responsibleUser);
        actionQuery.save(action);
        return action;
    }

    @Transactional
    public ActionResponseDTO completedAction(Long actionId, ActionCompletedRequestDTO data, User completedBy){
        User managedUser = userQuery.byId(completedBy.getId());
        Action action = actionQuery.getActionPendingById(actionId);

        NonConformity nonConformity = action.getNonconformity();
        nonConformity.completeAction(action, data, managedUser);
        return new ActionResponseDTO(action);
    }

    @Transactional
    public Action notExecutedAction(Long actionId, User userRequest, ActionNotExecutedRequestDTO data){

        User user = userQuery.byId(userRequest.getId());
        Action action = actionQuery.getActionPendingById(actionId);

        NonConformity nonConformity = action.getNonconformity();
        nonConformity.notExecutedAction(action, data, user);

        return action;
    }

    @Transactional
    public NonConformity closeActionStage(Long ncId, User userRequest){
        NonConformity nc = nonConformityQuery.byId(ncId);
        nc.closedAction(userRequest);
        return nc;
    }
}
