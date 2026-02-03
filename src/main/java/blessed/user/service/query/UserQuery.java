package blessed.user.service.query;

import blessed.exception.ResourceNotFoundException;
import blessed.user.dto.UserResponseDTO;
import blessed.user.entity.User;
import blessed.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserQuery {

    private final UserRepository repository;

    public List<UserResponseDTO> getAll(){
        return repository.findAll()
                .stream()
                .map(UserResponseDTO::new)
                .toList();
    }

    public UserQuery(UserRepository repository){
        this.repository = repository;
    }

    public void save(User newUser){
        repository.save(newUser);
    }

    public User byId(UUID id){
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    public List<UserResponseDTO> byName(String firstName){
        return repository.findByFirstNameStartingWithIgnoreCase(firstName, Limit.of(5))
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


}
