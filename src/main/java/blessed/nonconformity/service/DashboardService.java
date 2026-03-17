package blessed.nonconformity.service;

import blessed.auth.utils.CurrentUser;
import blessed.company.entity.Company;
import blessed.nonconformity.dto.DashboardIndicatorsResponse;
import blessed.nonconformity.dto.SummaryDTO;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.service.query.DashboardQuery;
import org.springframework.cglib.core.Local;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class DashboardService {

    private final DashboardQuery dashboardQuery;
    private final CurrentUser currentUser;

    DashboardService(DashboardQuery dashboardQuery, CurrentUser currentUser){
        this.dashboardQuery = dashboardQuery;
        this.currentUser = currentUser;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public DashboardIndicatorsResponse getIndicators(LocalDateTime startDate, LocalDateTime endDate){
        UUID companyId = currentUser.getCompanyId();
        DashboardIndicatorsResponse response = new DashboardIndicatorsResponse();


        response.setSummary(buildSummary(companyId, startDate, endDate));
        response.setByPriority(dashboardQuery.countByPriority(companyId, startDate, endDate));
        response.setByDepartment(dashboardQuery.countByDepartment(companyId, startDate, endDate));
        response.setTrend(dashboardQuery.trend(companyId, startDate, endDate));
        Double avg = dashboardQuery.averageResolutionDays(companyId, startDate, endDate);

        String formatted = formatAverageDays(avg);

        response.setAverageResolutionDays(formatted);

        return response;
    }

    private SummaryDTO buildSummary(UUID companyId,LocalDateTime startDate, LocalDateTime endDate) {

        Map<NonConformityStatus, Long> byStatus =
                dashboardQuery.countByStatus(companyId, startDate, endDate);

        long total = byStatus.values()
                .stream()
                .mapToLong(Long::longValue)
                .sum();

        SummaryDTO summary = new SummaryDTO();
        summary.setByStatus(byStatus);
        summary.setTotal(total);

        return summary;
    }

    public String formatAverageDays(Double avgDays) {
        if (avgDays == null || avgDays == 0) return "0 dias";

        int days = avgDays.intValue();
        int hours = (int) Math.round((avgDays - days) * 24);

        if (days == 0) {
            return hours + "h";
        }

        if (hours == 0) {
            return days + (days == 1 ? " dia" : " dias");
        }

        return days + (days == 1 ? " dia" : " dias") +
                " e " + hours + "h";
    }

}
