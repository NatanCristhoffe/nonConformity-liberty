package blessed.user.service;

import blessed.application.dto.AdminOnboardingRequestDTO;
import blessed.auth.dto.RegisterDTO;
import blessed.company.entity.Company;
import blessed.company.service.query.CompanyQuery;
import blessed.exception.BusinessException;
import blessed.exception.ResourceNotFoundException;
import blessed.infra.enums.FileType;
import blessed.infra.storage.S3FileStorageService;
import blessed.sector.entity.Sector;
import blessed.sector.repository.SectorRepository;
import blessed.sector.service.query.SectorQuery;
import blessed.user.dto.UpdateUserDTO;
import blessed.user.dto.UserRequestDTO;
import blessed.user.dto.UserResponseDTO;
import blessed.user.entity.User;
import blessed.user.enums.UserRole;
import blessed.user.repository.UserRepository;
import blessed.user.service.query.UserQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final CompanyQuery companyQuery;

    public UserService(
            UserQuery userQuery, PasswordEncoder passwordEncoder, SectorQuery sectorQuery,
            CompanyQuery companyQuery
    ){
        this.passwordEncoder = passwordEncoder;
        this.userQuery = userQuery;
        this.sectorQuery = sectorQuery;
        this.companyQuery = companyQuery;
    }
    public List<UserResponseDTO> getAll(){
         return userQuery.getAll();
    }

    public List<UserResponseDTO> findByFirstName(
            String firstName, UserRole role, UUID companyId) {
        return userQuery.byName(firstName, role, companyId);
    }


    @Transactional
    public void enable(UUID userId, User currentUser, UUID companyId) {
        User user = userQuery.byId(userId);
        validateNotSelOfOperation(user, currentUser);
        validateIfUserPertenceCompany(companyId, user);

        user.enable();
    }

    @Transactional
    public void disable(UUID userId, User currentUser, UUID companyId){
        User user = userQuery.byId(userId);
        validateNotSelOfOperation(user, currentUser);
        validateIfUserPertenceCompany(companyId, user);

        user.disable();
    }

    @Transactional
    public void register(AdminOnboardingRequestDTO data, Company company, Sector sector){
        validateUsersData(data.email(), data.phone());

        String encryptedPassword = encryptedPassword(data.password());

        User newUser = new User(data, encryptedPassword, sector, company);
        userQuery.save(newUser, company);
    }

    @Transactional
    public void create(RegisterDTO data, UUID companyId){
        validateUsersData(data.email(), data.phone());

        Company company = companyQuery.byId(companyId);
        Sector sector = sectorQuery.byId(data.sectorId());
        String encryptedPassword = encryptedPassword(data.password());

        User newUser = new User(data, encryptedPassword, sector, company);
        userQuery.save(newUser, company);
    }


    @Transactional
    public UserResponseDTO updateDataUser(
            UUID userUpdateId, UpdateUserDTO newData, User userRequest,
            UUID companyId
    ){
        User user = userQuery.byId(userUpdateId);

        validateIfUserPertenceCompany(companyId, user);
        validateUserOwnership(user, userRequest);

        Sector sector =  sectorQuery.byId(newData.sectorId());

        String newPassword =  null;
        boolean isTryingToChangePassword =
                newData.newPassword() != null || newData.oldPassword() != null;

        if (isTryingToChangePassword){
            if (newData.newPassword() == null || newData.oldPassword() == null) {
                throw new BusinessException("Para alterar a senha, informe senha atual e nova senha.");
            }

            if (passwordEncoder.matches(newData.newPassword(), user.getPassword())) {
                throw new BusinessException("A nova senha não pode ser igual à senha atual.");
            }

            boolean passwordMatches = passwordEncoder.matches(newData.oldPassword(), user.getPassword());

            if (!passwordMatches){
                throw new BusinessException("Senha antiga está incorreta.");
            }

            newPassword = encryptedPassword(newData.newPassword());
        }
        user.update(newData, sector, newPassword);

        return new UserResponseDTO(user);
    }

    @Transactional
    public void changeRole(UUID userId, UserRole newRole, User userRequest, UUID companyId){
        User user = userQuery.byId(userId);
        validateIfUserPertenceCompany(companyId, user);

        if (user.getId().equals(userRequest.getId())){
            throw  new BusinessException("Você não pode alterar sua própria role");
        }

        if (user.getRole() == newRole){
            throw  new BusinessException("Usuário já possui essa role");
        }
        user.setRole(newRole);
    }


    private String encryptedPassword(String password){
        return passwordEncoder.encode(password);
    }

    private void validateUsersData(String email, String phone){
        if (this.userQuery.existsByEmail(email)){
            throw new BusinessException("E-mail informado já está em uso.");
        }
        if (this.userQuery.existsByPhone(phone)){
            throw new BusinessException("Telefone informado já está em uso.");
        }
    }

    private void validateNotSelOfOperation(User user, User currentUser){
        if (user.getId().equals(currentUser.getId())){
            throw new BusinessException(
                    "Operação não permitida: não é possível habilitar ou desabilitar o próprio usuário."
            );
        }
    }

    private void validateIfUserPertenceCompany(UUID companyId, User user){
        if (!companyId.equals(user.getCompany().getId())){
            throw new BusinessException("Você não pode atualizar os dados desse usuário.");
        }
    }
    private void validateUserOwnership(User user, User currentUser){
        if (!user.getId().equals(currentUser.getId())){
            throw new BusinessException("Usuário não autorizado.");
        }
    }

}
