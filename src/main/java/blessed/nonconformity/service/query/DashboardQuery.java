package blessed.nonconformity.service.query;

import blessed.nonconformity.dto.DepartmentIndicatorDTO;
import blessed.nonconformity.dto.TrendDTO;
import blessed.nonconformity.enums.NonConformityPriorityLevel;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.repository.NonconformityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DashboardQuery {
    private final NonconformityRepository nonconformityRepository;

    DashboardQuery(NonconformityRepository nonconformityRepository){
        this.nonconformityRepository = nonconformityRepository;
    }


    public Map<NonConformityStatus, Long> countByStatus(UUID companyId, LocalDateTime startDate, LocalDateTime endDate){
        return nonconformityRepository.countByStatus(companyId, startDate, endDate)
                .stream()
                .collect(Collectors.toMap(
                        r -> (NonConformityStatus) r[0],
                        r -> (Long) r[1]
                ));
    }

    public Map<NonConformityPriorityLevel, Long> countByPriority(UUID companyId, LocalDateTime startDate, LocalDateTime endDate){
        return nonconformityRepository.countByPriority(companyId, startDate, endDate)
                .stream()
                .collect(Collectors.toMap(
                        r -> (NonConformityPriorityLevel) r[0],
                        r -> (Long) r[1]
                ));
    }

    public List<DepartmentIndicatorDTO> countByDepartment(UUID companyId, LocalDateTime startDate, LocalDateTime endDate){
        return nonconformityRepository.countByDepartment(companyId, startDate, endDate)
                .stream()
                .map(r -> new DepartmentIndicatorDTO(
                        (Long) r[0],
                        (String) r[1],
                        (Long) r[2]
                ))
                .toList();
    }

    public List<TrendDTO> trend(UUID companyId, LocalDateTime startDate, LocalDateTime endDate) {
        return nonconformityRepository.trend(companyId, startDate, endDate)
                .stream()
                .map(r -> new TrendDTO(
                        (String) r[0],
                        (Long) r[1],
                        (Long) r[2]
                ))
                .toList();
    }


    public Double averageResolutionDays(UUID companyId, LocalDateTime startDate, LocalDateTime endDate) {
        return Optional.ofNullable(nonconformityRepository.averageResolutionDays(companyId, startDate, endDate))
                .orElse(0.0);
    }





}
