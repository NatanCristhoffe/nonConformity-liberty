package blessed.util;

import blessed.company.entity.Company;
import blessed.company.enums.PlanType;
import blessed.company.enums.TypeDocument;
import blessed.company.repository.CompanyRepository;
import blessed.nonconformity.entity.FiveWhy;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.entity.RootCause;
import blessed.nonconformity.enums.NonConformityPriorityLevel;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.enums.QualityToolType;
import blessed.nonconformity.repository.NonconformityRepository;
import blessed.nonconformity.repository.RootCauseRepository;
import blessed.nonconformity.tools.FiveWhyTool;
import blessed.sector.entity.Sector;
import blessed.sector.repository.SectorRepository;
import blessed.user.entity.User;
import blessed.user.enums.UserRole;
import blessed.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class TestDataFactory {

    public static Company createCompany() {
        Company company = new Company();
        company.setCompanyName("teste company");
        company.setDocument("12345678000199");
        company.setPlanType(PlanType.ENTERPRISE);
        company.setTypeDocument(TypeDocument.CNPJ);
        company.setPhone("4100000000");
        company.setEmail("test@test.com");
        company.setStreet("rua teste");
        company.setNumberStreet(241);
        company.setCity("São José");
        company.setState("PR");
        company.setActive(true);
        company.setCreateAt(LocalDateTime.now());
        company.setUpdateAt(LocalDateTime.now());
        return company;
    }

    public static User createUser(Company company, Sector sector) {
        User user = new User();
        user.setFirstName("teste");
        user.setLastName("teste");
        user.setEmail("test@test.com");
        user.setPassword("123456789");
        user.setPhone("4100000000");
        user.setRole(UserRole.USER);
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());
        user.setSector(sector);
        user.setCompany(company);
        return user;
    }

    public static Sector createSector(Company company){
        Sector sector = new Sector();

        sector.setName("test sector");
        sector.setDescription("test description");
        sector.setActive(true);
        sector.setCompany(company);

        return  sector;
    }

//    public static NonConformity createValidNonConformity(){
//        Company company = createCompany();
//        Sector sector = createSector(company);
//        User user = createUser(company, sector);
//
//        return  createNonConformity(company, sector, user);
//    }

    public static NonConformity  createNonConformity(
            Company company,
            Sector sector,
            User user
    ) {

        NonConformity nonConformity = new NonConformity();

        nonConformity.setCompany(company);

        nonConformity.setTitle("RNC - Falha no processo produtivo");
        nonConformity.setDescription("Descrição detalhada da não conformidade identificada durante auditoria interna.");
        nonConformity.setHasAccidentRisk(false);

        nonConformity.setPriorityLevel(NonConformityPriorityLevel.MEDIUM);
        nonConformity.setStatus(NonConformityStatus.PENDING);

        nonConformity.setDispositionDate(LocalDateTime.now().plusDays(7));
        nonConformity.setCreatedAt(LocalDateTime.now());

        nonConformity.setUrlEvidence("https://example.com/evidence.jpg");

        nonConformity.setDispositionOwner(user);
        nonConformity.setEffectivenessAnalyst(user);
        nonConformity.setCreatedBy(user);

        nonConformity.setSourceDepartment(sector);
        nonConformity.setResponsibleDepartment(sector);

        nonConformity.setRequiresQualityTool(false);

        return nonConformity;
    }

    public static RootCause createRootCause(NonConformity nc, User user){
        RootCause rootCause = new RootCause();
        rootCause.setNonconformity(nc);
        rootCause.setDescription("Falta de treinamento adequado do operador.");
        rootCause.setUserCreated(user);

        return rootCause;
    }

    public static FiveWhyTool createFiveWhy(NonConformity nc){
        FiveWhyTool fiveWhyTool = new FiveWhyTool();
        fiveWhyTool.setNonconformity(nc);
        fiveWhyTool.addWhy(new FiveWhy(1, "Erro operacional.", fiveWhyTool));
        fiveWhyTool.addWhy(new FiveWhy(2,"Operador não conhecia o procedimento.", fiveWhyTool));
        fiveWhyTool.addWhy(new FiveWhy(3,"Treinamento não foi realizado.",fiveWhyTool));
        fiveWhyTool.addWhy(new FiveWhy(4,"Falha no planejamento de integração.", fiveWhyTool));
        fiveWhyTool.addWhy(new FiveWhy(5,"Ausência de checklist de onboarding.", fiveWhyTool));

        return fiveWhyTool;
    }



}