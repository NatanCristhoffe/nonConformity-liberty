package blessed.user.service.query;

import blessed.company.entity.Company;
import blessed.exception.BusinessException;
import blessed.exception.ResourceNotFoundException;
import blessed.user.dto.UserResponseDTO;
import blessed.user.entity.User;
import blessed.user.enums.UserRole;
import blessed.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserQuery {

    private final UserRepository repository;


    public UserQuery(UserRepository repository){
        this.repository = repository;
    }


    public List<UserResponseDTO> getAll(UUID companyId){
        return repository.findAllByCompanyId(companyId)
                .stream()
                .map(UserResponseDTO::new)
                .toList();
    }

    public void save(User newUser, Company company){
        Long totalUser = countByCompany(company.getId());

        if (!company.getPlanType().canAddUser(totalUser)){
            throw  new BusinessException("Limite de usuários do plano atingido.");
        }
        repository.save(newUser);
    }

    public User byId(UUID companyId,UUID userId){
        return repository.findById(companyId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
    }

    public List<UserResponseDTO> byName(String firstName, UserRole roleFilter, UUID companyId){
        return repository.findByFirstNameAndRole(firstName, roleFilter,companyId, Limit.of(5))
                .stream()
                .map(UserResponseDTO::new)
                .toList();
    }

    public Boolean existsByEmail(String email){
         return repository.existsByEmail(email);
    }

    public Boolean existsByPhone(String phone){
        return repository.existsByPhone(phone);
    }

    public Optional<User> byEmail(String email){
        return repository.findByEmail(email);
    }

    private Long countByCompany(UUID companyId){
        return repository.countActiveUsersByCompany(companyId);
    }
}
