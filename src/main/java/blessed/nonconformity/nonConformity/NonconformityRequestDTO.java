package blessed.nonconformity.nonConformity;

import java.time.LocalDateTime;

public record NonconformityRequestDTO(
        String title,
        String description,
        Boolean hasAccidentRisk,
        NonConformityPriorityLevel priorityLevel,
        LocalDateTime dispositionDate,
        String urlEvidence,
        Long linkedRncId,
        Long sourceDepartmentId,
        Long responsibleDepartmentId
) {
}