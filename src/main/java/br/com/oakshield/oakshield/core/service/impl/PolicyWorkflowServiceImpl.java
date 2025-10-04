package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.policy.*;
import br.com.oakshield.oakshield.core.service.PolicyWorkflowService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyWorkflowServiceImpl implements PolicyWorkflowService {

    private final PolicyRepository policyRepository;
    private final PolicyContentRepository policyContentRepository;
    private final PdfRenderer pdfRenderer = null;
    // Nota: pdfRenderer é opcional. Se você quiser suportar publicação em PDF,
    // forneça uma implementação concreta e registre como @Bean.

    @Transactional
    @Override
    public SubmitReviewResponse submitReview(UUID policyId, SubmitReviewRequest request) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Policy não encontrada"));

        PolicyStatus previous = policy.getPolicyStatus();

        // Só permite submissão quando está em DRAFT
        if (previous != PolicyStatus.DRAFT) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A policy precisa estar em DRAFT para ser submetida à revisão (status atual: " + previous + ")"
            );
        }

        // Transição
        policy.setPolicyStatus(PolicyStatus.UNDER_REVIEW);

        // Se seu AuditDomain possuir setUpdatedBy/updatedAt, você pode preencher aqui:
        // if (request.getSubmittedBy() != null) policy.setUpdatedBy(request.getSubmittedBy());
        // policy.setUpdatedAt(OffsetDateTime.now());

        policyRepository.save(policy);

        // Log simples (recomendado registrar em uma tabela de auditoria própria)
        log.info("Policy {} submetida para revisão por {}. Comentário: {}",
                policyId, request.getSubmittedBy(), request.getComment());

        return SubmitReviewResponse.builder()
                .policyId(policy.getId())
                .previousStatus(previous)
                .currentStatus(policy.getPolicyStatus())
                .submittedBy(request.getSubmittedBy())
                .comment(request.getComment())
                .submittedAt(OffsetDateTime.now(ZoneOffset.UTC))
                .build();
    }

    @Transactional
    @Override
    public PolicyApproveResponse approve(UUID policyId, PolicyApproveRequest request) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Policy não encontrada"));

        PolicyStatus previous = policy.getPolicyStatus();
        if (previous != PolicyStatus.UNDER_REVIEW) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A policy precisa estar em IN_REVIEW para ser aprovada (status atual: " + previous + ")"
            );
        }

        // Transição de estado
        policy.setPolicyStatus(PolicyStatus.APPROVED);

        // Se seu AuditDomain tiver updatedBy/updatedAt, você pode preencher aqui:
        // if (request != null && request.getApprovedBy() != null) policy.setUpdatedBy(request.getApprovedBy());
        // policy.setUpdatedAt(OffsetDateTime.now());

        policyRepository.save(policy);

        log.info("Policy {} aprovada por {}. Comentário: {}",
                policyId, request != null ? request.getApprovedBy() : null,
                request != null ? request.getComment() : null);

        return PolicyApproveResponse.builder()
                .policyId(policy.getId())
                .previousStatus(previous)
                .currentStatus(policy.getPolicyStatus())
                .approvedBy(request != null ? request.getApprovedBy() : null)
                .comment(request != null ? request.getComment() : null)
                .approvedAt(OffsetDateTime.now(ZoneOffset.UTC))
                .build();
    }

    @Transactional
    @Override
    public PolicyPublishResponse publish(UUID policyId, PolicyPublishRequest request) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Policy não encontrada"));

        PolicyStatus previous = policy.getPolicyStatus();
        if (previous != PolicyStatus.APPROVED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A policy precisa estar em APPROVED para publicação (status atual: " + previous + ")"
            );
        }

        PolicyContent content = policyContentRepository.findByPolicyId(policyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                        "Conteúdo não encontrado para a policy. Gere o conteúdo antes de publicar."));

        // Formato desejado (default = manter o formato já salvo)
        String requested = (request != null && request.getFormat() != null)
                ? request.getFormat().toLowerCase(Locale.ROOT) : null;

        ContentFormat currentFmt = content.getFormat();
        ContentFormat targetFmt = requested == null ? currentFmt : parseFormat(requested);

        // Regras de publicação/transformação:
        // - Se quiser publicar no mesmo formato, ok.
        // - Se quiser PDF e você tem HTML salvo, tentamos converter (pdfRenderer obrigatório).
        // - Qualquer outra combinação -> 409 (para v1 simples).
        if (targetFmt == currentFmt) {
            // mantém como está
        } else if (targetFmt == ContentFormat.PDF && currentFmt == ContentFormat.HTML) {
            if (pdfRenderer == null) {
                throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,
                        "Publicação em PDF requer um PdfRenderer configurado.");
            }
            byte[] pdf = pdfRenderer.fromHtml(content.getContentMdHtml());
            content.setFormat(ContentFormat.PDF);
            content.setContentPdf(pdf);
            content.setContentMdHtml(null); // Nota: você perderá o HTML. Para manter ambos,
            // altere o modelo para permitir múltiplos formatos por policy.
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Conversão de " + currentFmt + " para " + targetFmt + " não suportada nesta versão.");
        }

        policyContentRepository.save(content);

        // Atualiza Policy como publicada e define URL "canônica" para download
        String downloadUrl = "/v1/policies/" + policyId + "/content?format=" + targetFmt.name().toLowerCase(Locale.ROOT);
        policy.setPolicyStatus(PolicyStatus.PUBLISHED);
        policy.setDocumentUrl(downloadUrl); // você pode trocar por URL externa (S3/MinIO) se usar storage externo
        policyRepository.save(policy);

        log.info("Policy {} publicada por {} no formato {}. Notas: {}",
                policyId,
                request != null ? request.getPublishedBy() : null,
                targetFmt,
                request != null ? request.getPublishNotes() : null);

        return PolicyPublishResponse.builder()
                .policyId(policy.getId())
                .previousStatus(previous)
                .currentStatus(policy.getPolicyStatus())
                .format(targetFmt.name().toLowerCase(Locale.ROOT))
                .downloadUrl(downloadUrl)
                .documentUrl(policy.getDocumentUrl())
                .publishedBy(request != null ? request.getPublishedBy() : null)
                .publishNotes(request != null ? request.getPublishNotes() : null)
                .publishedAt(OffsetDateTime.now(ZoneOffset.UTC))
                .build();
    }

    private ContentFormat parseFormat(String f) {
        return switch (f) {
            case "md" -> ContentFormat.MD;
            case "html" -> ContentFormat.HTML;
            case "pdf" -> ContentFormat.PDF;
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Formato inválido. Use md|html|pdf.");
        };
    }
}
