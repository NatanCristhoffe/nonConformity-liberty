package blessed.auth.controller;


import blessed.auth.dto.AuthenticationDTO;
import blessed.auth.dto.LoginResponseDTO;
import blessed.auth.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(
    AuthenticationService service
    ){
        this.service = service;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data){
        return ResponseEntity.ok(service.login(data));
    }

}
