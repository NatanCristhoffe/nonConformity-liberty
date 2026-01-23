package blessed.nonconformity.dto;

import blessed.nonconformity.entity.EffectivenessAnalysis;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.enums.NonConformityPriorityLevel;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.enums.QualityToolType;
import blessed.nonconformity.tools.FiveWhyTool;
import blessed.sector.entity.Sector;
import blessed.user.dto.UserResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record NonconformityResponseDTO(

        Long id,
        String title,
        String description,

        Boolean hasAccidentRisk,
        String urlEvidence,

        NonConformityPriorityLevel priorityLevel,
        NonConformityStatus status,

        LocalDateTime dispositionDate,
        LocalDateTime createdAt,

        LinkedRncDTO linkedRnc,

        Sector sourceDepartment,
        Sector responsibleDepartment,

        Boolean requiresQualityTool,
        QualityToolType selectedTool,
        FiveWhyToolResponseDTO fiveWhyToll,
        RootCauseResponseDTO rootCause,

        Set<ActionResponseDTO> actions,
        EffectivenessAnalysisResponseDTO effectivenessAnalysis,

        UserResponseDTO createdByUser,
        UserResponseDTO dispositionOwnerUser,
        UserResponseDTO effectivenessAnalystUser,

        Set<NonconformityLogResponseDTO> logs
) {
    public NonconformityResponseDTO(NonConformity entity) {
        this(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),

                entity.getHasAccidentRisk(),
                entity.getUrlEvidence(),

                entity.getPriorityLevel(),
                entity.getStatus(),

                entity.getDispositionDate(),
                entity.getCreatedAt(),

                entity.getLinkedRnc() != null
                        ? new LinkedRncDTO(entity.getLinkedRnc())
                        : null,

                entity.getSourceDepartment(),
                entity.getResponsibleDepartment(),

                entity.getRequiresQualityTool(),
                entity.getSelectedTool(),

                entity.getFiveWhyTool() != null
                        ? new FiveWhyToolResponseDTO(entity.getFiveWhyTool())
                        : null,

                entity.getRootCause() != null
                        ? new RootCauseResponseDTO(entity.getRootCause())
                        : null,

                entity.getActions() != null
                        ? entity.getActions()
                        .stream()
                        .map(ActionResponseDTO::new)
                        .collect(Collectors.toSet())
                        : Set.of(),

                entity.getEffectivenessAnalysis() != null
                        ? new EffectivenessAnalysisResponseDTO(entity.getEffectivenessAnalysis())
                        : null,

                entity.getCreatedBy() != null
                        ? new UserResponseDTO(entity.getCreatedBy())
                        : null,

                entity.getDispositionOwner() != null
                        ? new UserResponseDTO(entity.getDispositionOwner())
                        : null,

                entity.getEffectivenessAnalyst() != null
                        ? new UserResponseDTO(entity.getEffectivenessAnalyst())
                        : null,

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

