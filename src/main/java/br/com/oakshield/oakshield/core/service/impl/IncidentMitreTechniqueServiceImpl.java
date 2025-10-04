package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.incident.Incident;
import br.com.oakshield.oakshield.core.domain.incident.IncidentMitreTechnique;
import br.com.oakshield.oakshield.core.domain.mitretechnique.MitreTechnique;
import br.com.oakshield.oakshield.core.service.IncidentMitreTechniqueService;
import br.com.oakshield.oakshield.core.service.IncidentService;
import br.com.oakshield.oakshield.core.service.MitreTechniqueService;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IncidentMitreTechniqueServiceImpl implements IncidentMitreTechniqueService {

    private final IncidentMitreTechniqueRepository repository;
    private final IncidentService incidentService;
    private final MitreTechniqueService mitreTechniqueService;

    public IncidentMitreTechniqueServiceImpl(IncidentMitreTechniqueRepository repository, IncidentService incidentService, MitreTechniqueService mitreTechniqueService) {
        this.repository = repository;
        this.incidentService = incidentService;
        this.mitreTechniqueService = mitreTechniqueService;
    }

    @Override
    public List<IncidentMitreTechnique> findAll() {
        return repository.findAll();
    }

    @Override
    public IncidentMitreTechnique create(IncidentMitreTechnique incidentMitreTechnique) {
        UUID incidentId = incidentMitreTechnique.getIncident().getId();
        UUID mitreTechniqueId = incidentMitreTechnique.getMitreTechnique().getId();
        if (incidentId == null || mitreTechniqueId == null) {
            throw new ResourceNotFoundException("Ids title cannot be null or empty.");
        }

        // Cria e seta o ID composto
        IncidentMitreTechnique.IncidentMitreTechniqueId id = new IncidentMitreTechnique.IncidentMitreTechniqueId(incidentId, mitreTechniqueId);
        incidentMitreTechnique.setId(id);

        Incident incidentFounded = incidentService.findById(incidentId);
        incidentMitreTechnique.setIncident(incidentFounded);
        MitreTechnique mitreTechniqueFounded = mitreTechniqueService.findById(mitreTechniqueId);
        incidentMitreTechnique.setMitreTechnique(mitreTechniqueFounded);

        return repository.save(incidentMitreTechnique);
    }

    @Override
    public IncidentMitreTechnique findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IncidentMitreTechnique not found."));
    }

    @Override
    public void deleteById(UUID id) {

        repository.deleteById(id);
    }
}
