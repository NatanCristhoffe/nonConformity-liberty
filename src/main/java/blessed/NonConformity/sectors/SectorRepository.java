package blessed.NonConformity.sectors;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SectorRepository extends JpaRepository<Sector, Long> {

    boolean existsByName(String name);
}
