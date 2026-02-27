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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class NonConformityRepositoryFindByUserTest {
    private final NonconformityRepository nonconformityRepository;
    private final SectorRepository sectorRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    NonConformityRepositoryFindByUserTest(
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
    void shouldReturnNonConformityWhenUserIsLinked(){
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

        Page<NonConformity> found = nonconformityRepository.findByUser(user.getId(), company.getId(), pageable);

        assertFalse(found.isEmpty());
        assertEquals(1, found.getTotalElements());
    }

    @Test
    void shouldReturnEmptyWhenUserNotLinked(){
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

        Page<NonConformity> found = nonconformityRepository.findByUser(
                UUID.randomUUID(), user.getCompany().getId(), pageable
        );

        assertTrue(found.isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenCompanyNotLinked(){
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

        Page<NonConformity> found = nonconformityRepository.findByUser(
                user.getId(), UUID.randomUUID(), pageable
        );

        assertTrue(found.isEmpty());
    }

}
