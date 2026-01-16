package blessed.nonconformity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EffectivenessAnalysisRequestDTO(

        @NotNull(message = "Você deve informar se a não conformidade foi eficaz.")
        Boolean effective,

        @NotBlank(message = "A descrição de eficacia é obrigatória.")
        @Size(min = 2, max = 1000)
        String effectivenessDescription,

        @NotNull(message = "O ID do usuário que está realizando a analise de eficacia deve ser informado")
        Long analyzedById
) {
}
