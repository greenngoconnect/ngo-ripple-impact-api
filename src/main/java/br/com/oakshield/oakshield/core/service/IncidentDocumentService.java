package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.incident.IncidentDocument;

import java.util.List;
import java.util.UUID;

public interface IncidentDocumentService {
    IncidentDocument create(IncidentDocument incidentDocument);

    List<IncidentDocument> findAll();

    IncidentDocument findById(UUID id);

    void deleteById(UUID id);

}
