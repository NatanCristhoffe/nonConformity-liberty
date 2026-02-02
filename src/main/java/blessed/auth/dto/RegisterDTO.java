package blessed.auth.dto;

import blessed.user.enums.UserRole;
import jakarta.validation.constraints.*;

public record RegisterDTO(

        @NotBlank(message = "Nome é obrigatório.")
        @Size(min = 2, max = 40, message = "Nome deve ter entre 2 e 50 caracteres.")
        String firstName,

        @NotBlank(message = "Sobrenome é obrigatório.")
        @Size(min = 2, max = 50, message = "Sobrenome deve ter entre 2 e 50 caracteres.")
        String lastName,

        @NotBlank(message = "E-mail é obrigatório.")
        @Email(message = "E-mail inválido.")
        @Size(max = 100, message = "E-mail deve ter no máximo 100 caracteres.")
        String email,

        @NotBlank(message = "Telefone é obrigatório.")
        @Pattern(
                regexp = "^\\+?[0-9]{10,15}$",
                message = "Telefone deve conter apenas números e ter entre 10 e 15 dígitos."
        )
        String phone,

        @NotBlank(message = "Senha é obrigatória.")
        @Size(min = 8, max = 20, message = "Senha deve ter entre 8 e 20 caracteres.")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*$",
                message = "Senha deve conter ao menos uma letra maiúscula, uma minúscula e um número."
        )
        String password,

        @NotNull(message = "Perfil do usuário é obrigatório.")
        UserRole role,

        @NotNull(message = "Setor é obrigatório.")
        Long sectorId
) {}
