package br.com.oakshield.oakshield.core.repository;

import br.com.oakshield.oakshield.core.domain.useracknowledgement.UserAcknowledgement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserAcknowledgementRepository extends JpaRepository<UserAcknowledgement, UUID> {
    boolean existsByUserIdAndPolicyId(UUID userId, UUID policyId);
}
