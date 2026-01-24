package blessed.nonconformity.service;

import blessed.exception.BusinessException;
import blessed.exception.ResourceNotFoundException;
import blessed.nonconformity.dto.FiveWhyAnswerRequestDTO;
import blessed.nonconformity.dto.FiveWhyRequestDTO;
import blessed.nonconformity.entity.FiveWhy;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.repository.FiveWhyRepository;
import blessed.nonconformity.repository.FiveWhyToolRepository;
import blessed.nonconformity.repository.NonconformityRepository;
import blessed.nonconformity.tools.FiveWhyTool;
import blessed.utils.DataTimeUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FiveWhyService {
    private final NonconformityRepository ncRepository;
    private final FiveWhyRepository fiveWhyRepository;
    private final FiveWhyToolRepository fiveWhyToolRepository;

    public FiveWhyService(
            NonconformityRepository ncRepository,
            FiveWhyToolRepository fiveWhyToolRepository,
            FiveWhyRepository fiveWhyRepository
            ){
        this.fiveWhyToolRepository = fiveWhyToolRepository;
        this.fiveWhyRepository = fiveWhyRepository;
        this.ncRepository = ncRepository;
    }


    @Transactional
    public void addAnswer(Long nonconformityId, Long fiveWhyId, FiveWhyAnswerRequestDTO answer){
        NonConformity nc = ncRepository.findById(nonconformityId)
                .orElseThrow(() -> new ResourceNotFoundException("Não conformidade não encontrada."));

        FiveWhy fiveWhy = fiveWhyRepository.findById(fiveWhyId)
                .orElseThrow(()-> new ResourceNotFoundException("Porquês não encontrado."));

        fiveWhy.addAnswer(answer, nc);
        fiveWhyRepository.save(fiveWhy);

        boolean allAnswered = fiveWhy.areAllWhysAnswered();
        if (allAnswered) {
            nc.concludeFiveWhyTool();
        }
    }


}
