package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.incident.Incident;

import java.util.List;
import java.util.UUID;

public interface IncidentService {
    List<Incident> findAll();

    Incident create(Incident incident);

    Incident update(UUID id, Incident incident);

    Incident findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);
}
