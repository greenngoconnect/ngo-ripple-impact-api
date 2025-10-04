package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.incident.IncidentResponseAction;

import java.util.List;
import java.util.UUID;

public interface IncidentResponseActionService {
    IncidentResponseAction create(IncidentResponseAction incidentResponseAction);

    List<IncidentResponseAction> findAll();

    IncidentResponseAction findById(UUID id);

    void deleteById(UUID id);

}
