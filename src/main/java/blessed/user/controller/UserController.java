package blessed.user.controller;

import blessed.auth.dto.RegisterDTO;
import blessed.user.dto.UpdateUserDTO;
import blessed.user.dto.UserRequestDTO;
import blessed.user.dto.UserResponseDTO;
import blessed.user.entity.User;
import blessed.user.enums.UserRole;
import blessed.user.service.UserService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> register(
            @RequestPart("data")RegisterDTO data,
            @AuthenticationPrincipal User user
            ){
        service.create(data, user.getCompany().getId());
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

    @GetMapping()
    public ResponseEntity<List<UserResponseDTO>> getByFirstName(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) UserRole role,
            @AuthenticationPrincipal User user
    ){
        if (firstName != null){
            List<UserResponseDTO> users = service.findByFirstName(firstName, role, user.getCompany().getId());
            return ResponseEntity.ok(users);
        }

        return  ResponseEntity.ok(service.getAll());

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/enable")
    public ResponseEntity<Map<String, String>> enable(
            @PathVariable UUID id, @AuthenticationPrincipal User currentUser) {
        service.enable(id, currentUser);
        return ResponseEntity.ok(Map.of("success", "Usuário habilitado."));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/disable")
    public ResponseEntity<Map<String, String>> disable(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        service.disable(id, currentUser);
        return ResponseEntity.ok(Map.of("success", "Usuário desabilitado."));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/promote")
    public ResponseEntity<Map<String, String>> promote(@PathVariable UUID id, @AuthenticationPrincipal User userRequest){
        service.changeRole(id, UserRole.ADMIN, userRequest);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("success", "Usuário promovido com sucesso"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/demote")
    public ResponseEntity<Map<String, String>> demote(@PathVariable UUID id, @AuthenticationPrincipal User userRequest){
        service.changeRole(id, UserRole.USER, userRequest);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("success", "Usuário definido como user com sucesso"));
    }

}
