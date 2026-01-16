package blessed.nonconformity.repository;

import blessed.nonconformity.entity.EffectivenessAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EffectivenessRepository extends JpaRepository<EffectivenessAnalysis, Long> {
}
