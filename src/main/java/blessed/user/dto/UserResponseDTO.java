package blessed.user.dto;

import blessed.sector.entity.Sector;
import blessed.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserResponseDTO {
    private String id;
    private String fullName;
    private String email;
    private String phone;
    private Sector sector;
    private Instant createdAt;

    public UserResponseDTO(User user){
        this.id = user.getId();
        this.fullName = user.getFirstName() + " " + user.getLastName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.sector = user.getSector();
        this.createdAt = user.getCreatedAt();
    }
}
