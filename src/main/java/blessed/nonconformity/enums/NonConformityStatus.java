package blessed.nonconformity.enums;

public enum NonConformityStatus {
    WAITING_QUALITY_TOOL,
    WAITING_ROOT_CAUSE,          // Aguardando causa-raiz
    WAITING_ACTIONS,             // Aguardando ações (imediatas/corretivas)
    WAITING_EFFECTIVENESS_CHECK, // Aguardando avaliação de eficácia
    APPROVED,                    // Aprovada
    CLOSED,                      // Encerrada
    PENDING
}
