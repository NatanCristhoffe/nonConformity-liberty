package blessed.application.dto;

public record AdminOnboardingRequestDTO(
        String firstName,
        String lastName,
        String email,
        String phone,
        String password
) {
}
