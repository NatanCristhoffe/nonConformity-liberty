package blessed.sector.repository;

import blessed.sector.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SectorRepository extends JpaRepository<Sector, Long> {
    boolean existsByName(String name);
    Integer countByActive(boolean active);

    @Query("""
        SELECT s FROM Sector s WHERE s.active = true 
    """)
    List<Sector> findAllActive();

    @Query("""
    SELECT s FROM Sector s
    WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))
    AND (:includeInactive = true OR s.active = true)
""")
    List<Sector> findByName(
            @Param("name") String name,
            @Param("includeInactive") boolean includeInactive
    );

}
