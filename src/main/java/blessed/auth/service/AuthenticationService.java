package blessed.auth.service;


import blessed.auth.dto.AuthenticationDTO;
import blessed.auth.dto.LoginResponseDTO;
import blessed.user.entity.User;
import blessed.user.service.query.UserQuery;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserQuery userQuery;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    AuthenticationService(
            UserQuery userQuery,
            TokenService tokenService,
            AuthenticationManager authenticationManager
            ){
        this.userQuery = userQuery;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;

    }


    @Transactional
    public LoginResponseDTO login(AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth  = this.authenticationManager.authenticate(usernamePassword);
        User userLogin = (User) auth.getPrincipal();

        User user = userQuery.byId(userLogin.getCompany().getId(), userLogin.getId());
        String token = tokenService.generateToken(user);

        return new LoginResponseDTO(token, user);
    }

}
