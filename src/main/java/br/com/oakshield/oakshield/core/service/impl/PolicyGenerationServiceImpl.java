package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.api.dto.request.GenerateRequest;
import br.com.oakshield.oakshield.core.domain.policy.*;
import br.com.oakshield.oakshield.core.service.PolicyGenerationService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PolicyGenerationServiceImpl implements PolicyGenerationService {

    private final PolicyRepository policyRepository;
    private final PolicyTemplateRepository templateRepository;
    private final PolicyContentRepository policyContentRepository;
    private final Configuration freemarkerConfig;

    @Override
    public PreviewResponse preview(PreviewRequest req) {
        PolicyTemplate tpl = templateRepository
                .findFirstByCodeAndLanguageAndTemplateStatus(req.getTemplateCode(), req.getLanguage(), TemplateStatus.ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("Template não encontrado/ativo"));

        Map<String, Object> ctx = new HashMap<>();
        if (req.getVariables() != null) ctx.putAll(req.getVariables());
        // TODO: autoFillFromOrg, riscos, controles

        String rendered = renderFreeMarker(tpl.getBodyTemplate(), ctx);
        List<Map<String,String>> controls = extractControls(tpl);

        return PreviewResponse.builder()
                .rendered(rendered)
                .controls(controls)
                .build();
    }

    @Override
    public UUID generate(GenerateRequest req) {
        // Para simplificar, este método apenas renderiza e retornaria um ID fictício.
        // No seu projeto, persista Policy, PolicyVariables e ComplianceMapping.

        // 1) Localiza o template ativo
        Optional<PolicyTemplate> tpl = templateRepository
                .findFirstByCodeAndLanguageAndTemplateStatus(req.getTemplateCode(), req.getLanguage(), TemplateStatus.ACTIVE);
        if (tpl.isEmpty()) {
            throw new IllegalArgumentException("Template não encontrado/ativo");
        }
                //.orElseThrow(() -> new IllegalArgumentException("Template não encontrado/ativo"));

        // 2) Prepara o contexto (vars manuais + futuras auto-fill)
        Map<String, Object> ctx = new HashMap<>();
        if (req.getVariables() != null) ctx.putAll(req.getVariables());

        // 3) Renderiza (md/html)
        String rendered = renderInline(tpl.get().getBodyTemplate(), ctx);

        // 4) Monta os dados da sua Policy (nome, descrição, validade, status)
        String orgName = Objects.toString(
                ctx.getOrDefault("org_name", ctx.getOrDefault("org.name", "")), "");
        String baseTitle = (tpl.get().getTitle() != null && !tpl.get().getTitle().isBlank())
                ? tpl.get().getTitle() : tpl.get().getCode();
        String policyName = orgName.isBlank() ? baseTitle : baseTitle + " – " + orgName;

        LocalDate validUntil = null;
        Object v = ctx.getOrDefault("policy_valid_until", ctx.get("validUntil"));
        if (v != null && !v.toString().isBlank()) {
            validUntil = LocalDate.parse(v.toString()); // esperar ISO-8601 "2026-12-31"
        }

        Policy policy = Policy.builder()
                .name(policyName)
                .description("Gerada a partir do template " + tpl.get().getCode() + " v" + tpl.get().getVersion())
                .documentUrl(null)   // se for salvar em S3, preencha aqui
                .validUntil(validUntil)
                .policyStatus(PolicyStatus.DRAFT) // seu enum
                .build();

        // 5) Salva a Policy
        policy = policyRepository.save(policy);

        // 6) Salva o conteúdo renderizado (ou envie para S3 e preencha documentUrl)
        ContentFormat fmt = switch (String.valueOf(req.getOutputFormat()).toLowerCase()) {
            case "html" -> ContentFormat.HTML;
            case "pdf"  -> ContentFormat.PDF; // precisa de renderer; abaixo mantém null
            default     -> ContentFormat.MD;
        };

        PolicyContent content = PolicyContent.builder()
                .policy(policy)
                .format(fmt)
                .contentMdHtml(fmt == ContentFormat.PDF ? null : rendered)
                .contentPdf(null) // se implementar PDF, gere bytes e salve aqui
                .build();
        policyContentRepository.save(content);

        // 7) (Opcional) Mapear compliance com base no controlsRefsJson do template
        // if (req.isAutoMapCompliance()
        //     && tpl.getControlsRefsJson() != null
        //     && !tpl.getControlsRefsJson().isBlank()) {
        //   List<Map<String, String>> refs = objectMapper.readValue(
        //       tpl.getControlsRefsJson(), new TypeReference<>() {});
        //   for (Map<String, String> r : refs) {
        //     complianceMappingRepository.save(ComplianceMapping.builder()
        //         .policyId(policy.getId())
        //         .framework(r.get("framework"))
        //         .clause(r.get("clause"))
        //         .sectionAnchor(r.get("section"))
        //         .build());
        //   }
        // }

        return policy.getId();
    }

    @Transactional
    @Override
    public PolicyContentResponse getContent(UUID id) {
        PolicyContent c = policyContentRepository.findByPolicyId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return switch (c.getFormat()) {
            case MD, HTML -> {
                if (c.getContentMdHtml() == null || c.getContentMdHtml().isBlank()) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conteúdo não disponível.");
                }
                yield PolicyContentResponse.builder()
                        .policyId(c.getPolicy().getId())
                        .format(c.getFormat().name())
                        .contentMdHtml(c.getContentMdHtml())
                        .build();
            }
            case PDF -> {
                if (c.getContentPdf() == null || c.getContentPdf().length == 0) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conteúdo não disponível.");
                }
                yield PolicyContentResponse.builder()
                        .policyId(c.getPolicy().getId())
                        .format(c.getFormat().name())
                        .contentPdf(c.getContentPdf())
                        .build();
            }
        };
    }

    private String renderFreeMarker(String inlineTemplate, Map<String, Object> ctx) {
        try {
            Template t = new Template("inline", new StringReader(inlineTemplate), freemarkerConfig);
            return FreeMarkerTemplateUtils.processTemplateIntoString(t, ctx);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException("Erro renderizando template: " + e.getMessage(), e);
        }
    }

    private List<Map<String,String>> extractControls(PolicyTemplate tpl) {
        if (tpl.getControlsRefsJson() == null || tpl.getControlsRefsJson().isBlank()) return List.of();
        // Mantemos simples: o frontend/cliente pode enviar um JSON array, mas aqui retornamos vazio ou um dummy.
        return List.of(
                Map.of("framework","ISO27001","clause","A.9","section","Access Control")
        );
    }

    private String renderInline(String ftTemplate, Map<String, Object> ctx) {
        try {
            Template t = new Template("inline", new StringReader(ftTemplate), freemarkerConfig);
            return FreeMarkerTemplateUtils.processTemplateIntoString(t, ctx);
        } catch (Exception e) {
            throw new RuntimeException("Erro renderizando template: " + e.getMessage(), e);
        }
    }
}
