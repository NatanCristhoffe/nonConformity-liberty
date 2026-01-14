package blessed.nonconformity.service;

import blessed.exception.BusinessException;
import blessed.exception.ResourceNotFoundException;
import blessed.nonconformity.dto.FiveWhyRequestDTO;
import blessed.nonconformity.entity.FiveWhy;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.repository.NonconformityRepository;
import blessed.nonconformity.tools.FiveWhyTool;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


@Service
public class FiveWhyService {
    private final NonconformityRepository ncRepository;

    public FiveWhyService(NonconformityRepository ncRepository){
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


}
