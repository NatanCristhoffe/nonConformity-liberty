package blessed.user.dto;

import blessed.sector.entity.Sector;
import blessed.user.entity.User;
import blessed.user.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class UserResponseDTO {
    private UUID id;
    private String fullName;
    private String email;
    private String phone;
    private Sector sector;
    private UserRole role;
    private boolean enable;
    private LocalDateTime createdAt;

    public UserResponseDTO(User user){
        this.id = user.getId();
        this.fullName = user.getFirstName() + " " + user.getLastName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.sector = user.getSector();
        this.role = user.getRole();
        this.enable = user.isEnabled();
        this.createdAt = user.getCreatedAt();
    }
}
