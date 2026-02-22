package blessed.sector.service.query;

import blessed.exception.BusinessException;
import blessed.exception.ResourceNotFoundException;
import blessed.sector.dto.SectorRequestDTO;
import blessed.sector.dto.SectorResponseDTO;
import blessed.sector.entity.Sector;
import blessed.sector.repository.SectorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectorQuery {

    private final SectorRepository repository;
    public SectorQuery(SectorRepository repository){
        this.repository = repository;
    }

    public List<SectorResponseDTO> getAll(){
        return repository.findAllActive()
                .stream()
                .map(SectorResponseDTO::new)
                .toList();
    }

    public Sector byId(Long idSector){
        return repository.findById(idSector)
                .orElseThrow(() -> new ResourceNotFoundException("Setor não encontrado!"));
    }

    public Sector save(Sector sector){
        if (repository.existsByName(sector.getName().toLowerCase())){
            throw new BusinessException("Você já tem esse setor salvo.");
        }
        return repository.save(sector);
    }

    public Integer countByActive(boolean active){
        return  repository.countByActive(active);
    }

    public List<SectorResponseDTO> getByName(String name, boolean includeInactive){
        return repository.findByName(name, includeInactive)
                .stream()
                .map(SectorResponseDTO::new)
                .toList();
    }
}
