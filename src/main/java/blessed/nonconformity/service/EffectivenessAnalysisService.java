package blessed.nonconformity.service;

import blessed.exception.BusinessException;
import blessed.exception.ResourceNotFoundException;
import blessed.nonconformity.dto.EffectivenessAnalysisRequestDTO;
import blessed.nonconformity.entity.EffectivenessAnalysis;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.repository.EffectivenessRepository;
import blessed.nonconformity.repository.NonconformityRepository;
import blessed.user.entity.User;
import blessed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class EffectivenessAnalysisService {

    private final EffectivenessRepository efRepository;
    private final NonconformityRepository ncRepository;
    private final UserRepository userRepository;

    public EffectivenessAnalysisService(
            EffectivenessRepository efRepository,
            NonconformityRepository ncRepository,
            UserRepository userRepository
    ) {
        this.efRepository = efRepository;
        this.ncRepository = ncRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void addEffectivenessAnalysis(
            Long ncId,
            EffectivenessAnalysisRequestDTO data
    ) {
        NonConformity nc = ncRepository.findById(ncId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Não conformidade não encontrada. Verifique o ID informado e tente novamente."
                ));
        if (nc.getStatus() != NonConformityStatus.WAITING_EFFECTIVENESS_CHECK) {
            throw new BusinessException("A não conformidade não está no status esperado para esta operação.");
        }


        User user = userRepository.findById(data.analyzedById())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado. Verifique o ID informado e tente novamente."
                ));

        EffectivenessAnalysis effectiveness = new EffectivenessAnalysis(data, nc, user);
        nc.addEffectivenessAnalysis(effectiveness);

        efRepository.save(effectiveness);
    }
}
