package br.com.greenngoconnect.rippleimpact.core.domain.auditrecord;

public enum AuditStatus {
    PENDING("Pendente"),
    IN_PROGRESS("Em Progresso"),
    COMPLIANT("Conforme"),
    NON_COMPLIANT("Não Conforme"),
    NOT_APPLICABLE("Não Aplicável"),
    COMPLETED("Concluído");

    private final String description;

    AuditStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
