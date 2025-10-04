package br.com.oakshield.oakshield.core.service.impl;


import br.com.oakshield.oakshield.core.domain.incident.Incident;
import br.com.oakshield.oakshield.core.service.IncidentService;
import br.com.oakshield.oakshield.exception.ResourceFoundException;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository repository;

    public IncidentServiceImpl(IncidentRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Incident> findAll() {
        return repository.findAll();
    }

    @Override
    public Incident create(Incident incident) {
        if (incident.getTitle() == null || incident.getTitle().isEmpty()) {
            throw new ResourceNotFoundException("Incident title cannot be null or empty.");
        }

        validCreateIfNotExists(incident.getTitle());
        return repository.save(incident);
    }

    @Override
    public Incident update(UUID id, Incident incident) {
        if (!repository.existsById(id)) {
            return null; // or throw an exception
        }
        incident.setId(id);
        return repository.save(incident);
    }

    @Override
    public Incident findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found."));
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Incident not found.");
        }
        repository.deleteById(id);
    }

    private void validCreateIfNotExists(String title) {
        repository.findByTitle(title)
                .ifPresent(assetType -> {
                    throw new ResourceFoundException("Incident with title '" + title + "' already exists.");
                });
    }
}
