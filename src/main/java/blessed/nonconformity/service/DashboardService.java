package blessed.nonconformity.service;

import blessed.company.entity.Company;
import blessed.nonconformity.dto.DashboardIndicatorsResponse;
import blessed.nonconformity.dto.SummaryDTO;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.service.query.DashboardQuery;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class DashboardService {

    private final DashboardQuery dashboardQuery;

    DashboardService(DashboardQuery dashboardQuery){
        this.dashboardQuery = dashboardQuery;
    }

    public DashboardIndicatorsResponse getIndicators(UUID companyId){
        DashboardIndicatorsResponse response = new DashboardIndicatorsResponse();

        response.setSummary(buildSummary(companyId));
        response.setByPriority(dashboardQuery.countByPriority(companyId));
        response.setByDepartment(dashboardQuery.countByDepartment(companyId));
        response.setTrend(dashboardQuery.trend(companyId));
        response.setAverageResolutionDays(dashboardQuery.averageResolutionDays(companyId));

        return response;
    }

    private SummaryDTO buildSummary(UUID companyId) {

        Map<NonConformityStatus, Long> byStatus =
                dashboardQuery.countByStatus(companyId);

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
