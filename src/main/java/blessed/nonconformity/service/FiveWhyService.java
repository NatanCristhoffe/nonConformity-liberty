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
    public void addWhy(Long ncId, FiveWhyRequestDTO data){
        NonConformity nc = ncRepository.findById(ncId)
                .orElseThrow(() -> new ResourceNotFoundException("NC não encontrada"));

        if (nc.getStatus() != NonConformityStatus.WAITING_QUALITY_TOOL){
            throw new BusinessException("Não é possível adicionar porquês neste status");
        }

        FiveWhyTool tool = nc.getFiveWhyTool();
        if (tool.getFiveWhys().size() >= 5){
            throw new BusinessException("Não é permitido mais de 5 porquês");
        }

        FiveWhy why = new FiveWhy();
        why.setLevel(data.level());
        why.setQuestion(data.question());
        why.setFiveWhyTool(tool);

        if(tool.getFiveWhys().contains(why)){
            throw new BusinessException("Já existe um porquê para este nível");
        }

        tool.getFiveWhys().add(why);
    }

    @Transactional
    public void addAnswer(Long nonconformityId, Long fiveWhyId, FiveWhyAnswerRequestDTO answer){
        NonConformity nc = ncRepository.findById(nonconformityId)
                .orElseThrow(() -> new ResourceNotFoundException("Não conformidade não encontrada."));

        FiveWhy fiveWhy = fiveWhyRepository.findById(fiveWhyId)
                .orElseThrow(()-> new ResourceNotFoundException("Porquês não encontrado."));

        if (!fiveWhy.getFiveWhyTool().getNonconformity().getId().equals(nc.getId())) {
            throw new BusinessException("A pergunta Cinco Porquês não pertence a esta categoria de não conformidade.");
        }

        if (fiveWhy.getAnswer() != null){
            throw new BusinessException("Esse Porquês já foi respondido.");
        }

        fiveWhy.setAnswer(answer.answer());
        fiveWhyRepository.save(fiveWhy);

        boolean allAnswered = fiveWhy.getFiveWhyTool()
                .getFiveWhys()
                .stream()
                .allMatch(w ->
                        w.getAnswer() != null &&
                                !w.getAnswer().isBlank()
                );
        if (allAnswered) {
            nc.concludeFiveWhyTool();
        }
    }


}
