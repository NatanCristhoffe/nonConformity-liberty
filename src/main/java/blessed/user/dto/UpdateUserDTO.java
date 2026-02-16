package blessed.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record UpdateUserDTO(
        @NotBlank(message = "O primeiro nome é obrigatório")
        @Size(min = 2, max = 50)
        @Pattern(
                regexp = "^[\\p{L}]+( [\\p{L}]+)?$",
                message = "Por favor, informe apenas o primeiro nome"
        )
        String firstName,

        @NotBlank
        @Size(min = 2, max = 50)
        @Pattern(
                regexp = "^[\\p{L} '\\-]+$",
                message = "O sobrenome é obrigatório."
        )
        String lastName,

        @NotBlank(message = "Telefone é obrigatório.")
        @Size(min = 10, max = 15)
        @Pattern(
                regexp = "\\d{10,15}",
                message = "Telefone deve conter apenas números (10 a 15 dígitos)"
        )
        String phone,

        @NotNull(message = "Setor é obrigatório.")
        Long sectorId,

        @Size(min = 8, max = 20, message = "Senha deve ter entre 8 e 20 caracteres.")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*$",
                message = "Senha deve conter ao menos uma letra maiúscula, uma minúscula e um número."
        )
        String oldPassword,

        @Size(min = 8, max = 20, message = "Senha deve ter entre 8 e 20 caracteres.")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*$",
                message = "Senha deve conter ao menos uma letra maiúscula, uma minúscula e um número."
        )
        String newPassword

) {
}
