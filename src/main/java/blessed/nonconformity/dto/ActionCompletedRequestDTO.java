package blessed.nonconformity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ActionCompletedRequestDTO(
        @NotBlank(message = "Você deve preencher o campo de observação.")
        @Size(min = 5, max = 500)
        String observation,
        String evidenceUrl
){
}
