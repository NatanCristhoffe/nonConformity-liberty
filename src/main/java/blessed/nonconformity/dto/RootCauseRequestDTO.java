package blessed.nonconformity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record RootCauseRequestDTO(
        @NotBlank(message = "A causa raiz é obrigatória.")
        @Size(min = 10, max = 2000)
        String description,

        @NotNull(message = "O ID do usuário criador da causa raiz é obrigatório.")
        UUID createById
) {
}
