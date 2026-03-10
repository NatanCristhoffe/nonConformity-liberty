package blessed.notification.service;

import blessed.notification.dto.NotificationResponseDTO;
import blessed.notification.entity.Notification;
import blessed.notification.enums.NotificationType;
import blessed.notification.repository.NotificationRepository;
import blessed.notification.service.query.NotificationQuery;
import blessed.user.entity.User;
import blessed.user.service.query.UserQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class NotificationService {

    private final NotificationQuery notificationQuery;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserQuery userQuery;
    @Autowired
    private SimpUserRegistry simpUserRegistry;

    public NotificationService(
            NotificationQuery notificationQuery,
            SimpMessagingTemplate messagingTemplate,
            UserQuery userQuery
    ){
        this.notificationQuery = notificationQuery;
        this.messagingTemplate = messagingTemplate;
        this.userQuery = userQuery;
    }

    public void notifyIfNotSameUser(
            UUID receiverId,
            UUID senderId,
            UUID companyId,
            NotificationType type,
            String reference
    ){
        if (!receiverId.equals(senderId)){
            notifyByUser(receiverId, companyId, type, reference);
        }
    }

    @Transactional
    public void notifyByUser(UUID userId, UUID companyId, NotificationType type, String reference){
        User user = userQuery.byId(companyId, userId);
        Notification notification = new Notification(user, type, reference);

        messagingTemplate.convertAndSendToUser(
                user.getEmail(),
                "/queue/notifications",
                new NotificationResponseDTO(notification)
        );
    }

    @Transactional
    public void markAsRead(UUID id){
        Notification notification = notificationQuery.byId(id);
        notification.markAsRead();
    }
}
