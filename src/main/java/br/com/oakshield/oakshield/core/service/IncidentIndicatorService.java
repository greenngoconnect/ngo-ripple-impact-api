package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.incident.IncidentIndicator;

import java.util.List;
import java.util.UUID;

public interface IncidentIndicatorService {
    IncidentIndicator create(IncidentIndicator asset);

    List<IncidentIndicator> findAll();

    IncidentIndicator findById(UUID id);

    void deleteById(UUID id);

}
