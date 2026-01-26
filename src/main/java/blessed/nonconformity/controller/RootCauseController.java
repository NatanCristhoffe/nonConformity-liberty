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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/root-cause")
public class RootCauseController {

    private final RootCauseService service;
    public RootCauseController(RootCauseService service){
        this.service = service;
    }

    @PostMapping(params = "ncId")
    public ResponseEntity<RootCauseResponseDTO> addRootCauseNc(
            @RequestParam Long ncId,
            @RequestBody @Valid RootCauseRequestDTO data,
            Authentication authentication
            ){
        User user = (User) authentication.getPrincipal();
        RootCause rootCause = service.create(ncId, data, user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RootCauseResponseDTO(rootCause));
    }
}
