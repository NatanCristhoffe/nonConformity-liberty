package blessed.nonconformity.service.query;

import blessed.exception.ResourceNotFoundException;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.repository.NonconformityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NonConformityQuery {

    private final NonconformityRepository nonconformityRepository;

    public NonConformityQuery(
            NonconformityRepository nonconformityRepository
    ){
        this.nonconformityRepository =nonconformityRepository;
    }

    public NonConformity byId(Long id){
        return  nonconformityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Não conformidade não encontrada. Verifique o ID informado e tente novamente."));

    };

    public NonConformity byIdWithAll(Long id){
        return nonconformityRepository.findByIdWithAll(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Não conformidade não encontrada.")
                );
    }

    public List<NonConformity> getAll(){
         return nonconformityRepository.findAll();
    }

    public List<NonConformity> findByTitle(String title){
        return  nonconformityRepository.findTop5ByTitleStartingWithIgnoreCase(title);
    }

    public List<NonConformity> getTwentyByStatus(NonConformityStatus status){
        return nonconformityRepository.findTop20AllByStatus(status);
    }

    public void save(NonConformity nc){nonconformityRepository.save(nc);}
}
