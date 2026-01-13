package blessed.nonconformity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RootCause {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @OneToMany(
            mappedBy = "rootCau",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<FiveWhy> fiveWhys = new HashSet<>();

}
