package blessed.nonconformity.service;

import blessed.exception.BusinessException;
import blessed.exception.ResourceNotFoundException;
import blessed.nonconformity.dto.EffectivenessAnalysisRequestDTO;
import blessed.nonconformity.entity.EffectivenessAnalysis;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.repository.EffectivenessRepository;
import blessed.nonconformity.repository.NonconformityRepository;
import blessed.nonconformity.service.query.EffectivenessAnalysisQuery;
import blessed.nonconformity.service.query.NonConformityQuery;
import blessed.user.entity.User;
import blessed.user.repository.UserRepository;
import blessed.user.service.query.UserQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class EffectivenessAnalysisService {

    private final EffectivenessAnalysisQuery effectivenessAnalysisQuery;
    private final NonConformityQuery nonConformityQuery;
    private final UserQuery userQuery;

    public EffectivenessAnalysisService(
            EffectivenessAnalysisQuery effectivenessAnalysisQuery,
            NonConformityQuery nonConformityQuery,
            UserQuery userQuery
    ) {
        this.effectivenessAnalysisQuery = effectivenessAnalysisQuery;
        this.nonConformityQuery = nonConformityQuery;
        this.userQuery = userQuery;
    }

    @Transactional
    public void addEffectivenessAnalysis(Long ncId,EffectivenessAnalysisRequestDTO data, User userRequest) {

        NonConformity nc = nonConformityQuery.byId(ncId);
        User user = userQuery.byId(userRequest.getId());

        EffectivenessAnalysis effectiveness = new EffectivenessAnalysis(data, nc, user);
        nc.addEffectivenessAnalysis(effectiveness);

        effectivenessAnalysisQuery.save(effectiveness);
    }
}
