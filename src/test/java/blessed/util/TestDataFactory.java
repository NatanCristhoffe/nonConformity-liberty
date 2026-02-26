package blessed.util;

import blessed.company.entity.Company;
import blessed.company.enums.PlanType;
import blessed.company.enums.TypeDocument;
import blessed.sector.entity.Sector;
import blessed.user.entity.User;
import blessed.user.enums.UserRole;

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
}