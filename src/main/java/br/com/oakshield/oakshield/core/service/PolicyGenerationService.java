package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.api.dto.request.GenerateRequest;

import java.util.UUID;

public interface PolicyGenerationService {
    PreviewResponse preview(PreviewRequest req);
    UUID generate(GenerateRequest req);
    PolicyContentResponse getContent(UUID id);
}
