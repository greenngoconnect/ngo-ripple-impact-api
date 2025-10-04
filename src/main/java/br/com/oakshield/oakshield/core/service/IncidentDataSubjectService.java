package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.incident.IncidentDataSubject;

import java.util.List;
import java.util.UUID;

public interface IncidentDataSubjectService {
    IncidentDataSubject create(IncidentDataSubject incidentDataSubject);

    List<IncidentDataSubject> findAll();

    IncidentDataSubject findById(UUID id);

    void deleteById(UUID id);

}
