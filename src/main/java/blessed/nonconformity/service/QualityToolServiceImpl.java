package blessed.nonconformity.service;

import blessed.exception.BusinessException;
import blessed.exception.ResourceNotFoundException;
import blessed.nonconformity.entity.Action;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.enums.ActionStatus;
import blessed.nonconformity.interfaces.QualityToolService;
import blessed.nonconformity.repository.FiveWhyToolRepository;
import blessed.nonconformity.tools.FiveWhyTool;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


@Service
public class QualityToolServiceImpl implements QualityToolService {
    private final FiveWhyToolRepository repository;

    public QualityToolServiceImpl(FiveWhyToolRepository repository){
        this.repository = repository;
    }

    @Override
    public void initializeTool(NonConformity nc){
            if (nc.getRequiresQualityTool() != true){
                return;
            }

            switch (nc.getSelectedTool()){
                case FIVE_WHYS -> {
                    FiveWhyTool tool = new FiveWhyTool();
                    tool.setNonconformity(nc);
                    tool.setCompleted(false);
                    repository.save(tool);
                }

                case ISHIKAWA -> {
                    return;
                }
                default -> throw new IllegalStateException("Ferramenta não suportada");
            }
    }

    @Transactional
    public void closedFiveWhyTool(NonConformity nc){
        Long id = nc.getFiveWhyTool().getId();
        FiveWhyTool tool = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ferramenta dos cinco porquês não encontrada."));

        for(Action action : nc.getActions()){
            if (action.getStatus() == ActionStatus.PENDING){
                throw new BusinessException(
                        "Existem ações pendentes. Todas as ações devem ser marcadas como concluídas ou não executadas antes de prosseguir."
                );
            }
        }

        tool.setCompleted(true);
    }

}
