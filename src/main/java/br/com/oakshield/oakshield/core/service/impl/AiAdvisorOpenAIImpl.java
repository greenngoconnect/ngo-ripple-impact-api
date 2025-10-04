package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.audit.AuditAdvice;
import br.com.oakshield.oakshield.core.domain.audit.AuditFinding;
import br.com.oakshield.oakshield.core.domain.audit.BusinessSeverity;
import br.com.oakshield.oakshield.core.service.AiAdvisorOpenAI;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiAdvisorOpenAIImpl implements AiAdvisorOpenAI {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Value("${openai.enabled}")
    private boolean isOpenAiEnabled;

    @Value("${openai.model:gpt-4o-mini}") // você pode trocar para gpt-4.1, etc.
    private String openAiModel;

    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper mapper;

    private final AuditFindingRepository auditFindingRepository;
    private final AuditAdviceRepository auditAdviceRepository;

    @Override
    public AuditAdvice analyze(AuditFinding auditFinding, List<String> verdicts) {

        if (!isOpenAiEnabled || openAiApiKey == null || openAiApiKey.isBlank()) {
            if (!isOpenAiEnabled) {
                log.info("OpenAI desabilitada. Usando fallback para regras estáticas.");
            } else {
                log.warn("Chave da OpenAI não configurada. Usando fallback.");
            }
            AuditAdvice advice = fallbackAdvice();
            linkAndPersist(auditFinding, advice);
            return advice;
        }

        try {
            // Monta request body (chat.completions) com system + user e response_format=json
            ObjectNode root = mapper.createObjectNode();
            root.put("model", openAiModel);
            root.set("messages", buildMessages(auditFinding, verdicts));
            root.put("temperature", 0.0);

            // Força JSON puro como saída
            ObjectNode responseFormat = mapper.createObjectNode();
            responseFormat.put("type", "json_object");
            root.set("response_format", responseFormat);

            // Faz a chamada
            String response = webClientBuilder.build()
                    .post()
                    .uri("https://api.openai.com/v1/chat/completions")
                    .header("Authorization", "Bearer " + openAiApiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(root.toString())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.debug("OpenAI raw response: {}", response);

            // Extrai o content (que deve ser um JSON)
            String content = extractChatContent(response);
            String cleanJson = sanitizeJsonFence(content);

            log.debug("OpenAI content (sanitized): {}", cleanJson);

            // Converte o JSON recebido para AuditAdvice (mapeando enum com segurança)
            AuditAdvice auditAdvice = toAuditAdvice(cleanJson);
            linkAndPersist(auditFinding, auditAdvice);
            return auditAdvice;
        } catch (Exception e) {
            log.error("Falha na chamada à OpenAI: {}", e.getMessage(), e);
            return fallbackAdvice();
        }
    }

    /* =========================================================
       Auxiliares
       ========================================================= */

    private String toJson(Object o) {
        try { return mapper.writeValueAsString(o); }
        catch (Exception e) { return "{}"; }
    }

    /** Mensagem SYSTEM com as regras e formato do JSON */
    private String buildPromptSystem() {
        return """
            Você é um auditor de conformidade em nuvem.
            Gere SOMENTE um JSON com as chaves:
            {
              "businessSeverity": "LOW|MEDIUM|HIGH|CRITICAL",
              "explanationPt": "string (máx 120 palavras)",
              "remediationSteps": ["passo1", "passo2"],
              "shouldOpenTicket": true|false
            }
            Regras: não invente fatos fora do achado; seja objetivo e seguro.
            """;
    }

    /** Mensagem USER com o achado e o contexto */
    private String buildPromptUser(AuditFinding auditFinding, List<String> verdicts) {
        return """
            Achado normalizado:
            %s

            Violações internas:
            %s

            Contexto: LGPD, ISO 27001 (mínimo privilégio, controle de acesso), ambiente=%s.
            """.formatted(toJson(auditFinding),
                String.join("; ", verdicts != null ? verdicts : List.of()),
                auditFinding != null ? auditFinding.getEnvironment() : "desconhecido");
    }

    /** Monta o array de mensagens (system + user) no formato da Chat API */
    private ArrayNode buildMessages(AuditFinding auditFinding, List<String> verdicts) {
        ArrayNode messages = mapper.createArrayNode();

        ObjectNode systemMsg = mapper.createObjectNode();
        systemMsg.put("role", "system");
        systemMsg.put("content", buildPromptSystem());
        messages.add(systemMsg);

        ObjectNode userMsg = mapper.createObjectNode();
        userMsg.put("role", "user");
        userMsg.put("content", buildPromptUser(auditFinding, verdicts));
        messages.add(userMsg);

        return messages;
    }

    /** Extrai choices[0].message.content do retorno do chat.completions */
    private String extractChatContent(String rawResponse) throws Exception {
        JsonNode root = mapper.readTree(rawResponse);
        JsonNode choices = root.path("choices");
        if (!choices.isArray() || choices.isEmpty()) {
            throw new IllegalStateException("Resposta sem 'choices'.");
        }
        JsonNode content = choices.get(0).path("message").path("content");
        if (content.isMissingNode() || content.asText().isBlank()) {
            throw new IllegalStateException("Resposta sem 'message.content'.");
        }
        return content.asText();
    }

    /** Remove cercas de código ```json ... ``` e espaços extras */
    private String sanitizeJsonFence(String s) {
        String trimmed = s.trim();
        if (trimmed.startsWith("```")) {
            // remove a primeira linha ```json ou ``` e a última ```
            trimmed = trimmed.replaceFirst("^```[a-zA-Z]*\\s*", "");
            int idx = trimmed.lastIndexOf("```");
            if (idx >= 0) trimmed = trimmed.substring(0, idx);
        }
        return trimmed.trim();
    }

    /** Converte o JSON (String) em AuditAdvice, mapeando enum/fields com segurança */
    private AuditAdvice toAuditAdvice(String json) throws Exception {
        JsonNode n = mapper.readTree(json);

        String sevStr = n.path("businessSeverity").asText("MEDIUM");
        BusinessSeverity severity;
        try {
            severity = BusinessSeverity.valueOf(sevStr.toUpperCase());
        } catch (Exception ignore) {
            severity = BusinessSeverity.MEDIUM;
        }

        String explanation = n.path("explanationPt").asText("");
        List<String> steps = new ArrayList<>();
        if (n.has("remediationSteps") && n.get("remediationSteps").isArray()) {
            for (JsonNode step : n.get("remediationSteps")) {
                steps.add(step.asText());
            }
        }
        boolean openTicket = n.path("shouldOpenTicket").asBoolean(false);

        AuditAdvice advice = new AuditAdvice();
        advice.setBusinessSeverity(severity);
        advice.setExplanationPt(explanation);
        advice.setRemediationSteps(steps);
        advice.setShouldOpenTicket(openTicket);
        return advice;
    }

    /** Fallback seguro para não quebrar o fluxo */
    private AuditAdvice fallbackAdvice() {
        AuditAdvice fallback = new AuditAdvice();
        fallback.setBusinessSeverity(BusinessSeverity.MEDIUM);
        fallback.setExplanationPt("Falha ao consultar a IA. Resultado gerado por fallback seguro.");
        fallback.setRemediationSteps(List.of("Revisar configuração conforme política interna"));
        fallback.setShouldOpenTicket(false);
        return fallback;
    }

    /* ===================== Persistência ===================== */

    /** Vincula advice↔finding e salva ambos em uma transação. */
    private void linkAndPersist(AuditFinding finding, AuditAdvice advice) {
        // dica: se o relacionamento Advice -> Finding tiver cascade, um save bastaria
        AuditFinding auditFinding = auditFindingRepository.save(finding);
        advice.setAuditFinding(auditFinding);
        auditAdviceRepository.save(advice);
    }
}
