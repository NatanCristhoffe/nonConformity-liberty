package blessed.user.service.query;

import blessed.exception.ResourceNotFoundException;
import blessed.user.entity.User;
import blessed.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserQuery {
    @Autowired
    UserRepository repository;

    public User byId(UUID id){
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }
}
