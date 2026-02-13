package blessed.auth.controller;


import blessed.auth.dto.AuthenticationDTO;
import blessed.auth.dto.LoginResponseDTO;
import blessed.auth.dto.RegisterDTO;
import blessed.auth.service.AuthenricationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenricationService service;

    public AuthenticationController(
    AuthenricationService service
    ){
        this.service = service;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data){
        return ResponseEntity.ok(service.login(data));
    }

}
