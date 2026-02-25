package blessed.nonconformity.service.query;

import blessed.nonconformity.dto.DepartmentIndicatorDTO;
import blessed.nonconformity.dto.TrendDTO;
import blessed.nonconformity.enums.NonConformityPriorityLevel;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.repository.NonconformityRepository;
import org.springframework.stereotype.Service;

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


    public Map<NonConformityStatus, Long> countByStatus(UUID companyId){
        return nonconformityRepository.countByStatus(companyId)
                .stream()
                .collect(Collectors.toMap(
                        r -> (NonConformityStatus) r[0],
                        r -> (Long) r[1]
                ));
    }

    public Map<NonConformityPriorityLevel, Long> countByPriority(UUID companyId){
        return nonconformityRepository.countByPriority(companyId)
                .stream()
                .collect(Collectors.toMap(
                        r -> (NonConformityPriorityLevel) r[0],
                        r -> (Long) r[1]
                ));
    }

    public List<DepartmentIndicatorDTO> countByDepartment(UUID companyId){
        return nonconformityRepository.countByDepartment(companyId)
                .stream()
                .map(r -> new DepartmentIndicatorDTO(
                        (Long) r[0],
                        (String) r[1],
                        (Long) r[2]
                ))
                .toList();
    }

    public List<TrendDTO> trend(UUID companyId) {
        return nonconformityRepository.trend(companyId)
                .stream()
                .map(r -> new TrendDTO(
                        (String) r[0],
                        (Long) r[1],
                        (Long) r[2]
                ))
                .toList();
    }


    public Double averageResolutionDays(UUID companyId) {
        return Optional.ofNullable(nonconformityRepository.averageResolutionDays(companyId))
                .orElse(0.0);
    }





}
