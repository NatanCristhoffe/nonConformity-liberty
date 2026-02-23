package blessed.sector.repository;

import blessed.sector.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SectorRepository extends JpaRepository<Sector, Long> {
    boolean existsByNameIgnoreCaseAndCompanyId(String name, UUID companyId);
    Integer countByActive(boolean active);

    @Query("""
        SELECT s FROM Sector s WHERE s.active = true AND s.company.id = :companyId
    """)
    List<Sector> findAllActive(@Param("companyId") UUID companyId);

    @Query("""
    SELECT s FROM Sector s
    WHERE s.company.id = :companyId
    AND LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))
    AND (:includeInactive = true OR s.active = true)
""")
    List<Sector> findByName(
            @Param("name") String name,
            @Param("includeInactive") boolean includeInactive,
            @Param("companyId") UUID companyId
    );

}
