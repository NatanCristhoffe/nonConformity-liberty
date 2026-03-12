package blessed.nonconformity.service.query;

import blessed.exception.BusinessException;
import blessed.nonconformity.entity.FiveWhy;
import blessed.nonconformity.repository.FiveWhyRepository;
import org.springframework.stereotype.Service;

@Service
public class FiveWhyQuery {
    private final FiveWhyRepository repository;

    public FiveWhyQuery(FiveWhyRepository repository){
        this.repository = repository;
    }

    public FiveWhy byId(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("Porquês não encontrado"));
    }

    public void save(FiveWhy fiveWhy){
        repository.save(fiveWhy);
    }
}
