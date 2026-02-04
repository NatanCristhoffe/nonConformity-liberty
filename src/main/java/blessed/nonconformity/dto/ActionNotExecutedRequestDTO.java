package blessed.nonconformity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ActionNotExecutedRequestDTO(
        @NotBlank(message = "Você deve informar o motivo da não realização da ação.")
        @Size(min = 5, max = 500)
        String nonExecutionReason
) {
}
