package blessed.nonconformity.nonConformity;

import blessed.nonconformity.nonconformityLog.NonconformityLogResponseDTO;
import blessed.nonconformity.sectors.Sector;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record NonconformityResponseDTO(
        Long id,
        String title,
        String description,
        Boolean hasAccidentRisk,
        NonConformityPriorityLevel priorityLevel,
        LocalDateTime dispositionDate,
        NonConformityStatus status,
        String urlEvidence,
        LinkedRncDTO linkedRnc,
        Sector sourceDepartment,
        Sector responsibleDepartment,
        Set<NonconformityLogResponseDTO> logs
) {

    public NonconformityResponseDTO(NonConformity entity) {
        this(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getHasAccidentRisk(),
                entity.getPriorityLevel(),
                entity.getDispositionDate(),
                entity.getStatus(),
                entity.getUrlEvidence(),
                entity.getLinkedRnc() != null ? new LinkedRncDTO(entity.getLinkedRnc()) : null,
                entity.getSourceDepartment(),
                entity.getResponsibleDepartment(),
                entity.getLogs()
                        .stream()
                        .map(NonconformityLogResponseDTO::new)
                        .collect(Collectors.toSet())
        );
    }

    public record LinkedRncDTO(Long id, String title) {
        public LinkedRncDTO(NonConformity linkedRnc) {
            this(linkedRnc.getId(), linkedRnc.getTitle());
        }
    }
}

