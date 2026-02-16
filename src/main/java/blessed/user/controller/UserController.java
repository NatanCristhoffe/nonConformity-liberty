package blessed.user.controller;

import blessed.auth.dto.RegisterDTO;
import blessed.user.dto.UpdateUserDTO;
import blessed.user.dto.UserRequestDTO;
import blessed.user.dto.UserResponseDTO;
import blessed.user.entity.User;
import blessed.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;
    public UserController(UserService service){
        this.service = service;
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> register(
            @RequestPart("data")RegisterDTO data
            ){
        service.register(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of("success", "usuário criado com sucesso.")
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable UUID id, @RequestBody @Valid UpdateUserDTO newData,
            @AuthenticationPrincipal User userRequest
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.updateDataUser(id, newData, userRequest));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll(){
        List<UserResponseDTO> users = service.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping(params = "firstName")
    public ResponseEntity<List<UserResponseDTO>> getByFirstName(@RequestParam String firstName){
        List<UserResponseDTO> users = service.findByFirstName(firstName);

        return ResponseEntity.ok(users);
    }

    @PutMapping("/admin/{id}/enable")
    public ResponseEntity<Map<String, String>> enable(@PathVariable UUID id) {
        service.enable(id);
        return ResponseEntity.ok(Map.of("success", "Usuário habilitado."));
    }

    @PutMapping("/admin/{id}/disable")
    public ResponseEntity<Map<String, String>> disable(@PathVariable UUID id) {
        service.disable(id);
        return ResponseEntity.ok(Map.of("success", "Usuário desabilitado."));
    }
}
