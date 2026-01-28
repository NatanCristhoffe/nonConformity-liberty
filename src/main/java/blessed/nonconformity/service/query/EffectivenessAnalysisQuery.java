package blessed.nonconformity.service.query;


import blessed.nonconformity.entity.EffectivenessAnalysis;
import blessed.nonconformity.repository.EffectivenessRepository;
import org.springframework.stereotype.Service;

@Service
public class EffectivenessAnalysisQuery {
    private final EffectivenessRepository effectivenessRepository;

    public EffectivenessAnalysisQuery(EffectivenessRepository effectivenessRepository){
        this.effectivenessRepository = effectivenessRepository;
    }

    public void save(EffectivenessAnalysis effectivenessAnalysis){effectivenessRepository.save(effectivenessAnalysis);}

}
