package blessed.sector.service;

import blessed.company.entity.Company;
import blessed.company.service.query.CompanyQuery;
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
import java.util.UUID;

@Service
public class SectorService {

    private final SectorQuery sectorQuery;
    private final UserQuery userQuery;
    private final CompanyQuery companyQuery;

    public  SectorService(
            SectorQuery sectorQuery, UserQuery userQuery,
            CompanyQuery companyQuery
    ){
        this.sectorQuery = sectorQuery;
        this.userQuery = userQuery;
        this.companyQuery = companyQuery;
    }

    @Transactional
    public List<SectorResponseDTO> getAll(UUID companyId){
        Company company = companyQuery.byId(companyId);
        return sectorQuery.getAll(company.getId());
    }

    public List<SectorResponseDTO> getByName(String name, boolean getNotActive, User userRequest, UUID companyId){
        User user = userQuery.byId(userRequest.getId());

        boolean includeInactive = user.isAdmin() && getNotActive;
        return sectorQuery.getByName(name, includeInactive, companyId);
    }

    @Transactional
    public Sector create(SectorRequestDTO data, UUID companyId){
        if (sectorQuery.countByActive(true) >= 15){
            throw new BusinessException("Número máximo de setores ativos atingido.");
        }

        Company company = companyQuery.byId(companyId);
        Sector sector = new Sector(data, company);
        return sectorQuery.save(sector, companyId);
    }

    @Transactional
    public SectorResponseDTO update(Long id, SectorRequestDTO dataUpdate, UUID companyId){
        Sector sector = sectorQuery.byId(id);
        verifyIfSectorPercentTheCompany(companyId, sector);

        sector.setName(dataUpdate.name());
        sector.setDescription(dataUpdate.description());

        return new SectorResponseDTO(sector);
    }

    @Transactional
    public void enable(Long id, UUID companyId){
        Sector sector = sectorQuery.byId(id);
        verifyIfSectorPercentTheCompany(companyId, sector);
        sector.enable();
    }

    @Transactional
    public void disable(Long id, UUID companyId){
        Sector sector = sectorQuery.byId(id);
        verifyIfSectorPercentTheCompany(companyId, sector);

        sector.disable();
    }

    private void verifyIfSectorPercentTheCompany(UUID companyId, Sector sector){
        Company company = companyQuery.byId(companyId);

        if (!sector.getCompany().getId().equals(company.getId())){
            throw new BusinessException("Você não pode atualizar os dados desse setor.");
        }
    }


}
