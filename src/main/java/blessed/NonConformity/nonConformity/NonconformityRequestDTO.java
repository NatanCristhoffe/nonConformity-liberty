package blessed.NonConformity.nonConformity;

public record NonconformityRequestDTO(
        String title, String description,
        String urlEvidence, Long sourceDepartmentId,
        Long responsibleDepartmentId
        ) {
}
