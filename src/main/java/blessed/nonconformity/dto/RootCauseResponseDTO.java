package blessed.nonconformity.dto;

import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.entity.RootCause;

public record RootCauseResponseDTO(
        String description
) {
    public RootCauseResponseDTO(RootCause data){
        this(
            data.getDescription()
        );
    }
}
