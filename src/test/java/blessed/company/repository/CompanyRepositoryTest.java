package blessed.company.repository;

import blessed.company.entity.Company;
import blessed.company.enums.PlanType;
import blessed.company.enums.TypeDocument;
import blessed.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CompanyRepositoryTest {

    private final CompanyRepository companyRepository;
    @Autowired
    CompanyRepositoryTest(CompanyRepository companyRepository){
        this.companyRepository = companyRepository;
    }

    @Test
    void shouldReturnTrueWhenDocumentExists(){
        companyRepository.save(TestDataFactory.createCompany());

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

        boolean phoneExists = companyRepository.existsByPhone("4100000001");
        assertFalse(phoneExists);
    }

    @Test
    void shouldReturnTrueWhenPhoneExists(){
        companyRepository.save(TestDataFactory.createCompany());

        boolean phoneExists = companyRepository.existsByPhone("4100000000");
        assertTrue(phoneExists);
    }

}