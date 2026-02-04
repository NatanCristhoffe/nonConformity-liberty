package blessed.sector.entity;

import blessed.common.entity.AuditableEntity;
import blessed.sector.dto.SectorRequestDTO;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "sectors",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        }
)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class Sector extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    private boolean active;

    public Sector(SectorRequestDTO data){
        this.name = data.name().toLowerCase();
        this.description = data.description().toLowerCase();
        this.active = true;
    }

    public void enable(){
        this.active = true;
    }

    public void disable(){
        this.active = false;
    }

}

