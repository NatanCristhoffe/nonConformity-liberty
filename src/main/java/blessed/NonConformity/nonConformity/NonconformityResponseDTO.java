package blessed.NonConformity.nonConformity;

import blessed.NonConformity.nonconformityLog.NonconformityLog;
import blessed.NonConformity.nonconformityLog.NonconformityLogResponseDTO;
import blessed.NonConformity.sectors.Sector;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record NonconformityResponseDTO(
        Long id,
        String title,
        NonConformityStatus status,
        Set<NonconformityLogResponseDTO> logs,
        Sector sourceDepartment,
        Sector responsibleDepartment

) {
    public NonconformityResponseDTO(NonConformity entity) {
        this(
                entity.getId(),
                entity.getTitle(),
                entity.getStatus(),
                entity.getLogs()
                        .stream()
                        .map(NonconformityLogResponseDTO::new)
                        .collect(Collectors.toSet()),
                entity.getSourceDepartment(),
                entity.getResponsibleDepartment()
        );
    }
}

