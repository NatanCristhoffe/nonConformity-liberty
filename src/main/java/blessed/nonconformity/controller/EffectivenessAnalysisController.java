package blessed.nonconformity.controller;


import blessed.nonconformity.dto.EffectivenessAnalysisRequestDTO;
import blessed.nonconformity.service.EffectivenessAnalysisService;
import blessed.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/effectiveness-analysis")
public class EffectivenessAnalysisController {
    private final EffectivenessAnalysisService service;

    public EffectivenessAnalysisController(EffectivenessAnalysisService service){
        this.service = service;
    }

    @PostMapping("/add/{ncId}")
    public ResponseEntity<Map<String, String>> addEffectivenessAnalysis(
            @RequestBody @Valid EffectivenessAnalysisRequestDTO data,
            @PathVariable Long ncId,
            Authentication authentication
            ){
        User user = (User) authentication.getPrincipal();
        service.addEffectivenessAnalysis(ncId, data, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Análise de eficácia criada com sucesso"));
    }

}
