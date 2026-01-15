package blessed.nonconformity.repository;

import blessed.nonconformity.entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepository extends JpaRepository<Action, Long>{
}
