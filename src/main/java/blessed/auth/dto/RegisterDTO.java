package blessed.auth.dto;

import blessed.user.enums.UserRole;

public record RegisterDTO(
        String firstName, String lastName, String email,
        String phone, String password, UserRole role, Long sectorId) {
}