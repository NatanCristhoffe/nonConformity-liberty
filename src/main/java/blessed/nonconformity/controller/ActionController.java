package blessed.nonconformity.controller;

import blessed.nonconformity.dto.*;
import blessed.nonconformity.entity.Action;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.service.ActionService;
import blessed.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/action")
public class ActionController {

    private final ActionService service;
    public ActionController(ActionService service){
        this.service = service;
    }

    @PostMapping("/add/{nc}")
    public ResponseEntity<ActionResponseDTO> addAction(
            @PathVariable Long nc,
            @RequestBody @Valid ActionRequestDTO actionUser,
            @AuthenticationPrincipal User user
            ){
        Action action = service.create(nc, actionUser, user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ActionResponseDTO(action));
    }


    @PutMapping(value = "/{actionId}/complete", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ActionResponseDTO> completeAction(
            @PathVariable Long actionId,
            @RequestPart("data") @Valid ActionCompletedRequestDTO data,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(service.completeAction(actionId, data, user, file));
    }

    @PutMapping(value = "/{actionId}/not-executed", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ActionResponseDTO> notExecutedAction(
            @PathVariable Long actionId,
            @RequestPart("data") @Valid ActionNotExecutedRequestDTO data,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(service.markAsNotExecuted(actionId, data, user, file));
    }

    @PutMapping("/closed/{ncId}")
    public ResponseEntity<NonconformityResponseDTO> closedActions(
            @PathVariable Long ncId,
            Authentication authentication
        ){
        User userRequest = (User) authentication.getPrincipal();
        NonConformity nc = service.closeActionStage(ncId, userRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new NonconformityResponseDTO(nc));
    }
}
