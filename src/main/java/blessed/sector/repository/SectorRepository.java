package blessed.sector.repository;

import blessed.sector.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SectorRepository extends JpaRepository<Sector, Long> {
    boolean existsByName(String name);

    @Query("""
        SELECT s FROM Sector s WHERE s.active = true 
    """)
    List<Sector> findAllActive();

    Integer countByActive(boolean active);

}
