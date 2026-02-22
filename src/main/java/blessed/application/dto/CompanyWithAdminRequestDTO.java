package blessed.application.dto;

import blessed.auth.dto.RegisterDTO;
import blessed.company.dto.CompanyRequestDTO;
import blessed.sector.dto.SectorRequestDTO;
import blessed.user.dto.UserRequestDTO;
import lombok.Setter;

public record CompanyWithAdminRequestDTO(
        CompanyRequestDTO company,
        SectorRequestDTO sector,
        AdminOnboardingRequestDTO admin

) {
}
