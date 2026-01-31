package blessed.nonconformity.controller;

import blessed.nonconformity.dto.RootCauseRequestDTO;
import blessed.nonconformity.dto.RootCauseResponseDTO;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.entity.RootCause;
import blessed.nonconformity.service.RootCauseService;
import blessed.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/root-cause")
public class RootCauseController {

    private final RootCauseService service;
    public RootCauseController(RootCauseService service){
        this.service = service;
    }
    @PostMapping("/{ncId}")
    public ResponseEntity<RootCauseResponseDTO> addRootCauseNc(
            @PathVariable Long ncId,
            @RequestBody @Valid RootCauseRequestDTO data,
            @AuthenticationPrincipal User user
            ){
        RootCause rootCause = service.create(ncId, data, user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RootCauseResponseDTO(rootCause));
    }
}
