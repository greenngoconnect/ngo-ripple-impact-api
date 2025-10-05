package br.com.greenngoconnect.rippleimpact.core.repository;

import br.com.greenngoconnect.rippleimpact.core.domain.useracknowledgement.UserAcknowledgement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserAcknowledgementRepository extends JpaRepository<UserAcknowledgement, UUID> {
}
