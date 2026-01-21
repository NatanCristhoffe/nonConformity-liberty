package blessed.auth.dto;

import blessed.sector.entity.Sector;
import blessed.user.enums.UserRole;

public record LoginResponseDTO(
        String token,
        String email,
        String name,
        UserRole role,
        Sector sector,
        Boolean isActivated
) {}

