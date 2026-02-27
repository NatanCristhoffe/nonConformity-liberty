package blessed.nonconformity.repository;

import blessed.company.entity.Company;
import blessed.company.repository.CompanyRepository;
import blessed.nonconformity.entity.NonConformity;
import blessed.sector.entity.Sector;
import blessed.sector.repository.SectorRepository;
import blessed.user.entity.User;
import blessed.user.repository.UserRepository;
import blessed.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class NonConformityRepositoryFindByTitleByCompanyTest {

    private final NonconformityRepository nonconformityRepository;
    private final SectorRepository sectorRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    NonConformityRepositoryFindByTitleByCompanyTest(
            NonconformityRepository nonconformityRepository,
            CompanyRepository companyRepository,
            SectorRepository sectorRepository,
            UserRepository userRepository
    ){
        this.nonconformityRepository = nonconformityRepository;
        this.companyRepository = companyRepository;
        this.sectorRepository = sectorRepository;
        this.userRepository = userRepository;
    }

    @Test
    void shouldReturnListNonConformityByTitleAndCompany(){
        Company company = companyRepository.save(
                TestDataFactory.createCompany()
        );
        Sector sector = sectorRepository.save(
                TestDataFactory.createSector(company)
        );
        User user = userRepository.save(
                TestDataFactory.createUser(company, sector)
        );

        NonConformity nonConformity = TestDataFactory.createNonConformity(
                company,
                sector,
                user
        );
        nonconformityRepository.save(nonConformity);
        Pageable pageable = PageRequest.of(0, 10);
        List<NonConformity> ncs = nonconformityRepository.findTopByTitleAndCompany(
                nonConformity.getTitle(),
                user.getCompany().getId(),
                pageable

        );

        assertFalse(ncs.isEmpty());
        assertEquals(ncs.get(0).getCompany().getId(), company.getId());

    }

    @Test
    void shouldReturnEmptyWhenNonConformityNotFoundByTitle(){
        Pageable pageable = PageRequest.of(0, 10);
        List<NonConformity> ncs = nonconformityRepository.findTopByTitleAndCompany(
                "title invalid",
                UUID.randomUUID(),
                pageable

        );
        assertTrue(ncs.isEmpty());
    }

}
