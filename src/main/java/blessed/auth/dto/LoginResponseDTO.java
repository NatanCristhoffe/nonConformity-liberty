package blessed.auth.dto;

import blessed.company.dto.CompanyResponseDTO;
import blessed.sector.dto.SectorResponseDTO;
import blessed.sector.entity.Sector;
import blessed.user.entity.User;
import blessed.user.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.UUID;

@JsonFormat(

)
public record LoginResponseDTO(
        String token,
        UUID id,
        String name,
        String email,
        String phone,
        UserRole role,
        SectorResponseDTO sector,
        CompanyResponseDTO company,
        Boolean enable
) {
    public LoginResponseDTO(String token, User user){
        this(
                token,
                user.getId(),
                user.getFirstName() + " " + user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getSector() != null
                    ? new SectorResponseDTO(user.getSector())
                    : null,
                user.getCompany() != null
                    ? new CompanyResponseDTO(user.getCompany())
                    : null,
                user.isEnabled()
        );
    }

}

