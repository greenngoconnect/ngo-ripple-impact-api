package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.document.Document;
import br.com.oakshield.oakshield.core.service.DocumentService;
import br.com.oakshield.oakshield.exception.ResourceFoundException;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import br.com.oakshield.oakshield.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository repository;

    public DocumentServiceImpl(DocumentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Document create(Document document) {
        if (document.getName() == null || document.getName().isEmpty()) {
            throw new ResourceNotFoundException("Nome do ativo não pode ser nulo ou vazio.");
        }

        validCreateIfNotExists(document.getName());

        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        document.setUploadedBy(currentUserLogin);
        document.setHashChecksum(UUID.randomUUID().toString());
        document.setUploadedDate(OffsetDateTime.now());
        return repository.save(document);
    }

    @Override
    public List<Document> findAll() {
        return repository.findAll();
    }

    @Override
    public Document update(UUID id, Document document) {
        Document existingDocument = findById(id);
        existingDocument.updateFrom(id, document);
        return repository.save(existingDocument);
    }

    @Override
    public Document findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ativo não encontrado."));
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        if (!existsById(id)) {
            throw new IllegalArgumentException("Ativo não encontrado.");
        }
        repository.deleteById(id);
    }

    private void validCreateIfNotExists(String name) {
        repository.findByName(name)
                .ifPresent(existingDocument -> {
                    throw new ResourceFoundException("Ativo com o nome '" + name + "' já existe.");
                });
    }
}
