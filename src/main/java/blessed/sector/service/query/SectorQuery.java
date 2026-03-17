package blessed.sector.service.query;

import blessed.auth.utils.CurrentUser;
import blessed.exception.BusinessException;
import blessed.exception.ResourceNotFoundException;
import blessed.sector.dto.SectorRequestDTO;
import blessed.sector.dto.SectorResponseDTO;
import blessed.sector.entity.Sector;
import blessed.sector.repository.SectorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SectorQuery {

    private final SectorRepository repository;

    public SectorQuery(SectorRepository repository, CurrentUser currentUser){
        this.repository = repository;
    }

    public List<SectorResponseDTO> getAll(UUID companyId){
        return repository.findAllActive(companyId)
                .stream()
                .map(SectorResponseDTO::new)
                .toList();
    }

    public Sector byId(Long sectorId, UUID companyId){
        return repository.findByIdAndCompanyId(sectorId, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Setor não encontrado"));
    }

    public Long countByCompany(UUID companyId){
        return repository.countByCompanyIdAndActiveTrue(companyId);
    };

    public Sector save(Sector sector, UUID companyId){
        if (repository.existsByNameIgnoreCaseAndCompanyId(sector.getName().toLowerCase(), companyId)){
            throw new BusinessException("Você já tem esse setor salvo.");
        }
        return repository.save(sector);
    }

    public Integer countByActive(boolean active, UUID companyId){
        return  repository.countByActiveAndCompanyId(active, companyId);
    }

    public List<SectorResponseDTO> getByName(
            String name, boolean includeInactive,
            UUID companyId
    ){
        return repository.findByName(name, includeInactive, companyId)
                .stream()
                .map(SectorResponseDTO::new)
                .toList();
    }
}
