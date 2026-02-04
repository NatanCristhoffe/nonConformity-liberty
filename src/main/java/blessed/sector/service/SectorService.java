package blessed.sector.service;

import blessed.exception.BusinessException;
import blessed.sector.entity.Sector;
import blessed.sector.dto.SectorRequestDTO;
import blessed.sector.dto.SectorResponseDTO;
import blessed.sector.service.query.SectorQuery;
import blessed.user.entity.User;
import blessed.user.service.query.UserQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectorService {

    private final SectorQuery sectorQuery;
    private final UserQuery userQuery;

    public  SectorService(SectorQuery sectorQuery, UserQuery userQuery){
        this.sectorQuery = sectorQuery;
        this.userQuery = userQuery;
    }

    public List<SectorResponseDTO> getAll(){
        return sectorQuery.getAll();
    }

    public List<SectorResponseDTO> getByName(String name, boolean getNotActive, User userRequest){
        User user = userQuery.byId(userRequest.getId());
        System.out.println(getNotActive);
        boolean includeInactive = user.isAdmin() && getNotActive;


        return sectorQuery.getByName(name, includeInactive);

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
