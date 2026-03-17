package blessed.sector.service;

import blessed.auth.utils.CurrentUser;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SectorService {

    private final SectorQuery sectorQuery;
    private final CurrentUser currentUser;

    public  SectorService(
            SectorQuery sectorQuery,
            CurrentUser currentUser
    ){
        this.sectorQuery = sectorQuery;
        this.currentUser = currentUser;
    }

    @Transactional
    public List<SectorResponseDTO> getAll(){
        return sectorQuery.getAll(currentUser.getCompanyId());
    }

    public List<SectorResponseDTO> getByName(String name, boolean getNotActive){

        boolean includeInactive = currentUser.isAdmin() && getNotActive;
        return sectorQuery.getByName(
                name, includeInactive,
                currentUser.getCompanyId());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Sector create(SectorRequestDTO data){
        if (sectorQuery.countByActive(true, currentUser.getCompanyId()) >= 20){
            throw new BusinessException("Número máximo de setores ativos atingido.");
        }
        Sector sector = new Sector(data, currentUser.getCompany());
        return sectorQuery.save(sector,currentUser.getCompanyId());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public SectorResponseDTO update(Long id, SectorRequestDTO data){
        Sector sector = sectorQuery.byId(id, currentUser.getCompanyId());
        sector.update(data);

        return new SectorResponseDTO(sector);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void enable(Long id){
        if (sectorQuery.countByActive(true, currentUser.getCompanyId()) >= 20){
            throw new BusinessException("Número máximo de setores ativos atingido.");
        }
        Sector sector = sectorQuery.byId(id, currentUser.getCompanyId());
        sector.enable();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void disable(Long idSector){
        Sector sector = sectorQuery.byId(idSector, currentUser.getCompanyId());
        if(!sector.isActive()){
            throw  new BusinessException("O setor já está desabilitado");
        }

        Long activeSectors = sectorQuery.countByCompany(currentUser.getCompanyId());
        if(activeSectors <= 1){
          throw  new BusinessException("A empresa deve possuir pelo menos um setor ativo");
        }

        sector.disable();
    }
}
