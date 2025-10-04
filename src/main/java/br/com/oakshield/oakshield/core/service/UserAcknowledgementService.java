package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.useracknowledgement.UserAcknowledgement;

import java.util.List;
import java.util.UUID;

public interface UserAcknowledgementService {
    void acknowledgePolicy(UUID userId, UUID policyId);

    List<UserAcknowledgement> getAll();
}
