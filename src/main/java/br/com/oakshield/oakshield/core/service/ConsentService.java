package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.consent.Consent;

import java.util.List;
import java.util.UUID;

public interface ConsentService {
    Consent create(Consent consent);

    List<Consent> findAll();

    Consent update(UUID id, Consent consent);

    Consent findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);

}
