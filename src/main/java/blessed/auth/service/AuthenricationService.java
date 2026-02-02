package blessed.auth.service;


import blessed.auth.dto.RegisterDTO;
import blessed.exception.BusinessException;
import blessed.nonconformity.service.query.SectorQuery;
import blessed.sector.entity.Sector;
import blessed.user.entity.User;
import blessed.user.repository.UserRepository;
import blessed.user.service.query.UserQuery;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenricationService {
    private final UserQuery userQuery;
    private final SectorQuery sectorQuery;
    private final PasswordEncoder passwordEncoder;

    AuthenricationService(
            UserQuery userQuery,
            SectorQuery sectorQuery,
            PasswordEncoder passwordEncoder
            ){
        this.userQuery = userQuery;
        this.sectorQuery = sectorQuery;
        this.passwordEncoder = passwordEncoder;

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


}
