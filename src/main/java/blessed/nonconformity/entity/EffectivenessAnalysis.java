package blessed.nonconformity.entity;


import blessed.nonconformity.dto.EffectivenessAnalysisRequestDTO;
import blessed.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "EffectivenessAnalysis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class EffectivenessAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean effective;

    @Column(columnDefinition = "TEXT", nullable = false)
    @Size(min = 10, max = 5000)
    private String effectivenessDescription;

    @Column(nullable = false)
    private LocalDateTime analyzedAt;

    @ManyToOne
    @JoinColumn(name = "analyzed_by_id", nullable = false)
    private User analyzedByUser;


    @OneToOne
    @JoinColumn(
            name = "non_conformity_id",
            nullable = false,
            unique = true
    )
    private NonConformity nonConformity;

    public EffectivenessAnalysis(EffectivenessAnalysisRequestDTO data, NonConformity nc, User user){
        this.effective = data.effective();
        this.effectivenessDescription = data.effectivenessDescription();
        this.nonConformity = nc;
        this.analyzedByUser = user;
        this.analyzedAt = LocalDateTime.now();
    }


}
