package blessed.common.entity;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;

import java.time.Instant;

@MappedSuperclass
@Getter
public class AuditableEntity {
    @Column(nullable = false, updatable = false)
    protected Instant createdAt;

    @PrePersist
    protected  void onCreate(){
        this.createdAt = Instant.now();
    }
}
