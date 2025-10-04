package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.incident.IncidentContact;

import java.util.List;
import java.util.UUID;

public interface IncidentContactService {
    IncidentContact create(IncidentContact incidentContact);

    List<IncidentContact> findAll();

    IncidentContact findById(UUID id);

    void deleteById(UUID id);

}
