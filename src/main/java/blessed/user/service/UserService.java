package blessed.user.service;

import blessed.exception.BusinessException;
import blessed.exception.ResourceNotFoundException;
import blessed.sector.entity.Sector;
import blessed.sector.repository.SectorRepository;
import blessed.user.dto.UserRequestDTO;
import blessed.user.dto.UserResponseDTO;
import blessed.user.entity.User;
import blessed.user.repository.UserRepository;
import blessed.user.service.query.UserQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService{

    private final UserQuery userQuery;

    public UserService(UserQuery userQuery){
        this.userQuery = userQuery;
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

}
