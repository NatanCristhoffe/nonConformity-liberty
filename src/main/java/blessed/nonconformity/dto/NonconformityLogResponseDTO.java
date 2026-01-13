package blessed.nonconformity.dto;

import blessed.nonconformity.entity.NonconformityLog;

import java.time.Instant;

public record NonconformityLogResponseDTO(
        Long id,
        String message,
        Instant createdAt
) {
    public NonconformityLogResponseDTO(NonconformityLog log) {
        this(
                log.getId(),
                log.getMessage(),
                log.getCreatedAt()
        );
    }
}
