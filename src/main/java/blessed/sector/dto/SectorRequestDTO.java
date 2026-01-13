package blessed.sector.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SectorRequestDTO(
        @NotBlank(message = "O nome do setor é obrigatório.")
        @Size(min = 4, max = 50)
        String name,
        @NotBlank(message = "O setor deve ter uma descrição.")
        @Size(min = 4, max = 200)
        String description
) {
}
