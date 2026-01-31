package blessed.nonconformity.controller;

import blessed.nonconformity.dto.DashboardIndicatorsResponse;
import blessed.nonconformity.service.DashboardService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<DashboardIndicatorsResponse> dashboard(){
        return ResponseEntity.ok(service.getIndicators());
    }



}
