package blessed.nonconformity.dto;

import blessed.nonconformity.enums.NonConformityPriorityLevel;
import blessed.nonconformity.enums.NonConformityStatus;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@JsonPropertyOrder({
        "summary",
        "byPriority",
        "byDepartment",
        "trend",
        "averageResolutionDays",
        "effectivenessRate",
        "withAccidentRisk"
})
@Getter
@Setter
public class DashboardIndicatorsResponse {

    private SummaryDTO summary;
    private Map<NonConformityPriorityLevel, Long> byPriority;
    private List<DepartmentIndicatorDTO> byDepartment;
    private List<TrendDTO> trend;
    private Double averageResolutionDays;
    private Double effectivenessRate;
    private Long withAccidentRisk;
}