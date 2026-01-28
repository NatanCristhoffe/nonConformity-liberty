package blessed.nonconformity.service.query;

import blessed.exception.ResourceNotFoundException;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.repository.NonconformityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NonConformityQuery {

    @Autowired
    NonconformityRepository nonconformityRepository;

    public NonConformity byId(Long id){
        return  nonconformityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Não conformidade não encontrada. Verifique o ID informado e tente novamente."));

    };
}
