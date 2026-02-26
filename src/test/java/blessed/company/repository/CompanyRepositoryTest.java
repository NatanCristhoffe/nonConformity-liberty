package blessed.company.repository;

import blessed.company.entity.Company;
import blessed.company.enums.PlanType;
import blessed.company.enums.TypeDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void shouldReturnTrueWhenDocumentExists(){
        createCompany();
        boolean exists = companyRepository.existsByDocument("12345678000199");

        assertTrue(exists);
    }

    @Test
    void shouldReturnFalseWhenDocumentDoesNotExist() {
        boolean exists = companyRepository.existsByDocument("00000000000000");

        assertFalse(exists);
    }

    @Test
    void shouldReturnFalseWhenPhoneDoesNotExists(){
        createCompany();
        boolean phoneExists = companyRepository.existsByPhone("4100000001");
        assertFalse(phoneExists);
    }

    @Test
    void shouldReturnTrueWhenPhoneExists(){
        createCompany();
        boolean phoneExists = companyRepository.existsByPhone("4100000000");
        assertTrue(phoneExists);
    }


    private void createCompany() {
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

        companyRepository.save(company);
    }
}