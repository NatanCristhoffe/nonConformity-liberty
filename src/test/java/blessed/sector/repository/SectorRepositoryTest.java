package blessed.sector.repository;

import blessed.company.entity.Company;
import blessed.company.repository.CompanyRepository;
import blessed.sector.entity.Sector;
import blessed.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class SectorRepositoryTest {
    private final SectorRepository sectorRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    SectorRepositoryTest(SectorRepository sectorRepository, CompanyRepository companyRepository){
        this.sectorRepository = sectorRepository;
        this.companyRepository = companyRepository;
    }

    @Test
    void shouldReturnTrueWhenFindByNameAndCompany(){
        Sector sector = create();

        Optional<Sector> found = sectorRepository.findByIdAndCompanyId(
                sector.getId(),
                sector.getCompany().getId()
        );

        assertFalse(found.isEmpty());
    }


    @Test
    void shouldReturnTrueWhenFindSectorByName(){
        Sector sector = create();

        List<Sector> sectors =  sectorRepository.findByName(
                sector.getName(),
                false,
                sector.getCompany().getId()
        );

        assertFalse(sectors.isEmpty());
        assertEquals(1, sectors.size());

        Sector foundSector = sectors.get(0);

        assertEquals(sector.getName(), foundSector.getName());
        assertEquals(sector.getCompany().getId(), foundSector.getCompany().getId());
    }

    @Test
    void shouldReturnSectorsWhenActive(){
        Sector sector = create();

        List<Sector> sectors = sectorRepository.findAllActive(sector.getCompany().getId());

        assertFalse(sectors.isEmpty());
        assertEquals(1, sectors.size());
    }

    private Sector create(){
        Company company = TestDataFactory.createCompany();
        companyRepository.save(company);
        return sectorRepository.save(TestDataFactory.createSector(company));

    }
}
