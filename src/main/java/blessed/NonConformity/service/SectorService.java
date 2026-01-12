package blessed.NonConformity.service;

import blessed.NonConformity.exception.BusinessException;
import blessed.NonConformity.sectors.Sector;
import blessed.NonConformity.sectors.SectorRepository;
import blessed.NonConformity.sectors.SectorRequestDTO;
import blessed.NonConformity.sectors.SectorResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectorService {
    private final SectorRepository repository;

    public SectorService(SectorRepository repository) {
        this.repository = repository;
    }

    public List<SectorResponseDTO> getAll(){
        List<SectorResponseDTO> sectors = repository
                .findAll()
                .stream()
                .map(SectorResponseDTO::new)
                .toList();

        return sectors;
    }

    public Sector create(SectorRequestDTO data){
        Sector sector = new Sector(data);
        String normalizedName = data.name().trim().toLowerCase();

        if(repository.existsByName(normalizedName)){
            throw new BusinessException("Setor já cadastrado");
        }

        return repository.save(new Sector(data));
    }
}
