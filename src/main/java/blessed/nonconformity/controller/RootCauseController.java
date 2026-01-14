package blessed.nonconformity.controller;

import blessed.nonconformity.dto.RootCauseRequestDTO;
import blessed.nonconformity.dto.RootCauseResponseDTO;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.entity.RootCause;
import blessed.nonconformity.service.RootCauseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/root-cause")
public class RootCauseController {

    private final RootCauseService service;
    public RootCauseController(RootCauseService service){
        this.service = service;
    }

    @PostMapping("/nonconformity/{ncId}")
    public ResponseEntity<RootCauseResponseDTO> addRootCauseNc(
            @PathVariable Long ncId,
            @RequestBody @Valid RootCauseRequestDTO data
            ){
        RootCause rootCause = service.create(ncId, data);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RootCauseResponseDTO(rootCause));
    }
}
