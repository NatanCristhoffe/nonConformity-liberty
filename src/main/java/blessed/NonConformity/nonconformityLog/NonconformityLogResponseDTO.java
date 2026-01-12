package blessed.NonConformity.nonconformityLog;

import blessed.NonConformity.nonConformity.NonConformityStatus;

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
