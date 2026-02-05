package blessed.nonconformity.service.query;

import blessed.exception.ResourceNotFoundException;
import blessed.nonconformity.dto.NonconformityResponseDTO;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.repository.NonconformityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    public Page<NonConformity> getAll(Pageable pageable){
         return nonconformityRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Page<NonConformity> getAllUser(UUID user, Pageable pageable){
        return nonconformityRepository.findByUser(user, pageable);
    }

    public List<NonConformity> findByTitle(String title){
        return  nonconformityRepository.findTop5ByTitleStartingWithIgnoreCase(title);
    }

    public Page<NonConformity> findAllByStatus(
            NonConformityStatus status,
            Pageable pageable
    ) {
        return nonconformityRepository.findByStatus(status, pageable);
    }


    public void save(NonConformity nc){nonconformityRepository.save(nc);}


    public Page<NonConformity> findMyByStatus(
            NonConformityStatus status,
            UUID userId,
            Pageable pageable
    ) {
        return nonconformityRepository
                .findMyNonconformitiesByStatus(status, userId, pageable);
    }

    public boolean existsByIdAndDispositionOwnerId(Long ncId, UUID idUSer){
        return nonconformityRepository.existsByIdAndDispositionOwnerId(ncId, idUSer);
    }

    public boolean existsByIdAndEffectivenessAnalystId(Long ncId, UUID idUSer){
        return nonconformityRepository.existsByIdAndEffectivenessAnalystId(ncId, idUSer);
    }

    public boolean hasLink(Long ncId, UUID userId){
        return nonconformityRepository.hasLink(ncId, userId);

    }
}
