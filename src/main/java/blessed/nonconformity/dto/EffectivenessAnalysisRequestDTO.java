package blessed.nonconformity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record EffectivenessAnalysisRequestDTO(

        @NotNull(message = "Você deve informar se a não conformidade foi eficaz.")
        Boolean effective,

        @NotBlank(message = "A descrição de eficacia é obrigatória.")
        @Size(min = 2, max = 5000)
        String effectivenessDescription
) {
}
