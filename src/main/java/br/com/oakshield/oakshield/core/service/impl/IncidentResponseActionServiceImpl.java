package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.incident.IncidentResponseAction;
import br.com.oakshield.oakshield.core.service.IncidentResponseActionService;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class IncidentResponseActionServiceImpl implements IncidentResponseActionService {

    private final IncidentResponseActionRepository repository;

    public IncidentResponseActionServiceImpl(IncidentResponseActionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<IncidentResponseAction> findAll() {
        return repository.findAll();
    }

    @Override
    public IncidentResponseAction create(IncidentResponseAction incidentResponseAction) {
        incidentResponseAction.setActionDate(OffsetDateTime.now());
        return repository.save(incidentResponseAction);
    }

    @Override
    public IncidentResponseAction findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IncidentResponseAction not found."));
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
