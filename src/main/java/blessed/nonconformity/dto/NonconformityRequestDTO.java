package blessed.nonconformity.dto;

import blessed.nonconformity.enums.NonConformityPriorityLevel;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record NonconformityRequestDTO(
        @NotBlank(message = "O título é obrigatório.")
        @Size(min = 5, max = 150, message = "O título deve ter entre 5 e 150 caracteres.")
        String title,

        @NotBlank(message = "A descrição é obrigatória.")
        @Size(min = 5, max = 350, message = "A descrição deve ter entre 5 e 350 caracteres.")
        String description,

        @NotNull(message = "É obrigatório informar se há risco de acidente.")
        Boolean hasAccidentRisk,

        @NotNull(message = "O nível de prioridade é obrigatório.")
        NonConformityPriorityLevel priorityLevel,

        @NotNull(message = "A data de disposição é obrigatória.")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime dispositionDate,

        String urlEvidence,

        Long linkedRncId,

        @NotNull(message = "O setor onde ocorreu a não conformidade deve ser informado.")
        Long sourceDepartmentId,

        @NotNull(message = "O setor responsável pela disposição deve ser informado.")
        Long responsibleDepartmentId,

        @NotNull(message = "O usuário responsável pela disposição deve ser informado.")
        Long dispositionOwnerId,

        @NotNull(message = "O usuário responsável pela análise de eficácia deve ser informado.")
        Long effectivenessAnalystId,

        @NotNull(message = "Informe se devera ser feito o uso de alguma ferramenta.")
        Boolean requiresQualityTool,

        @NotNull(message = "O usuário que criou o registro deve ser informado.")
        Long createdById



        ) {
        }