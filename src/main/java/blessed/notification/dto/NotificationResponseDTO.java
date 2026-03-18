package blessed.notification.dto;

import blessed.notification.entity.Notification;
import blessed.notification.enums.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationResponseDTO(
        UUID id,
        String message,
        NotificationType type,
        boolean read,
        LocalDateTime createdAt
) {
    public NotificationResponseDTO(Notification data) {
        this(data.getId(), data.getMessage(), data.getType(), data.isRead(),data.getCreatedAt());
    }
}
