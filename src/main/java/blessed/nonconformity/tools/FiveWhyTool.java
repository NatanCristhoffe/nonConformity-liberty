package blessed.nonconformity.tools;

import blessed.nonconformity.entity.FiveWhy;
import blessed.nonconformity.entity.NonConformity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FiveWhyTool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "nonconformity_id", nullable = false)
    private NonConformity nonconformity;

    @OneToMany(
            mappedBy = "fiveWhyTool",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<FiveWhy> fiveWhys = new HashSet<>();

    private boolean completed;
}
