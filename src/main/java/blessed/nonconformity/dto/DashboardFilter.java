package blessed.nonconformity.dto;

import java.time.LocalDateTime;

public record DashboardFilter(
        LocalDateTime startDate,
        LocalDateTime endDate
) {}