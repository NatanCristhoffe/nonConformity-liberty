package blessed.nonconformity.entity;


import blessed.exception.BusinessException;
import blessed.nonconformity.dto.ActionCompletedRequestDTO;
import blessed.nonconformity.dto.ActionNotExecutedRequestDTO;
import blessed.nonconformity.enums.ActionStatus;
import blessed.nonconformity.enums.NonConformityPriorityLevel;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.dto.NonconformityRequestDTO;
import blessed.nonconformity.enums.QualityToolType;
import blessed.nonconformity.interfaces.QualityToolService;
import blessed.nonconformity.tools.FiveWhyTool;
import blessed.sector.entity.Sector;
import blessed.user.entity.User;
import blessed.utils.DataTimeUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

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
    @Column(length = 150, nullable = false)
    @Size(max = 150)
    private String title;

    @Column(nullable = false)
    @Size(max = 5000)
    private String description;
    private Boolean hasAccidentRisk;

    @Enumerated(EnumType.STRING)
    private NonConformityPriorityLevel priorityLevel;
    private LocalDateTime dispositionDate;
    private LocalDateTime dispositionClosedAt;
    @ManyToOne
    @JoinColumn(name = "linked_rnc_id")
    private NonConformity linkedRnc;
    private String urlEvidence;
    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
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

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


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


    private Boolean requiresQualityTool;

    @Enumerated(EnumType.STRING)
    private QualityToolType selectedTool;



    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )

    @JoinColumn(name = "root_cause_id")
    private RootCause rootCause;

    @OneToOne(mappedBy = "nonconformity", cascade = CascadeType.ALL)
    private FiveWhyTool fiveWhyTool;

    @OneToMany(
            mappedBy = "nonconformity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Action> actions = new HashSet<>();

    @OneToOne(mappedBy = "nonConformity", cascade = CascadeType.ALL, orphanRemoval = true)
    private EffectivenessAnalysis effectivenessAnalysis;

    private LocalDateTime closedAt;

    public NonConformity(
            NonconformityRequestDTO data, Sector source, Sector responsibleDepartment,
            User createBy, User dispositionOwner, User effectivenessAnalyst
            ){
        this.title = data.title().toLowerCase();
        this.description = data.description();
        this.hasAccidentRisk = data.hasAccidentRisk();
        this.priorityLevel = data.priorityLevel();
        this.dispositionDate = data.dispositionDate();
        this.urlEvidence = data.urlEvidence();
        this.sourceDepartment = source;
        this.responsibleDepartment = responsibleDepartment;
        this.dispositionOwner = dispositionOwner;
        this.effectivenessAnalyst = effectivenessAnalyst;
        this.requiresQualityTool = data.requiresQualityTool();
        this.selectedTool = data.selectedTool();
        this.status = NonConformityStatus.PENDING;
        this.createdBy = createBy;
        this.createdAt = LocalDateTime.now();
        addLog(
                "[NC] Não conformidade criada" +
                " | Usuário: " + createBy.getFirstName() + " " + createBy.getLastName() +
                " | Data/Hora: " + DataTimeUtils.formatNow()
        );
    }

    public void approve(User user){
        if (this.status != NonConformityStatus.PENDING){
            throw new BusinessException("Não conformidade não pode ser aprovada no status atual.");
        }

        if (this.requiresQualityTool) {
            this.status = NonConformityStatus.WAITING_QUALITY_TOOL;

            switch (this.selectedTool){
                case FIVE_WHYS -> {
                    addLog(
                            "[QUALIDADE] Ferramenta adicionada: 5 Porquês" +
                            " | Usuário: " + user.getFirstName() + " " + user.getLastName() +
                            " | Data/Hora: " + DataTimeUtils.formatNow()
                    );

                }
                case ISHIKAWA -> {
                    throw new BusinessException("Ferramenta não disponível");
                }
                default -> throw new BusinessException("Ferramenta de qualidade não encontrada");
            }

        } else {
            this.status = NonConformityStatus.WAITING_ROOT_CAUSE;
        }

        addLog(
                "[NC] Não conformidade aprovada" +
                " | Usuário: " + user.getFirstName() + " " + user.getLastName() +
                " | Data/Hora: " + DataTimeUtils.formatNow()
        );

    }

    public void correction(User user){
        if (this.status != NonConformityStatus.PENDING){
            throw new BusinessException("Não conformidade não pode ser enviada para correção no status atual.");
        }

        this.status = NonConformityStatus.RETURNED_FOR_CORRECTION;
        addLog(
                "[NC] Enviada para correção" +
                " | Usuário: " + user.getFirstName() + " " + user.getLastName() +
                " | Data/Hora: " + DataTimeUtils.formatNow()
        );

    }


    public void addLog(String message){
        logs.add(new NonconformityLog(this, message));
    }

    public void concludeFiveWhyTool(User user) {
        if (this.status != NonConformityStatus.WAITING_QUALITY_TOOL) {
            throw new BusinessException(
                    "A ferramenta dos 5 Porquês só pode ser concluída quando a NC estiver aguardando ferramenta de qualidade."
            );
        }
        this.fiveWhyTool.setCompleted(true);
        this.status = NonConformityStatus.WAITING_ROOT_CAUSE;
        addLog(
                "[QUALIDADE] Ferramenta 5 Porquês concluída" +
                " | Usuário: " + user.getFirstName() + " " + user.getLastName() +
                " | Data/Hora: " + DataTimeUtils.formatNow()
        );

    }

    public void addRootCause(RootCause rootCause, User user){
        if(this.status != NonConformityStatus.WAITING_ROOT_CAUSE){
            throw new BusinessException(
                    "A causa raiz só pode ser adicionada quando a NC estiver aguardando causa raiz"
            );
        }

        this.rootCause = rootCause;
        rootCause.setNonconformity(this);
        this.status = NonConformityStatus.WAITING_ACTIONS;
        addLog(
                "[CAUSA RAIZ] Definida" +
                " | Usuário: " + user.getFirstName() + " " + user.getLastName() +
                " | Data/Hora: " + DataTimeUtils.formatNow()
        );

    }

    public void addAction(Action action, User responsibleUser, User userRequest){
        if (this.status != NonConformityStatus.WAITING_ACTIONS) {
            throw new BusinessException(
                    "Ação só pode ser adicionada quando a NC estiver aguardando ações."
            );
        }

        action.setNonconformity(this);
        action.setResponsibleUser(responsibleUser);

        this.actions.add(action);
        addLog(
                "[AÇÃO] Adicionada: " + action.getTitle() +
                " | Usuário: " + userRequest.getFirstName() + " " + userRequest.getLastName() +
                " | Responsável: " + responsibleUser.getFirstName() + " " + responsibleUser.getLastName() +
                " | Data/Hora: " + DataTimeUtils.formatNow()
        );
    }

    public void completeAction(Action action, ActionCompletedRequestDTO data, User user) {

        if (!this.actions.contains(action)) {
            throw new BusinessException("A ação não pertence a esta não conformidade.");
        }

        action.complete(data, user);

        addLog(
                "[AÇÃO] Concluída: " + action.getTitle() +
                " | Usuário: " + user.getFirstName() + " " + user.getLastName() +
                " | Data/Hora: " + DataTimeUtils.formatNow()
        );

    }

    public void notExecutedAction(Action action, ActionNotExecutedRequestDTO data, User user) {

        if (!this.actions.contains(action)) {
            throw new BusinessException("A ação não pertence a esta não conformidade.");
        }

        action.markAsNotExecuted(data, user);

        addLog(
                "[AÇÃO] Não executada: " + action.getTitle() +
                " | Usuário: " + user.getFirstName() + " " + user.getLastName() +
                " | Data/Hora: " + DataTimeUtils.formatNow()
        );

    }

    public void closedAction(User user){
        if (this.status != NonConformityStatus.WAITING_ACTIONS){
            throw new BusinessException("A não conformidade não está no status esperado para esta operação.");
        }

        for(Action action : this.actions){
            if (action.getStatus() == ActionStatus.PENDING){
                throw new BusinessException(
                        "Existem ações pendentes. Todas as ações devem ser marcadas como concluídas ou não executadas antes de prosseguir."
                );
            }
        }

        this.status = NonConformityStatus.WAITING_EFFECTIVENESS_CHECK;
        addLog(
                "[AÇÃO] Todas as ações finalizadas" +
                " | Usuário: " + user.getFirstName() + " " + user.getLastName() +
                " | Data/Hora: " + DataTimeUtils.formatNow()
        );

    }

    public void closedDisposition(User user){
        this.dispositionClosedAt = LocalDateTime.now();
        addLog(
                "[DISPOSIÇÃO] concluida." +
                " | Usuário : " + user.getFirstName() + " " + user.getLastName() +
                " | Data : " + DataTimeUtils.formatNow()
        );
    }


    public void addEffectivenessAnalysis(EffectivenessAnalysis analysis, User user) {
        if (this.status != NonConformityStatus.WAITING_EFFECTIVENESS_CHECK) {
            throw new BusinessException(
                    "A análise de eficácia só pode ser realizada quando a NC estiver aguardando verificação de eficácia."
            );
        }

        this.effectivenessAnalysis = analysis;
        this.status = NonConformityStatus.CLOSED;
        this.closedAt = LocalDateTime.now();

        addLog(
                "[EFICÁCIA] Análise registrada" +
                " | Usuário: " + user.getFirstName() + " " + user.getLastName() +
                " | Data/Hora: " + DataTimeUtils.formatNow()
        );

    }

}
