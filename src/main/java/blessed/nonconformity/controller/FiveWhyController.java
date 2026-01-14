package blessed.nonconformity.controller;

import blessed.nonconformity.dto.FiveWhyRequestDTO;
import blessed.nonconformity.service.FiveWhyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quality-tools/five-whys")
public class FiveWhyController {
    private final FiveWhyService service;

    public FiveWhyController(FiveWhyService service) {
        this.service = service;
    }

    @PostMapping("/{nonConformityId}")
    public ResponseEntity<Void> addWhy(
            @PathVariable Long nonConformityId,
            @RequestBody @Valid FiveWhyRequestDTO data
            ){
        service.addWhy(nonConformityId, data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
