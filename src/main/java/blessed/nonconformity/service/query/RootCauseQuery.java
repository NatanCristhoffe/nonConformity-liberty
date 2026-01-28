package blessed.nonconformity.service.query;

import blessed.nonconformity.entity.RootCause;
import blessed.nonconformity.repository.RootCauseRepository;
import org.springframework.stereotype.Service;

@Service
public class RootCauseQuery {
    private final RootCauseRepository rootCauseRepository;

    public RootCauseQuery(RootCauseRepository rootCauseRepository){
        this.rootCauseRepository = rootCauseRepository;
    }

    public void save(RootCause rootCause){rootCauseRepository.save(rootCause);}


}
