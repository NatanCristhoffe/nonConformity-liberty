package blessed.nonconformity.dto;

import blessed.nonconformity.tools.FiveWhyTool;

import java.util.Set;
import java.util.stream.Collectors;

public record FiveWhyToolResponseDTO(
        Long id,
        Boolean completed,
        Set<FiveWhyResponseDTO> whys
) {
    public FiveWhyToolResponseDTO(FiveWhyTool entity) {
        this(
                entity.getId(),
                entity.isCompleted(),
                entity.getFiveWhys()
                        .stream()
                        .map(FiveWhyResponseDTO::new)
                        .collect(Collectors.toSet())
        );
    }
}

