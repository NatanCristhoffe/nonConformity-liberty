package blessed.notification.service;

import blessed.auth.utils.CurrentUser;
import blessed.notification.dto.NotificationResponseDTO;
import blessed.notification.entity.Notification;
import blessed.notification.enums.NotificationType;
import blessed.notification.repository.NotificationRepository;
import blessed.notification.service.query.NotificationQuery;
import blessed.user.entity.User;
import blessed.user.service.query.UserQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class NotificationService {

    private final NotificationQuery notificationQuery;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserQuery userQuery;
    private final CurrentUser currentUser;

    public NotificationService(
            NotificationQuery notificationQuery,
            SimpMessagingTemplate messagingTemplate,
            UserQuery userQuery,
            CurrentUser currentUser
    ){
        this.notificationQuery = notificationQuery;
        this.messagingTemplate = messagingTemplate;
        this.userQuery = userQuery;
        this.currentUser = currentUser;
    }

    public void notifyIfNotSameUser(
            Set<UUID> usersId,
            UUID senderId,
            UUID companyId,
            NotificationType type,
            String reference
    ){
        for (UUID userId : usersId){
            if (!userId.equals(senderId)){
                notifyByUser(userId, companyId, type, reference);
            }
        }
    }

    @Transactional
    public void notifyByUser(UUID userId, UUID companyId, NotificationType type, String reference){
        User user = userQuery.byId(companyId, userId);
        Notification notification = new Notification(user, type, reference);
        notificationQuery.save(notification);

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

    public Page<NotificationResponseDTO> getAllByUser(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Notification> notification = notificationQuery.getAllByUser(currentUser.getId(), pageable);

        return notification.map(NotificationResponseDTO::new);
    }
}
