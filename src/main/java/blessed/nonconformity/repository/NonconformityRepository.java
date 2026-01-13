package blessed.nonconformity.repository;

import blessed.nonconformity.entity.NonConformity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NonconformityRepository extends JpaRepository<NonConformity, Long> {
}
