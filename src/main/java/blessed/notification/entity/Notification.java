package blessed.notification.entity;

import blessed.notification.enums.NotificationType;
import blessed.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "notification",
        indexes = {
                @Index(name = "idx_notification_user", columnList = "user_id"),
                @Index(name = "idx_notification_read", columnList = "read")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String message;

    private boolean isRead;
    private LocalDateTime readAt;

    private LocalDateTime createdAt;

    private NotificationType type;

    public Notification(
            User user,
            NotificationType type, String reference
    ){
        this.user = user;
        this.message = type.buildMessage(reference);
        this.isRead = false;
        this.type = type;
        this.createdAt = LocalDateTime.now();
    }

    public void markAsRead(){
        if(!this.isRead){
            this.setRead(true);
            this.setReadAt(LocalDateTime.now());
        }
    }

}
