package blessed.nonconformity.repository;

import blessed.nonconformity.dto.NonconformityResponseDTO;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.enums.NonConformityStatus;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NonconformityRepository extends JpaRepository<NonConformity, Long> {
    List<NonConformity> findTop5ByTitleStartingWithIgnoreCase(String title);
    List<NonConformity> findTop20AllByStatus(NonConformityStatus status);

    @Query("""
    SELECT nc.status, COUNT(nc)
    FROM NonConformity nc
    GROUP BY nc.status
    """)
    List<Object[]> countByStatus();
    @Query("""
    SELECT nc.priorityLevel, COUNT(nc)
    FROM NonConformity nc
    GROUP BY nc.priorityLevel
    """)
    List<Object[]> countByPriority();

    @Query("""
    SELECT d.id, d.name, COUNT (nc)
    FROM NonConformity  nc
    JOIN nc.responsibleDepartment d
    GROUP BY d.id, d.name
    """)
    List<Object[]> countByDepartment();
    @Query("""
    SELECT 
        FUNCTION('DATE_FORMAT', nc.createdAt, '%Y-%m'),
        COUNT(nc),
        SUM(CASE WHEN nc.status = 'CLOSED' THEN 1 ELSE 0 END)
    FROM NonConformity nc
    GROUP BY FUNCTION('DATE_FORMAT', nc.createdAt, '%Y-%m')
    ORDER BY 1 DESC
    """)
    List<Object[]> trend();

    @Query("""
    SELECT AVG(DATEDIFF(nc.closedAt, nc.createdAt))
    FROM NonConformity nc
    WHERE nc.closedAt IS NOT NULL
    """)
    Double averageResolutionDays();
}
