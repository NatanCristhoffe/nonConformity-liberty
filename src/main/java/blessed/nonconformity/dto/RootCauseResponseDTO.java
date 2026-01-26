package blessed.nonconformity.dto;

import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.entity.RootCause;
import blessed.user.dto.UserResponseDTO;

public record RootCauseResponseDTO(
        String description,
        UserResponseDTO createBy
) {
    public RootCauseResponseDTO(RootCause data){
        this(
            data.getDescription(),
            data.getUserCreated() != null
                ? new UserResponseDTO(data.getUserCreated())
                : null
        );
    }
}
