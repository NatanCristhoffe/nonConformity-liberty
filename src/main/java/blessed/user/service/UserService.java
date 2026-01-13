package blessed.user.service;

import blessed.exception.BusinessException;
import blessed.exception.ResourceNotFoundException;
import blessed.sector.entity.Sector;
import blessed.sector.repository.SectorRepository;
import blessed.user.dto.UserRequestDTO;
import blessed.user.dto.UserResponseDTO;
import blessed.user.entity.User;
import blessed.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SectorRepository sectorRepository;

    public UserService(
            UserRepository userRepository,
            SectorRepository sectorRepository){
        this.userRepository = userRepository;
        this.sectorRepository = sectorRepository;
    }

    public List<UserResponseDTO> getAll(){
        List<UserResponseDTO> users = userRepository
                .findAll()
                .stream()
                .map(UserResponseDTO::new)
                .toList();
        return users;
    }

    public UserResponseDTO create(UserRequestDTO data){
        if (userRepository.existsByEmail(data.getEmail().toLowerCase())){
            throw new BusinessException("Email já cadastrado");
        }
        if (userRepository.existsByPhone(data.getPhone())){
            throw new BusinessException(
                    "Já existe um usuário cadastrado com o número de telefone informado."
                    );
        }
        Sector sector = sectorRepository.findById(data.getSectorId())
                .orElseThrow(() -> new ResourceNotFoundException("Setor não encontrado"));

        User user = new User(data);
        user.setSector(sector);
        userRepository.save(user);

        return  new UserResponseDTO(user);
    }
}
