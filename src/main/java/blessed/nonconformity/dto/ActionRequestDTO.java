package blessed.nonconformity.dto;

import blessed.nonconformity.enums.ActionType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ActionRequestDTO(

        @NotBlank(message = "O título da ação é obrigatório.")
        @Size(max = 120, message = "O título da ação deve ter no máximo 120 caracteres.")
        String title,

        @NotBlank(message = "A descrição da ação é obrigatória.")
        @Size(min = 10, max = 300, message = "A descrição da ação deve ter no máximo 300 caracteres.")
        String description,

        @NotNull(message = "O tipo da ação é obrigatório.")
        ActionType actionType,

        @NotNull(message = "O prazo da ação é obrigatório.")
        @Future(message = "O prazo da ação deve ser uma data futura.")
        LocalDateTime dueDate,

        @NotNull(message = "O responsável pela ação é obrigatório.")
        Long responsibleUserId
){}
