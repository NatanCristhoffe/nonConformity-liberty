package blessed.nonconformity.entity;


import blessed.exception.BusinessException;
import blessed.nonconformity.enums.NonConformityPriorityLevel;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.dto.NonconformityRequestDTO;
import blessed.sector.entity.Sector;
import blessed.user.entity.User;
import blessed.utils.DataTimeUtils;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
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

    @ManyToOne
    @JoinColumn(name = "disposition_owner_id", nullable = false)
    private User dispositionOwner;

    @ManyToOne
    @JoinColumn(name = "effectiveness_analyst_id", nullable = false)
    private User effectivenessAnalyst;

    @ManyToOne
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdBy;


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

    //Root Cause
    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "root_cause_id")
    private RootCause rootCause;

    @OneToOne(mappedBy = "nonConformity", cascade = CascadeType.ALL)
    private FiveWhy fiveWhyTool;



    public NonConformity(NonconformityRequestDTO data){
        this.title = data.title();
        this.description = data.description();
        this.hasAccidentRisk = data.hasAccidentRisk();
        this.priorityLevel = data.priorityLevel();
        this.dispositionDate = data.dispositionDate();
        this.urlEvidence = data.urlEvidence();
        this.status = NonConformityStatus.WAITING_ROOT_CAUSE;
        addLog(
                "Não conformidade criada | "
                + DataTimeUtils.formatNow()
                + " | Status: Aguardando causa-raiz"
        );
    }

    private void addLog(String message){
        logs.add(new NonconformityLog(this, message));
    }

    public void addRootCause(RootCause rootCause){
        if(this.status != NonConformityStatus.WAITING_ROOT_CAUSE){
            throw new BusinessException(
                    "A causa raiz só pode ser adicionada quando a NC estiver aguardando causa raiz"
            );
        }

        this.rootCause = rootCause;
        this.status = NonConformityStatus.WAITING_ACTIONS;
        addLog(
                "Causa raiz definida | " +
                DataTimeUtils.formatNow() +
                " | Status: Aguardando ações"
        );
    }

}
