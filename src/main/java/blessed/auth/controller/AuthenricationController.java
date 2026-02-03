package blessed.auth.controller;


import blessed.auth.dto.AuthenticationDTO;
import blessed.auth.dto.LoginResponseDTO;
import blessed.auth.dto.RegisterDTO;
import blessed.auth.service.AuthenricationService;
import blessed.auth.service.TokenService;
import blessed.exception.BusinessException;
import blessed.sector.entity.Sector;
import blessed.sector.repository.SectorRepository;
import blessed.sector.service.SectorService;
import blessed.user.entity.User;
import blessed.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthenricationController {

    private final TokenService tokenService;
    private final AuthenricationService service;

    public  AuthenricationController(
    AuthenticationManager authenticationManager,
    TokenService tokenService,
    AuthenricationService service
    ){
        this.tokenService = tokenService;
        this.service = service;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data){
        return ResponseEntity.ok(service.login(data));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody @Valid RegisterDTO data){
        service.register(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Usuário criado com sucesso"));
    }

}
