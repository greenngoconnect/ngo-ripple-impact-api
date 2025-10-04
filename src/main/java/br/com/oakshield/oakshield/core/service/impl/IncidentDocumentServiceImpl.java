package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.document.Document;
import br.com.oakshield.oakshield.core.domain.incident.Incident;
import br.com.oakshield.oakshield.core.domain.incident.IncidentDocument;
import br.com.oakshield.oakshield.core.service.DocumentService;
import br.com.oakshield.oakshield.core.service.IncidentDocumentService;
import br.com.oakshield.oakshield.core.service.IncidentService;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IncidentDocumentServiceImpl implements IncidentDocumentService {

    private final IncidentDocumentRepository repository;
    private final IncidentService incidentService;
    private final DocumentService documentService;

    public IncidentDocumentServiceImpl(IncidentDocumentRepository repository, IncidentService incidentService, DocumentService documentService) {
        this.repository = repository;
        this.incidentService = incidentService;
        this.documentService = documentService;
    }

    @Override
    public List<IncidentDocument> findAll() {
        return repository.findAll();
    }

    @Override
    public IncidentDocument create(IncidentDocument incidentDocument) {
        UUID incidentId = incidentDocument.getIncident().getId();
        UUID documentId = incidentDocument.getDocument().getId();
        if (incidentId == null || documentId == null) {
            throw new ResourceNotFoundException("Ids title cannot be null or empty.");
        }

        // Cria e seta o ID composto
        IncidentDocument.IncidentDocumentId id = new IncidentDocument.IncidentDocumentId(incidentId, documentId);
        incidentDocument.setId(id);

        Incident incidentFounded = incidentService.findById(incidentId);
        incidentDocument.setIncident(incidentFounded);
        Document documentFounded = documentService.findById(documentId);
        incidentDocument.setDocument(documentFounded);

        return repository.save(incidentDocument);
    }

    @Override
    public IncidentDocument findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IncidentDocument not found."));
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
