package blessed.nonconformity.dto;

public record TrendDTO(
        String month,
        Long opened,
        Long closed
) {}