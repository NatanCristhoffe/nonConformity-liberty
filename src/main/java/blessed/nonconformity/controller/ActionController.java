package blessed.nonconformity.controller;

import blessed.nonconformity.dto.*;
import blessed.nonconformity.entity.Action;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.service.ActionService;
import blessed.user.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
            @RequestBody @Valid ActionRequestDTO actionUser){
        Action action = service.create(nc, actionUser);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ActionResponseDTO(action));
    }

    @PutMapping("/{actionId}/completed")
    public ResponseEntity<ActionResponseDTO> completedAction(
            @PathVariable Long actionId,
            @RequestBody @Valid ActionCompletedRequestDTO data,
            @AuthenticationPrincipal User user
            ){
        ActionResponseDTO actionCompleted = service.completedAction(actionId, data, user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actionCompleted);

    }

    @PutMapping("/{actionId}/not-executed")
    public ResponseEntity<ActionResponseDTO> notExecutedAction(
            @PathVariable Long actionId,
            @RequestBody @Valid ActionNotExecutedRequestDTO data,
            Authentication authentication
    ){
        User userRequest = (User) authentication.getPrincipal();
        Action actionCompleted = service.notExecutedAction(actionId, userRequest, data);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ActionResponseDTO(actionCompleted));

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
