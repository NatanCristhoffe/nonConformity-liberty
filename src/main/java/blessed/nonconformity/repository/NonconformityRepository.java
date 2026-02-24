package blessed.nonconformity.repository;

import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.enums.NonConformityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NonconformityRepository extends JpaRepository<NonConformity, Long> {
    NonConformity findByIdAndCompanyId(Long  id, UUID companyId);

    @Query("""
    SELECT nc FROM NonConformity nc
    WHERE (
        nc.createdBy.id = :userId OR
        nc.dispositionOwner.id = :userId OR
        nc.effectivenessAnalyst.id = :userId
            )
    AND nc.company.id = :companyId
    """)
    Page<NonConformity> findByUser(
            @Param("userId") UUID userId,
            @Param("companyId") UUID companyId,
            Pageable pageable);

    Page<NonConformity> findAllByCompanyIdOrderByCreatedAtDesc(
            UUID companyId,
            Pageable pageable
    );

    @Query("""
    SELECT nc FROM NonConformity nc
    WHERE LOWER(nc.title) LIKE LOWER(CONCAT('%', :title, '%'))
    AND nc.company.id = :companyId
    """)
    List<NonConformity> findTopByTitleAndCompany(
            @Param("title") String title,
            @Param("companyId") UUID companyId,
            Pageable pageable
    );

    @Query("""
    SELECT nc FROM NonConformity nc
    WHERE nc.company.id = :companyId
    AND nc.status = :status
    """)
    Page<NonConformity> findByStatus(
            @Param("status") NonConformityStatus status,
            @Param("companyId") UUID companyId,
            Pageable pageable
    );

    boolean existsByIdAndDispositionOwnerId(Long id, UUID dispositionOwnerId);
    boolean existsByIdAndEffectivenessAnalystId(Long id, UUID effectivenessAnalystId);


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

    @Query("""
    select distinct nc
    from NonConformity nc
    left join fetch nc.actions a
    left join fetch a.responsibleUser
    left join fetch a.finalizedBy
    left join fetch nc.effectivenessAnalysis
    where nc.id = :id
    and nc.company.id = :companyId
    """)
    Optional<NonConformity> findByIdWithAll(
            @Param("id") Long id,
            @Param("companyId") UUID companyId
    );

    @Query("""
        select distinct nc
        from NonConformity nc
        left join nc.actions a
        where nc.company.id = :companyId
        and nc.status = :status
        and (
            nc.createdBy.id = :userId
            or nc.dispositionOwner.id = :userId
            or nc.effectivenessAnalyst.id = :userId
            or (
                a.responsibleUser.id = :userId
                and a.status = 'PENDING'
            )
        )
    """)
    Page<NonConformity> findMyNonconformitiesByStatus(
            @Param("status") NonConformityStatus status,
            @Param("userId") UUID userId,
            @Param("companyId") UUID companyId,
            Pageable pageable
    );

    @Query("""
        select
            case when count(nc) > 0 then true else false end
        from NonConformity nc
            left join nc.actions a
        where nc.id = :idRnc
          and (
               nc.createdBy.id = :idUser
            or nc.dispositionOwner.id = :idUser
            or nc.effectivenessAnalyst.id = :idUser
            or a.responsibleUser.id = :idUser
          )
    """)
    boolean hasLink(
            @Param("idRnc") Long idRnc,
            @Param("idUser") UUID idUser
    );
}
