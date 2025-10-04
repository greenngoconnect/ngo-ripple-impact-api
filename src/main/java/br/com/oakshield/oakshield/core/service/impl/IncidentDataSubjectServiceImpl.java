package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.datasubject.DataSubject;
import br.com.oakshield.oakshield.core.domain.incident.Incident;
import br.com.oakshield.oakshield.core.domain.incident.IncidentDataSubject;
import br.com.oakshield.oakshield.core.service.DataSubjectService;
import br.com.oakshield.oakshield.core.service.IncidentDataSubjectService;
import br.com.oakshield.oakshield.core.service.IncidentService;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IncidentDataSubjectServiceImpl implements IncidentDataSubjectService {

    private final IncidentDataSubjectRepository repository;
    private final IncidentService incidentService;
    private final DataSubjectService dataSubjectService;

    public IncidentDataSubjectServiceImpl(IncidentDataSubjectRepository repository, IncidentService incidentService, DataSubjectService dataSubjectService) {
        this.repository = repository;
        this.incidentService = incidentService;
        this.dataSubjectService = dataSubjectService;
    }

    @Override
    public List<IncidentDataSubject> findAll() {
        return repository.findAll();
    }

    @Override
    public IncidentDataSubject create(IncidentDataSubject incidentDataSubject) {
        UUID incidentId = incidentDataSubject.getIncident().getId();
        UUID dataSubjectId = incidentDataSubject.getDataSubject().getId();
        if (incidentId == null || dataSubjectId == null) {
            throw new ResourceNotFoundException("Ids title cannot be null or empty.");
        }

        // Cria e seta o ID composto
        IncidentDataSubject.IncidentDataSubjectId id = new IncidentDataSubject.IncidentDataSubjectId(incidentId, dataSubjectId);
        incidentDataSubject.setId(id);

        Incident incidentFounded = incidentService.findById(incidentId);
        incidentDataSubject.setIncident(incidentFounded);
        DataSubject dataSubjectFounded = dataSubjectService.findById(dataSubjectId);
        incidentDataSubject.setDataSubject(dataSubjectFounded);

        return repository.save(incidentDataSubject);
    }

    @Override
    public IncidentDataSubject findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IncidentDataSubject not found."));
    }

    @Override
    public void deleteById(UUID id) {

        repository.deleteById(id);
    }
}
