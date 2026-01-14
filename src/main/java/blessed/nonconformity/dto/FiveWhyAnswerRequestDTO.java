package blessed.nonconformity.dto;

import jakarta.validation.constraints.NotBlank;

public record FiveWhyAnswerRequestDTO(
        @NotBlank
        String answer
    ){}
