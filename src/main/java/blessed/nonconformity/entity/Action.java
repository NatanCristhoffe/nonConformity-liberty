package blessed.nonconformity.entity;

import blessed.exception.BusinessException;
import blessed.nonconformity.dto.ActionCompletedRequestDTO;
import blessed.nonconformity.dto.ActionNotExecutedRequestDTO;
import blessed.nonconformity.dto.ActionRequestDTO;
import blessed.nonconformity.enums.ActionStatus;
import blessed.nonconformity.enums.ActionType;
import blessed.user.entity.User;
import blessed.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "actions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(name = "non_conformity_id", nullable = false)
    private NonConformity nonconformity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsible_user_id")
    private User responsibleUser;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType actionType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionStatus status;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "evidence_url")
    private String evidenceUrl;

    @Column(columnDefinition = "TEXT")
    private String observation;

    @Column(name = "non_execution_reason", columnDefinition = "TEXT")
    private String nonExecutionReason;

    private LocalDateTime completedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finalized_by_user_id")
    private User finalizedBy;


    public Action(ActionRequestDTO data){
        this.title = data.title();
        this.description = data.description();
        this.actionType = data.actionType();
        this.status = ActionStatus.PENDING;
        this.dueDate = data.dueDate();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void complete(ActionCompletedRequestDTO data, User user) {
        if (this.status != ActionStatus.PENDING) {
            throw new BusinessException("A ação só pode ser concluída quando o status estiver PENDING.");
        }

        boolean isResponsible = this.responsibleUser.getId().equals(user.getId());
        boolean isAdmin = user.getRole() == UserRole.ADMIN;

        if (!isResponsible && !isAdmin) {
            throw new BusinessException("Você não tem permissão para concluir esta ação.");
        }

        this.status = ActionStatus.COMPLETED;
        this.evidenceUrl = data.evidenceUrl();
        this.observation = data.observation();
        this.completedAt = LocalDateTime.now();
        this.finalizedBy = user;
        this.updatedAt = LocalDateTime.now();
    }



    public void markAsNotExecuted(ActionNotExecutedRequestDTO data, User user) {
        if (this.status != ActionStatus.PENDING) {
            throw new BusinessException("A ação não pode ser marcada como não executada.");
        }

        boolean isResponsible = this.responsibleUser.getId().equals(user.getId());
        boolean isAdmin = user.getRole() == UserRole.ADMIN;

        if (!isResponsible && !isAdmin) {
            throw new BusinessException("Você não tem permissão para marcar esta ação como não executada.");
        }

        this.status = ActionStatus.NOT_EXECUTED;
        this.nonExecutionReason = data.nonExecutionReason();
        this.completedAt = LocalDateTime.now();
        this.finalizedBy = user;
        this.updatedAt = LocalDateTime.now();
    }
}
