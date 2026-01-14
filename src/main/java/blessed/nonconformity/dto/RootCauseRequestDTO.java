package blessed.nonconformity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RootCauseRequestDTO(
        @NotBlank(message = "A causa raiz é obrigatória.")
        @Size(min = 10, max = 2000)
        String description
) {
}
