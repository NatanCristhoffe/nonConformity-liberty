package blessed.user.service;

import blessed.auth.dto.RegisterDTO;
import blessed.exception.BusinessException;
import blessed.exception.ResourceNotFoundException;
import blessed.infra.enums.FileType;
import blessed.infra.storage.S3FileStorageService;
import blessed.sector.entity.Sector;
import blessed.sector.repository.SectorRepository;
import blessed.sector.service.query.SectorQuery;
import blessed.user.dto.UserRequestDTO;
import blessed.user.dto.UserResponseDTO;
import blessed.user.entity.User;
import blessed.user.repository.UserRepository;
import blessed.user.service.query.UserQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class UserService{

    private final UserQuery userQuery;
    private final PasswordEncoder passwordEncoder;
    private final SectorQuery sectorQuery;
    private final S3FileStorageService s3Service;

    public UserService(
            UserQuery userQuery, PasswordEncoder passwordEncoder, SectorQuery sectorQuery,
            S3FileStorageService s3Service
    ){
        this.passwordEncoder = passwordEncoder;
        this.userQuery = userQuery;
        this.sectorQuery = sectorQuery;
        this.s3Service = s3Service;
    }
    public List<UserResponseDTO> getAll(){
         return userQuery.getAll();
    }
    public List<UserResponseDTO> findByFirstName(String firstName) {
        return userQuery.byName(firstName);
    }


    @Transactional
    public void enable(UUID userId) {
        User user = userQuery.byId(userId);
        user.enable();
    }

    @Transactional
    public void disable(UUID userId) {
        User user = userQuery.byId(userId);
        user.disable();
    }

    @Transactional
    public void register(RegisterDTO data, MultipartFile file){
        if (this.userQuery.existsByEmail(data.email())){
            throw new BusinessException("E-mail informado já está em uso.");
        }
        if (this.userQuery.existsByPhone(data.phone())){
            throw new BusinessException("Telefone informado já está em uso.");
        }

        String photoProfileUrl = null;
        if (file != null && !file.isEmpty()){
            photoProfileUrl = s3Service.uploadFile(file, "users", FileType.IMAGE);
        }


        Sector sector = sectorQuery.byId(data.sectorId());
        String encryptedPassword = passwordEncoder.encode(data.password());

        User newUser = new User(data, encryptedPassword, sector, photoProfileUrl);
        userQuery.save(newUser);
    }

}
