package blessed.nonconformity.sectors;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "sectors",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        }
)
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Sector {

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
}
