package blessed.auth.controller;


import blessed.auth.dto.AuthenticationDTO;
import blessed.auth.dto.LoginResponseDTO;
import blessed.auth.dto.RegisterDTO;
import blessed.auth.service.TokenService;
import blessed.exception.BusinessException;
import blessed.sector.entity.Sector;
import blessed.sector.repository.SectorRepository;
import blessed.sector.service.SectorService;
import blessed.user.entity.User;
import blessed.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenricationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SectorRepository sectorRepository;
    @Autowired
    private TokenService tokenService;


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) auth.getPrincipal());


        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data){
        if (this.userRepository.findByEmail(data.email()) != null){
            throw new BusinessException("E-mai já consta na base de dados.");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data, encryptedPassword);
        Sector sectorUser = sectorRepository.findById(data.sectorId())
                .orElseThrow(() -> new BusinessException("Setor não encontrado."));

        newUser.setSector(sectorUser);
        this.userRepository.save(newUser);

        return ResponseEntity.ok().build();

    }


}
