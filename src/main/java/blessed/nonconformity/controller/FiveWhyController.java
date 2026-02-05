package blessed.nonconformity.controller;

import blessed.nonconformity.dto.FiveWhyAnswerRequestDTO;
import blessed.nonconformity.dto.FiveWhyRequestDTO;
import blessed.nonconformity.service.FiveWhyService;
import blessed.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quality-tools/five-whys")
public class FiveWhyController {
    FiveWhyService service;

    public FiveWhyController(FiveWhyService service) {
        this.service = service;
    }


    @PutMapping("/nonconformities/{nonconformityId}/five-whys/{fiveWhyId}/answer")
    public ResponseEntity<Void> addAnswer(
            @PathVariable Long nonconformityId,
            @PathVariable Long fiveWhyId,
            @RequestBody @Valid FiveWhyAnswerRequestDTO data,
            @AuthenticationPrincipal User user
            ){
        service.addAnswer(nonconformityId, fiveWhyId, data, user);
        return ResponseEntity.noContent().build();
    }
}
