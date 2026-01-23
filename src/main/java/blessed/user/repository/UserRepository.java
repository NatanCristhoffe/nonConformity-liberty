package blessed.user.repository;

import blessed.user.entity.User;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<UserDetails> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    List<User> findByFirstNameStartingWithIgnoreCase(String firstName, Limit limit);
}
