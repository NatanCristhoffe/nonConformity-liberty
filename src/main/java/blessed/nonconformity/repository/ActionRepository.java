package blessed.nonconformity.repository;

import blessed.nonconformity.entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActionRepository extends JpaRepository<Action, Long>{
    boolean existsByIdAndResponsibleUserId(Long actionId, UUID responsibleUserId);
}
