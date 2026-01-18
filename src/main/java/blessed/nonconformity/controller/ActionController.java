package blessed.nonconformity.controller;

import blessed.nonconformity.dto.*;
import blessed.nonconformity.entity.Action;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.service.ActionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/action")
public class ActionController {

    private final ActionService service;
    public ActionController(ActionService service){
        this.service = service;
    }

    @PostMapping("/add/{ncId}")
    public ResponseEntity<ActionResponseDTO> addAction(
            @PathVariable Long ncId,
            @RequestBody @Valid ActionRequestDTO actionUser){
        Action action = service.create(ncId, actionUser);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ActionResponseDTO(action));
    }

    @PutMapping("/competed/{actionId}/user/{completedById}")
    public ResponseEntity<ActionResponseDTO> completedAction(
            @PathVariable Long actionId,
            @PathVariable Long completedById,
            @RequestBody @Valid ActionCompletedRequestDTO data
            ){

        Action actionCompleted = service.completedAction(actionId, data, completedById);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ActionResponseDTO(actionCompleted));

    }

    @PutMapping("/not-executed/{notExecutedId}")
    public ResponseEntity<ActionResponseDTO> notExecutedAction(
            @PathVariable Long notExecutedId,
            @RequestBody @Valid ActionNotExecutedRequestDTO data
    ){

        Action actionCompleted = service.notExecutedAction(notExecutedId, data);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ActionResponseDTO(actionCompleted));

    }

    @PutMapping("/closed/{ncId}")
    public ResponseEntity<NonconformityResponseDTO> closedActions(@PathVariable Long ncId){
        NonConformity nc = service.closeActionStage(ncId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new NonconformityResponseDTO(nc));
    }
}
