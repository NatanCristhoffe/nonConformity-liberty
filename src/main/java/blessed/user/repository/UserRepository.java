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
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);


    @Query("""
        SELECT u FROM users u
        WHERE LOWER(u.firstName) LIKE LOWER(CONCAT(:firstName, "%"))
        AND (:role IS NULL OR u.role = :role)
        """)
    List<User> findByFirstNameAndRole(
            @Param("firstName") String firstName,
            @Param("role") UserRole role,
            Limit limit
    );
}
