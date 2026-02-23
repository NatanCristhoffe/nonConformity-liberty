package blessed.application.service;


import blessed.application.dto.CompanyWithAdminRequestDTO;
import blessed.auth.dto.RegisterDTO;
import blessed.company.dto.CompanyRequestDTO;
import blessed.company.entity.Company;
import blessed.company.service.CompanyService;
import blessed.exception.BusinessException;
import blessed.sector.entity.Sector;
import blessed.sector.service.SectorService;
import blessed.user.entity.User;
import blessed.user.service.UserService;
import blessed.user.service.query.UserQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CreateCompanyWithAdminService {
    private final CompanyService companyService;
    private final UserService userService;
    private final SectorService sectorService;

    CreateCompanyWithAdminService(
            CompanyService companyService,
            UserService userService,
            SectorService sectorService
    ){
        this.companyService = companyService;
        this.userService = userService;
        this.sectorService = sectorService;
    }

    @Transactional
    public void createCompanyWithAdmin(
            CompanyWithAdminRequestDTO data
    ){
            Company company = companyService.create(data.company());
            Sector sector = sectorService.create(data.sector(), company.getId());
            userService.register(data.admin(), company, sector);
    }
}
