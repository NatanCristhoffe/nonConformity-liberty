package blessed.nonconformity.service;

import blessed.nonconformity.dto.DashboardIndicatorsResponse;
import blessed.nonconformity.dto.SummaryDTO;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.service.query.DashboardQuery;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DashboardService {

    private final DashboardQuery dashboardQuery;

    DashboardService(DashboardQuery dashboardQuery){
        this.dashboardQuery = dashboardQuery;
    }

    public DashboardIndicatorsResponse getIndicators(){
        DashboardIndicatorsResponse response = new DashboardIndicatorsResponse();

        response.setSummary(buildSummary());
        response.setByPriority(dashboardQuery.countByPriority());
        response.setByDepartment(dashboardQuery.countByDepartment());
        response.setTrend(dashboardQuery.trend());
        response.setAverageResolutionDays(dashboardQuery.averageResolutionDays());

        return response;
    }

    private SummaryDTO buildSummary() {

        Map<NonConformityStatus, Long> byStatus =
                dashboardQuery.countByStatus();

        long total = byStatus.values()
                .stream()
                .mapToLong(Long::longValue)
                .sum();

        SummaryDTO summary = new SummaryDTO();
        summary.setByStatus(byStatus);
        summary.setTotal(total);

        return summary;
    }


}
