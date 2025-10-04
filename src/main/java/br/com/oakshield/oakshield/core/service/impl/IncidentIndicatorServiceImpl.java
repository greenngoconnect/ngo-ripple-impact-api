package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.incident.Incident;
import br.com.oakshield.oakshield.core.domain.incident.IncidentIndicator;
import br.com.oakshield.oakshield.core.domain.incident.Indicator;
import br.com.oakshield.oakshield.core.service.IncidentIndicatorService;
import br.com.oakshield.oakshield.core.service.IncidentService;
import br.com.oakshield.oakshield.core.service.IndicatorService;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IncidentIndicatorServiceImpl implements IncidentIndicatorService {

    private final IncidentIndicatorRepository repository;
    private final IncidentService incidentService;
    private final IndicatorService indicatorService;

    public IncidentIndicatorServiceImpl(IncidentIndicatorRepository repository, IncidentService incidentService, IndicatorService indicatorService) {
        this.repository = repository;
        this.incidentService = incidentService;
        this.indicatorService = indicatorService;
    }

    @Override
    public List<IncidentIndicator> findAll() {
        return repository.findAll();
    }

    @Override
    public IncidentIndicator create(IncidentIndicator incidentIndicator) {
        UUID incidentId = incidentIndicator.getIncident().getId();
        UUID indicatorId = incidentIndicator.getIndicator().getId();
        if (incidentId == null || indicatorId == null) {
            throw new ResourceNotFoundException("Ids title cannot be null or empty.");
        }

        // Cria e seta o ID composto
        IncidentIndicator.IncidentIndicatorId id = new IncidentIndicator.IncidentIndicatorId(incidentId, indicatorId);
        incidentIndicator.setId(id);

        Incident incidentFounded = incidentService.findById(incidentId);
        incidentIndicator.setIncident(incidentFounded);
        Indicator indicatorFounded = indicatorService.findById(indicatorId);
        incidentIndicator.setIndicator(indicatorFounded);

        return repository.save(incidentIndicator);
    }

    @Override
    public IncidentIndicator findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IncidentIndicator not found."));
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
