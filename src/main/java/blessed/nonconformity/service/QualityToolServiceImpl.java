package blessed.nonconformity.service;

import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.interfaces.QualityToolService;
import blessed.nonconformity.repository.FiveWhyToolRepository;
import blessed.nonconformity.tools.FiveWhyTool;
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
}
