package blessed.nonconformity.dto;


import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.enums.NonConformityPriorityLevel;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.enums.QualityToolType;
import blessed.sector.dto.SectorResponseDTO;
import blessed.sector.entity.Sector;
import blessed.user.dto.UserResponseDTO;

import java.time.LocalDateTime;
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
        LocalDateTime createdAt,

        LocalDateTime dispositionDate,
        LocalDateTime dispositionClosedAt,

        LinkedRncDTO linkedRnc,

        SectorResponseDTO sourceDepartment,
        SectorResponseDTO responsibleDepartment,

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
        this(entity, false, null, null);
    }

    public NonconformityResponseDTO(
            NonConformity entity, boolean includeAll,String presignedUrlEvidence,
            Set<ActionResponseDTO> signedActions
    ) {
        this(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),

                entity.getHasAccidentRisk(),
                presignedUrlEvidence != null ? presignedUrlEvidence : entity.getUrlEvidence(),

                entity.getPriorityLevel(),
                entity.getStatus(),

                entity.getDispositionDate(),
                entity.getDispositionClosedAt(),
                entity.getCreatedAt(),

                entity.getLinkedRnc() != null
                        ? new LinkedRncDTO(entity.getLinkedRnc())
                        : null,

                new SectorResponseDTO(entity.getSourceDepartment()),
                new SectorResponseDTO(entity.getResponsibleDepartment()),

                entity.getRequiresQualityTool(),
                entity.getSelectedTool(),

                includeAll && entity.getFiveWhyTool() != null
                        ? new FiveWhyToolResponseDTO(entity.getFiveWhyTool())
                        : null,

                includeAll && entity.getRootCause() != null
                        ? new RootCauseResponseDTO(entity.getRootCause())
                        : null,

                signedActions != null ? signedActions : (
                        includeAll && entity.getActions() != null
                                ? entity.getActions().stream().map(ActionResponseDTO::new).collect(Collectors.toSet())
                                : Set.of()
                ),

                includeAll && entity.getEffectivenessAnalysis() != null
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

                includeAll
                        ? entity.getLogs()
                        .stream()
                        .map(NonconformityLogResponseDTO::new)
                        .collect(Collectors.toSet())
                        : Set.of()
        );
    }

    public record LinkedRncDTO(Long id, String title) {
        public LinkedRncDTO(NonConformity linkedRnc) {
            this(linkedRnc.getId(), linkedRnc.getTitle());
        }
    }
}
