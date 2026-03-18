package blessed.notification.controller;

import blessed.notification.dto.NotificationResponseDTO;
import blessed.notification.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService service;

    NotificationController(NotificationService service){
        this.service = service;
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Map<String, String>> markAsRead(@PathVariable UUID id){
        service.markAsRead(id);
        return ResponseEntity.ok(Map.of("success", "notificação lida com sucesso!"));
    }

    @GetMapping()
    public ResponseEntity<Page<NotificationResponseDTO>> getNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(service.getAllByUser(page, size));
    }
}
