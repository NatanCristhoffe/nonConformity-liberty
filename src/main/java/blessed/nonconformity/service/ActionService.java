package blessed.nonconformity.service;


import blessed.exception.BusinessException;
import blessed.exception.ResourceNotFoundException;
import blessed.nonconformity.dto.ActionCompletedRequestDTO;
import blessed.nonconformity.dto.ActionNotExecutedRequestDTO;
import blessed.nonconformity.dto.ActionRequestDTO;
import blessed.nonconformity.entity.Action;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.enums.ActionStatus;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.repository.ActionRepository;
import blessed.nonconformity.repository.NonconformityRepository;
import blessed.user.entity.User;
import blessed.user.repository.UserRepository;
import blessed.utils.DataTimeUtils;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ActionService {
    private final ActionRepository actionRepository;
    private final NonconformityRepository ncRepository;
    private final UserRepository userRepository;
    private final QualityToolServiceImpl qualityService;

    public ActionService(
            ActionRepository actionRepository,
            NonconformityRepository ncRepository,
            UserRepository userRepository,
            QualityToolServiceImpl qualityService
            ){
        this.actionRepository = actionRepository;
        this.ncRepository = ncRepository;
        this.userRepository = userRepository;
        this.qualityService = qualityService;
    }

    @Transactional
    public Action create(Long ncId, ActionRequestDTO data){
        NonConformity nc = ncRepository.findById(ncId)
                .orElseThrow(() -> new BusinessException(
                        "Não conformidade não encontrada. Verifique o ID informado e tente novamente."
                ));

        User responsibleUser = userRepository.findById(data.responsibleUserId())
                .orElseThrow(() -> new BusinessException("Usuário não encontrado. Verifique o ID informado e tente novamente."));

        if (nc.getStatus() != NonConformityStatus.WAITING_ACTIONS){
            throw new BusinessException(
                    "A não conformidade não está em um status que permita o cadastro de ações."
            );
        }

        Action action = new Action(data);
        nc.addAction(action, responsibleUser);

        return actionRepository.save(action);
    }

    @Transactional
    public Action completedAction(Long actionId, ActionCompletedRequestDTO data, User completedBy){
        Action action = actionRepository.findById(actionId)
                .orElseThrow(() -> new ResourceNotFoundException("A ação informada não foi encontrada."));

        if (action.getStatus() != ActionStatus.PENDING){
            throw  new BusinessException("A ação já foi finalizada e não pode ser modificada.");
        }

        NonConformity nonConformity = action.getNonconformity();
        nonConformity.completeAction(action, data, completedBy);
        return action;
    }

    @Transactional
    public Action notExecutedAction(Long actionId,User user, ActionNotExecutedRequestDTO data){
        Action action = actionRepository.findById(actionId)
                .orElseThrow(() -> new ResourceNotFoundException("A ação informada não foi encontrada."));

        if (action.getStatus() != ActionStatus.PENDING){
            throw  new BusinessException("A ação já foi finalizada e não pode ser modificada.");
        }

        NonConformity nonConformity = action.getNonconformity();
        nonConformity.notExecutedAction(action, data, user);

        return action;
    }

    @Transactional
    public NonConformity closeActionStage(Long ncId){
        NonConformity nc = ncRepository.findById(ncId)
                .orElseThrow(() -> new ResourceNotFoundException("Não conformidade não encontrada. Verifique o ID informado e tente novamente."));
        if (nc.getStatus() != NonConformityStatus.WAITING_ACTIONS){
            throw new BusinessException("A não conformidade não está no status esperado para esta operação.");
        }

        qualityService.closedFiveWhyTool(nc);
        nc.setStatus(NonConformityStatus.WAITING_EFFECTIVENESS_CHECK);
        return nc;
    }
}
