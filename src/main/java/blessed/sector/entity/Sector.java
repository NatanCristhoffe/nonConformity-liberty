package blessed.sector.entity;

import blessed.common.entity.AuditableEntity;
import blessed.company.entity.Company;
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
                @UniqueConstraint(columnNames = {"name", "company_id"})
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

    @Column(nullable = false)
    private String name;
    private String description;
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    public Sector(SectorRequestDTO data, Company company){
        this.name = data.name().toLowerCase();
        this.description = data.description().toLowerCase();
        this.active = true;
        this.company = company;
    }

    public void enable(){
        this.active = true;
    }

    public void disable(){
        this.active = false;
    }

    public void update(SectorRequestDTO data){
        this.name = data.name().toLowerCase();
        this.description = data.description().toLowerCase();
    }

}

