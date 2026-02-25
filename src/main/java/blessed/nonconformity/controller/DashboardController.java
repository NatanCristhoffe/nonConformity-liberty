package blessed.nonconformity.controller;

import blessed.nonconformity.dto.DashboardIndicatorsResponse;
import blessed.nonconformity.service.DashboardService;
import blessed.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/dashboard")
@RestController
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service){
        this.service = service;
    }

    @GetMapping("/indicators")
    public ResponseEntity<DashboardIndicatorsResponse> dashboard(
            @AuthenticationPrincipal User user
            ){
        return ResponseEntity.ok(service.getIndicators(user.getCompany().getId()));
    }



}
