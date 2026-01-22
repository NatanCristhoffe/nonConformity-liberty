package blessed.nonconformity.repository;

import blessed.nonconformity.dto.NonconformityResponseDTO;
import blessed.nonconformity.entity.NonConformity;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NonconformityRepository extends JpaRepository<NonConformity, Long> {
    List<NonConformity> findTop5ByTitleStartingWithIgnoreCase(String title);
}
