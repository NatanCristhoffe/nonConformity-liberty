package blessed.user.dto;

import blessed.nonconformity.sectors.Sector;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {
    @NotBlank
    @Size(min = 2, max = 50)
    @Pattern(
            regexp = "^[\\p{L}]+( [\\p{L}]+)?$",
            message = "Por favor, informe apenas o primeiro nome"
    )
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    @Pattern(
            regexp = "^[\\p{L} '\\-]+$",
            message = "Sobrenome contém caracteres inválidos"
    )
    private String lastName;

    @NotBlank
    @Email
    @Size(max = 200)
    private String email;

    @NotBlank
    @Size(min = 10, max = 15)
    @Pattern(
            regexp = "\\d{10,15}",
            message = "Telefone deve conter apenas números (10 a 15 dígitos)"
    )
    private String phone;

    @NotBlank
    private Long sectorId;
}
