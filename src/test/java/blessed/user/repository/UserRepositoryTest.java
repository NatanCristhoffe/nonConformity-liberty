package blessed.user.repository;

import blessed.company.entity.Company;
import blessed.company.repository.CompanyRepository;
import blessed.exception.ResourceNotFoundException;
import blessed.sector.entity.Sector;
import blessed.sector.repository.SectorRepository;
import blessed.user.entity.User;
import blessed.user.enums.UserRole;
import blessed.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final SectorRepository sectorRepository;

    @Autowired
    UserRepositoryTest(
            UserRepository userRepository, CompanyRepository companyRepository,
            SectorRepository sectorRepository
    ){
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.sectorRepository = sectorRepository;
    }

    @Test
    void shouldReturnUserWhenIdAndCompanyIdExist(){
        User userNew = createUser();
        User user = userRepository.findById(
                userNew.getCompany().getId(),
                userNew.getId()
                )
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        assertNotNull(user);
        assertEquals(userNew.getId(), user.getId());
        assertEquals(userNew.getCompany().getId(), user.getCompany().getId());
        assertEquals("test@test.com", user.getEmail());
    }



    private User createUser(){
        Company company = companyRepository.save(TestDataFactory.createCompany());
        Sector sector = sectorRepository.save(TestDataFactory.createSector(company));

        return userRepository.save(TestDataFactory.createUser(company, sector));
    }
}