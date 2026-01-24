package blessed.nonconformity.controller;

import blessed.nonconformity.dto.FiveWhyAnswerRequestDTO;
import blessed.nonconformity.dto.FiveWhyRequestDTO;
import blessed.nonconformity.service.FiveWhyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quality-tools/five-whys")
public class FiveWhyController {
    FiveWhyService service;

    public FiveWhyController(FiveWhyService service) {
        this.service = service;
    }


    @PutMapping("/nonconformities/{nonConformityId}/five-whys/{fiveWhyId}/answer")
    public ResponseEntity<Void> addAnswer(
            @PathVariable Long nonConformityId,
            @PathVariable Long fiveWhyId,
            @RequestBody @Valid FiveWhyAnswerRequestDTO data
            ){
        service.addAnswer(nonConformityId, fiveWhyId, data);
        return ResponseEntity.noContent().build();
    }
}
