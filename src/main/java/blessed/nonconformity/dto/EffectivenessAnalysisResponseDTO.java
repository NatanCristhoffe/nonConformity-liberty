package blessed.nonconformity.dto;

import blessed.nonconformity.entity.EffectivenessAnalysis;

import java.time.LocalDateTime;

public record EffectivenessAnalysisResponseDTO(
        Long id,
        Boolean effective,
        String effectivenessDescription,
        LocalDateTime analyzedAt,
        Long analyzedByUserId
) {
    public EffectivenessAnalysisResponseDTO(EffectivenessAnalysis entity) {
        this(
                entity.getId(),
                entity.getEffective(),
                entity.getEffectivenessDescription(),
                entity.getAnalyzedAt(),
                entity.getAnalyzedByUser().getId()
        );
    }
}
