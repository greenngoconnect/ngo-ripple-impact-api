package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.incident.IncidentMitreTechnique;

import java.util.List;
import java.util.UUID;

public interface IncidentMitreTechniqueService {
    IncidentMitreTechnique create(IncidentMitreTechnique incidentMitreTechnique);

    List<IncidentMitreTechnique> findAll();

    IncidentMitreTechnique findById(UUID id);

    void deleteById(UUID id);

}
