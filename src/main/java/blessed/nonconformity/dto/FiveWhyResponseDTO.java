package blessed.nonconformity.dto;

import blessed.nonconformity.entity.FiveWhy;

public record FiveWhyResponseDTO(
        Long id,
        Integer level,
        String question,
        String answer
) {
    public FiveWhyResponseDTO(FiveWhy entity) {
        this(
                entity.getId(),
                entity.getLevel(),
                entity.getQuestion(),
                entity.getAnswer()
        );
    }
}

