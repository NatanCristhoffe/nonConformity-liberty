package blessed.nonconformity.controller;

import blessed.nonconformity.dto.ActionCompletedRequestDTO;
import blessed.nonconformity.dto.ActionNotExecutedRequestDTO;
import blessed.nonconformity.dto.ActionRequestDTO;
import blessed.nonconformity.dto.ActionResponseDTO;
import blessed.nonconformity.entity.Action;
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

    @PutMapping("/competed/{actionId}")
    public ResponseEntity<ActionResponseDTO> completedAction(
            @PathVariable Long actionId,
            @RequestBody @Valid ActionCompletedRequestDTO data
            ){

        Action actionCompleted = service.completedAction(actionId, data);

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

}
