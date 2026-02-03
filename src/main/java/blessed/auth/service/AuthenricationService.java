package blessed.auth.service;


import blessed.auth.dto.AuthenticationDTO;
import blessed.auth.dto.LoginResponseDTO;
import blessed.auth.dto.RegisterDTO;
import blessed.exception.BusinessException;
import blessed.nonconformity.service.query.SectorQuery;
import blessed.sector.entity.Sector;
import blessed.user.entity.User;
import blessed.user.repository.UserRepository;
import blessed.user.service.query.UserQuery;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenricationService {
    private final UserQuery userQuery;
    private final SectorQuery sectorQuery;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;



    AuthenricationService(
            UserQuery userQuery,
            SectorQuery sectorQuery,
            PasswordEncoder passwordEncoder,
            TokenService tokenService,
            AuthenticationManager authenticationManager
            ){
        this.userQuery = userQuery;
        this.sectorQuery = sectorQuery;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;

    }

    @Transactional
    public void register(RegisterDTO data){

        if (this.userQuery.existsByEmail(data.email())){
            throw new BusinessException("E-mail informado já está em uso.");
        }
        if (this.userQuery.existsByPhone(data.phone())){
            throw new BusinessException("Telefone informado já está em uso.");
        }

        String encryptedPassword = passwordEncoder.encode(data.password());
        User newUser = new User(data, encryptedPassword);
        Sector sectorUser = sectorQuery.byId(data.sectorId());

        newUser.setSector(sectorUser);
        this.userQuery.save(newUser);
    }

    @Transactional
    public LoginResponseDTO login(AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth  = this.authenticationManager.authenticate(usernamePassword);
        User userLogin = (User) auth.getPrincipal();

        User user = userQuery.byId(userLogin.getId());
        String token = tokenService.generateToken(user);

        return new LoginResponseDTO(token, user);
    }

}
