package br.com.greenngoconnect.rippleimpact.core.service;

import br.com.greenngoconnect.rippleimpact.core.domain.useracknowledgement.UserAcknowledgement;

import java.util.List;
import java.util.UUID;

public interface UserAcknowledgementService {
    List<UserAcknowledgement> getAll();
}
