package blessed.nonconformity.dto;

import blessed.nonconformity.entity.Action;
import blessed.nonconformity.enums.ActionStatus;
import blessed.nonconformity.enums.ActionType;
import blessed.user.dto.UserResponseDTO;
import java.time.LocalDateTime;
public record ActionResponseDTO(

        Long id,

        String title,
        String description,

        ActionType actionType,
        ActionStatus status,

        LocalDateTime dueDate,

        UserResponseDTO responsibleUser,
        UserResponseDTO finalizedBy,
        Boolean finalizedByAdmin,

        String evidenceUrl,
        String observation,
        String nonExecutionReason,

        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime completedAt

) {

    public ActionResponseDTO(Action action) {
        this(
                action.getId(),
                action.getTitle(),
                action.getDescription(),
                action.getActionType(),
                action.getStatus(),
                action.getDueDate(),
                action.getResponsibleUser() != null
                        ? new UserResponseDTO(action.getResponsibleUser())
                        : null,
                action.getFinalizedBy() != null
                        ? new UserResponseDTO(action.getFinalizedBy())
                        : null,
                isFinalizedByAdmin(action),
                action.getEvidenceUrl(),
                action.getObservation(),
                action.getNonExecutionReason(),
                action.getCreatedAt(),
                action.getUpdatedAt(),
                action.getCompletedAt()
        );
    }
    private static Boolean isFinalizedByAdmin(Action action) {
        if (action.getFinalizedBy() == null || action.getResponsibleUser() == null) {
            return false;
        }

        return !action.getFinalizedBy().getId()
                .equals(action.getResponsibleUser().getId());
    }

}
