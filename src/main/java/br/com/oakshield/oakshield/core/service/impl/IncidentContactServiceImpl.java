package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.contact.Contact;
import br.com.oakshield.oakshield.core.domain.incident.Incident;
import br.com.oakshield.oakshield.core.domain.incident.IncidentContact;
import br.com.oakshield.oakshield.core.service.ContactService;
import br.com.oakshield.oakshield.core.service.IncidentContactService;
import br.com.oakshield.oakshield.core.service.IncidentService;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IncidentContactServiceImpl implements IncidentContactService {

    private final IncidentContactRepository repository;
    private final IncidentService incidentService;
    private final ContactService contactService;

    public IncidentContactServiceImpl(IncidentContactRepository repository, IncidentService incidentService, ContactService contactService) {
        this.repository = repository;
        this.incidentService = incidentService;
        this.contactService = contactService;
    }

    @Override
    public List<IncidentContact> findAll() {
        return repository.findAll();
    }

    @Override
    public IncidentContact create(IncidentContact incidentContact) {
        UUID incidentId = incidentContact.getIncident().getId();
        UUID contactId = incidentContact.getContact().getId();
        if (incidentId == null || contactId == null) {
            throw new ResourceNotFoundException("Ids title cannot be null or empty.");
        }

        // Cria e seta o ID composto
        IncidentContact.IncidentContactId id = new IncidentContact.IncidentContactId(incidentId, contactId);
        incidentContact.setId(id);

        Incident incidentFounded = incidentService.findById(incidentId);
        incidentContact.setIncident(incidentFounded);
        Contact contactFounded = contactService.findById(contactId);
        incidentContact.setContact(contactFounded);

        return repository.save(incidentContact);
    }

    @Override
    public IncidentContact findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IncidentContact not found."));
    }

    @Override
    public void deleteById(UUID id) {

        repository.deleteById(id);
    }
}
