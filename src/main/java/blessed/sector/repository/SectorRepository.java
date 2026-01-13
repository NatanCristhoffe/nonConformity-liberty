package blessed.sector.repository;

import blessed.sector.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectorRepository extends JpaRepository<Sector, Long> {

    boolean existsByName(String name);
}
