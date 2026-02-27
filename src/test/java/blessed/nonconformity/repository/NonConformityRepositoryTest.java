package blessed.nonconformity.repository;

import blessed.company.entity.Company;
import blessed.company.repository.CompanyRepository;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.entity.RootCause;
import blessed.nonconformity.tools.FiveWhyTool;
import blessed.sector.entity.Sector;
import blessed.sector.repository.SectorRepository;
import blessed.user.entity.User;
import blessed.user.repository.UserRepository;
import blessed.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("test")
public class NonConformityRepositoryTest {

    private final NonconformityRepository nonconformityRepository;
    private final SectorRepository sectorRepository;
    private final UserRepository userRepository;
    private final RootCauseRepository rootCauseRepository;
    private final FiveWhyToolRepository fiveWhyToolRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    NonConformityRepositoryTest(
            NonconformityRepository nonconformityRepository,
            RootCauseRepository rootCauseRepository,
            FiveWhyToolRepository fiveWhyToolRepository,
            CompanyRepository companyRepository,
            SectorRepository sectorRepository,
            UserRepository userRepository
    ){
        this.nonconformityRepository = nonconformityRepository;
        this.rootCauseRepository = rootCauseRepository;
        this.fiveWhyToolRepository = fiveWhyToolRepository;
        this.companyRepository = companyRepository;
        this.sectorRepository = sectorRepository;
        this.userRepository = userRepository;
    }

    @Test
    void shouldReturnTrueWhenFindByIdAndCompanyId(){
        NonConformity nonConformity = create();
        nonconformityRepository.save(nonConformity);

        Optional<NonConformity> found = nonconformityRepository.findByIdAndCompanyId(
                nonConformity.getId(),
                nonConformity.getCompany().getId()
        );

        assertTrue(found.isPresent());
        assertEquals(nonConformity.getId(), found.get().getId());
    }
    @Test
    void shouldReturnFalseWhenFindByIdAndCompanyId(){
        Optional<NonConformity> found = nonconformityRepository.findByIdAndCompanyId(
                198L,
                UUID.randomUUID()
        );

        assertFalse(found.isPresent());
    }







    private NonConformity create(){
        Company company = companyRepository.save(
                TestDataFactory.createCompany()
        );

        Sector sector = sectorRepository.save(
                TestDataFactory.createSector(company)
        );

        User user = userRepository.save(
                TestDataFactory.createUser(company, sector)
        );

        NonConformity nonConformity =
                TestDataFactory.createNonConformity(company, sector, user);

        RootCause rootCause = TestDataFactory.createRootCause(nonConformity, user);
        FiveWhyTool fiveWhyTool = TestDataFactory.createFiveWhy(nonConformity);

        nonConformity.setRootCause(rootCause);
        nonConformity.setFiveWhyTool(fiveWhyTool);

        return nonConformity;
    }

}
