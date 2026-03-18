package blessed.notification.repository;

import blessed.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByUserIdAndReadFalse(Long userId);
    Optional<Notification> findById(UUID id);

    @Query("""
    SELECT n FROM Notification n
    WHERE n.user.id =  :userId
    """)
    Page<Notification> findByUser(@Param("userId") UUID userId, Pageable pageable);
}
