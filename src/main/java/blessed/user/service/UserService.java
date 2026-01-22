package blessed.user.service;

import blessed.exception.BusinessException;
import blessed.exception.ResourceNotFoundException;
import blessed.sector.entity.Sector;
import blessed.sector.repository.SectorRepository;
import blessed.user.dto.UserRequestDTO;
import blessed.user.dto.UserResponseDTO;
import blessed.user.entity.User;
import blessed.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService{
    @Autowired
    private UserRepository userRepository;

    public List<UserResponseDTO> getAll(){
        List<UserResponseDTO> users = userRepository
                .findAll()
                .stream()
                .map(UserResponseDTO::new)
                .toList();
        return users;
    }

    public List<UserResponseDTO> findByFirstName(String firstName) {
        List<User> usersByname = userRepository.findByFirstNameStartingWithIgnoreCase(firstName, Limit.of(5));

        List<UserResponseDTO> users = usersByname
                .stream()
                .map(UserResponseDTO::new)
                .toList();

        return users;
    }
}
