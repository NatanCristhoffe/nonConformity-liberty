package blessed.company.dto;

import blessed.company.enums.PlanType;
import jakarta.validation.constraints.*;

public record CompanyRequestDTO(

        @NotBlank(message = "O nome da companhia é obrigatório.")
        @Size(min = 3, max = 100, message = "O nome da companhia deve conter entre 3 e 100 caracteres.")
        String companyName,

        @NotBlank(message = "O documento é obrigatório.")
        @Size(min = 5, max = 20, message = "O documento deve conter entre 5 e 20 caracteres.")
        String document,

        @NotNull(message = "O tipo de plano deve ser informado.")
        PlanType planType,

        @NotBlank(message = "O telefone é obrigatório.")
        @Pattern(
                regexp = "^\\+?[0-9]{10,15}$",
                message = "O telefone deve conter apenas números e ter entre 10 e 15 dígitos, podendo incluir o código do país."
        )
        String phone,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Informe um endereço de e-mail válido.")
        @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres.")
        String email,

        @NotBlank(message = "O nome da rua é obrigatório.")
        @Size(max = 150, message = "O nome da rua deve ter no máximo 150 caracteres.")
        String street,

        @NotNull(message = "O número da rua é obrigatório.")
        @Positive(message = "O número da rua deve ser um valor positivo.")
        Integer numberStreet,

        @NotBlank(message = "O nome da cidade é obrigatório.")
        @Size(max = 100, message = "O nome da cidade deve ter no máximo 100 caracteres.")
        String city,

        @NotBlank(message = "O estado é obrigatório.")
        @Size(min = 2, max = 2, message = "O estado deve conter a sigla com 2 caracteres (ex: SP, RJ).")
        String state
) {
}
