package blessed.user.repository;

import blessed.user.entity.User;
import blessed.user.enums.UserRole;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    List<User> findAllByCompanyId(UUID companyId);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);


    @Query("""
    SELECT u FROM users u
    WHERE u.company.id = :companyId
    AND u.id = :userId AND u.enabled = true
    """)
    Optional<User> findById(@Param("companyId") UUID companyId, @Param("userId") UUID userId);


    @Query("""
        SELECT u FROM users u
        WHERE u.company.id = :companyId
        AND (
                LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%'))
                OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%'))
            )
        AND (:role IS NULL OR u.role = :role)
        """)
    List<User> findByFirstNameAndRole(
            @Param("name") String name,
            @Param("role") UserRole role,
            @Param("companyId") UUID companyId,
            Limit limit
    );

    @Query("""
    SELECT COUNT(u)
    FROM users u
    WHERE u.company.id = :companyId
    AND u.enabled = true
    """)
    Long countActiveUsersByCompany(@Param("companyId") UUID companyId);
}
