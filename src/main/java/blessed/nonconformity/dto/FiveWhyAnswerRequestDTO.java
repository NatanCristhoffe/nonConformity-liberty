package blessed.nonconformity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FiveWhyAnswerRequestDTO(
        @NotBlank
        @Size(min = 10, max = 500)
        String answer
    ){}
