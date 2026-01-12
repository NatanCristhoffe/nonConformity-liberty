package blessed.NonConformity.nonConformity;


import blessed.NonConformity.nonconformityLog.NonconformityLog;
import blessed.NonConformity.sectors.Sector;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Table(name = "nonconformities")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class NonConformity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Boolean hasAccidentRisk;
    private NonConformityPriorityLevel priorityLevel;
    private LocalDateTime dispositionDate;
    @ManyToOne
    @JoinColumn(name = "linked_rnc_id")
    private NonConformity linkedRnc;
    private String urlEvidence;
    private NonConformityStatus status;
//    private User dispositionOwner;
//    private User effectivenessAnalyst;
//    private User userCreated;


    @OneToMany(
            mappedBy = "nonconformity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<NonconformityLog> logs = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "source_department_id")
    private Sector sourceDepartment;

    @ManyToOne
    @JoinColumn(name = "responsible_department_id")
    private Sector responsibleDepartment;

    public NonConformity(NonconformityRequestDTO data){
        this.title = data.title();
        this.description = data.description();
        this.urlEvidence = data.urlEvidence();
        this.status = NonConformityStatus.WAITING_ROOT_CAUSE;
        addLog("Não conformidade criada - aguardando causa-raiz");
    }

    private void addLog(String message){
        logs.add(new NonconformityLog(this, message));
    }

}
