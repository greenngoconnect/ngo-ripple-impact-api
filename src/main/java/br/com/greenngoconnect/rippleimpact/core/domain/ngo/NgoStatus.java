package br.com.greenngoconnect.rippleimpact.core.domain.ngo;

import lombok.Getter;

@Getter
public enum NgoStatus {
    FAILED("Reprovada"),
    ACTIVE("Ativa"),
    INACTIVE("Inativa"),
    SUSPENDED("Suspensa"),
    APPROVED("Aprovada"),
    APPROVeD_WITH_CONDITIONS("Aprovada com Condições"),
    PENDING_APPROVAL("Pendente de Aprovação");

    private final String description;

    NgoStatus(String description) {
        this.description = description;
    }
}
