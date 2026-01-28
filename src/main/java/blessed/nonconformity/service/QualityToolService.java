package blessed.nonconformity.service;

import blessed.exception.BusinessException;
import blessed.exception.ResourceNotFoundException;
import blessed.nonconformity.entity.Action;
import blessed.nonconformity.entity.FiveWhy;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.enums.ActionStatus;
import blessed.nonconformity.repository.FiveWhyToolRepository;
import blessed.nonconformity.tools.FiveWhyTool;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class QualityToolService implements blessed.nonconformity.interfaces.QualityToolService {
    private final FiveWhyToolRepository repository;

    public QualityToolService(FiveWhyToolRepository repository){
        this.repository = repository;
    }

    @Override
    @Transactional
    public void initializeTool(NonConformity nc){
            if (nc.getSelectedTool() == null){
                throw new BusinessException("Ferramenta de qualidade não definida");
            }

            switch (nc.getSelectedTool()){
                case FIVE_WHYS -> {
                    FiveWhyTool tool = new FiveWhyTool();
                    tool.setNonconformity(nc);
                    tool.setCompleted(false);

                    List<String> defaultQuestions = List.of(
                            "Por que o problema ocorreu?",
                            "Por que essa causa aconteceu?",
                            "Por que essa causa não foi detectada anteriormente?",
                            "Por que o processo permitiu que o problema acontecesse?",
                            "Por que não existe um controle eficaz para evitar esse problema?"
                    );

                    for(int i =0; i < defaultQuestions.size(); i++){
                        FiveWhy why = new FiveWhy(
                                i+1,
                                defaultQuestions.get(i),
                                tool);
                        tool.getFiveWhys().add(why);
                    }

                    nc.setFiveWhyTool(tool);
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
