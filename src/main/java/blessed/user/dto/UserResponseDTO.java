package blessed.user.dto;

import blessed.nonconformity.sectors.Sector;
import blessed.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private Sector sector;

    public UserResponseDTO(User user){
        this.id = user.getId();
        this.fullName = user.getFirstName() + " " + user.getLastName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.sector = user.getSector();
    }
}
