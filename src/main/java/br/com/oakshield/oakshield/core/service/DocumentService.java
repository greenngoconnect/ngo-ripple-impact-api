package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.document.Document;

import java.util.List;
import java.util.UUID;

public interface DocumentService {
    Document create(Document document);

    List<Document> findAll();

    Document update(UUID id, Document document);

    Document findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);

}
