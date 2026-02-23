package blessed.company.repository;

import blessed.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID>{
    boolean existsByDocument(String document);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
