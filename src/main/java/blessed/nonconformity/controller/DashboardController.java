package blessed.nonconformity.controller;

import blessed.nonconformity.dto.DashboardFilter;
import blessed.nonconformity.dto.DashboardIndicatorsResponse;
import blessed.nonconformity.service.DashboardService;
import blessed.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequestMapping("/dashboard")
@RestController
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service){
        this.service = service;
    }

    @GetMapping("/indicators")
    public ResponseEntity<DashboardIndicatorsResponse> dashboard(
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate
    ){
        return ResponseEntity.ok(service.getIndicators(startDate, endDate));
    }
    }
