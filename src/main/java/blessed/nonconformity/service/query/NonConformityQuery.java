package blessed.nonconformity.service.query;

import blessed.company.entity.Company;
import blessed.exception.ResourceNotFoundException;
import blessed.nonconformity.dto.NonconformityResponseDTO;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.repository.NonconformityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public NonConformity byId(Long id, UUID companyId){
        return  nonconformityRepository.findByIdAndCompanyId(id, companyId);
    };

    public NonConformity byIdWithAll(Long id, UUID companyId){
        return nonconformityRepository.findByIdWithAll(id, companyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Não conformidade não encontrada.")
                );
    }

    public Page<NonConformity> getAll(Pageable pageable){
         return nonconformityRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Page<NonConformity> getAllNonconformitiesByUser(UUID user, UUID companyId,Pageable pageable){
        return nonconformityRepository.findByUser(user, companyId, pageable);
    }

    public List<NonConformity> findByTitle(String title, UUID companyId){
        Pageable topFive = PageRequest.of(0, 5);
        return  nonconformityRepository.findTopByTitleAndCompany(title, companyId, topFive);
    }

    public Page<NonConformity> findAllByStatus(
            NonConformityStatus status,
            UUID companyId,
            Pageable pageable
    ) {
        return nonconformityRepository.findByStatus(status, companyId,pageable);
    }


    public void save(NonConformity nc){nonconformityRepository.save(nc);}


    public Page<NonConformity> findMyByStatus(
            NonConformityStatus status,
            UUID userId,
            UUID companyId,
            Pageable pageable
    ) {
        return nonconformityRepository
                .findMyNonconformitiesByStatus(status, userId, companyId, pageable);
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
