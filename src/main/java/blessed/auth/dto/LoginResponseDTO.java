package blessed.auth.dto;

import blessed.sector.dto.SectorResponseDTO;
import blessed.sector.entity.Sector;
import blessed.user.entity.User;
import blessed.user.enums.UserRole;

public record LoginResponseDTO(
        String token,
        String email,
        String name,
        UserRole role,
        SectorResponseDTO sector,
        Boolean enable
) {
    public LoginResponseDTO(String token, User user){
        this(
                token,
                user.getEmail(),
                user.getFirstName() + " " + user.getLastName(),
                user.getRole(),
                user.getSector() != null
                    ? new SectorResponseDTO(user.getSector())
                    : null,
                user.isEnabled()
        );
    }

}

