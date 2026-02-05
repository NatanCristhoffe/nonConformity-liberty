package blessed.nonconformity.service.query;


import blessed.exception.BusinessException;
import blessed.exception.ResourceNotFoundException;
import blessed.nonconformity.entity.Action;
import blessed.nonconformity.enums.ActionStatus;
import blessed.nonconformity.repository.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ActionQuery {

    private final ActionRepository actionRepository;

    public ActionQuery(ActionRepository actionRepository){
        this.actionRepository = actionRepository;
    }


    public void save(Action action){
        actionRepository.save(action);
    }

    public Action getById(Long id){
        return actionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("A ação informada não foi encontrada."));
    }

    public Action getActionPendingById(Long id){
        Action action = actionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("A ação informada não foi encontrada."));

        if (action.getStatus() != ActionStatus.PENDING){
            throw  new BusinessException("A ação já foi finalizada e não pode ser modificada.");
        }

        return action;
    }

    public boolean existsByIdAndResponsibleUserId(Long actionId, UUID responsibleUserId){
        return actionRepository.existsByIdAndResponsibleUserId(actionId, responsibleUserId);
    }
}
