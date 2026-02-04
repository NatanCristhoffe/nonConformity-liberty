package blessed.sector.service;

import blessed.exception.BusinessException;
import blessed.sector.entity.Sector;
import blessed.sector.dto.SectorRequestDTO;
import blessed.sector.dto.SectorResponseDTO;
import blessed.sector.service.query.SectorQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectorService {

    private final SectorQuery sectorQuery;
    public  SectorService(SectorQuery sectorQuery){
        this.sectorQuery = sectorQuery;
    }

    public List<SectorResponseDTO> getAll(){
        return sectorQuery.getAll();
    }

    @Transactional
    public Sector create(SectorRequestDTO data){
        if (sectorQuery.countByActive(true) >= 15){
            throw new BusinessException("Número máximo de setores ativos atingido.");
        }
        return sectorQuery.save(data);
    }

    @Transactional
    public SectorResponseDTO update(Long id, SectorRequestDTO dataUpdate){
        Sector sector = sectorQuery.byId(id);

        sector.setName(dataUpdate.name());
        sector.setDescription(dataUpdate.description());

        return new SectorResponseDTO(sector);
    }

    @Transactional
    public void enable(Long id){
        Sector sector = sectorQuery.byId(id);
        sector.enable();
    }

    @Transactional
    public void disable(Long id){
        Sector sector = sectorQuery.byId(id);
        sector.disable();
    }


}
