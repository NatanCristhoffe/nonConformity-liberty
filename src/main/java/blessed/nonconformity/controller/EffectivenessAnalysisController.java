package blessed.nonconformity.controller;


import blessed.nonconformity.dto.EffectivenessAnalysisRequestDTO;
import blessed.nonconformity.service.EffectivenessAnalysisService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/effectiveness-analysis")
public class EffectivenessAnalysisController {
    private final EffectivenessAnalysisService service;

    public EffectivenessAnalysisController(EffectivenessAnalysisService service){
        this.service = service;
    }

    @PostMapping("/add/{ncId}")
    public ResponseEntity<Void> addEffectivenessAnalysis(
            @RequestBody @Valid EffectivenessAnalysisRequestDTO data,
            @PathVariable Long ncId
            ){
        service.addEffectivenessAnalysis(ncId, data);

        return ResponseEntity.ok(null);
    }

}
