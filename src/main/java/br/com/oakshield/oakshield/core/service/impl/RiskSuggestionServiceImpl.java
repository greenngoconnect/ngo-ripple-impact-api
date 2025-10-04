package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.risk.*;
import br.com.oakshield.oakshield.core.domain.asset.Asset;
import br.com.oakshield.oakshield.core.service.RiskSuggestionService;
import com.fasterxml.jackson.core.type.TypeReference;
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
public class RiskSuggestionServiceImpl implements RiskSuggestionService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Value("${openai.enabled}")
    private boolean isOpenAiEnabled;

    private final WebClient.Builder webClientBuilder;
    private final RiskRepository riskRepository;
    private final ObjectMapper objectMapper;

    @Override
    public boolean suggestRisksForAsset(Asset asset) {
        if (isOpenAiEnabled) {
            return suggestRisksForAssetUsingOpenAI(asset);
        } else {
            return suggestRisksForAssetWithoutAI(asset);
        }
    }

    private List<RiskRule> getRiskRules() {
        return List.of(
                new RiskRule(s -> s.contains("servidor") && s.contains("banco"),
                        "Vazamento de dados", RiskCategory.TECHNICAL),
                new RiskRule(s -> s.contains("documento") || s.contains("contrato"),
                        "Acesso indevido", RiskCategory.OPERATIONAL),
                new RiskRule(s -> s.contains("rede") || s.contains("firewall"),
                        "Ataque DoS", RiskCategory.TECHNICAL),
                new RiskRule(s -> s.contains("pessoa") || s.contains("funcionário"),
                        "Erro humano", RiskCategory.OPERATIONAL),
                new RiskRule(s -> s.contains("backup") || s.contains("recuperação"),
                        "Falha no backup", RiskCategory.DATA),
                new RiskRule(s -> s.contains("cloud") || s.contains("nuvem"),
                        "Risco em nuvem", RiskCategory.TECHNICAL),
                new RiskRule(s -> s.contains("mobile") || s.contains("dispositivo móvel"),
                        "Risco em dispositivos móveis", RiskCategory.TECHNICAL),
                new RiskRule(s -> s.contains("internet") || s.contains("iot"),
                        "Risco em IoT", RiskCategory.TECHNICAL),
                new RiskRule(s -> s.contains("email") || s.contains("comunicação eletrônica"),
                        "Phishing e engenharia social", RiskCategory.OPERATIONAL),
                new RiskRule(s -> s.contains("plano de continuidade") || s.contains("recuperação de desastres"),
                        "Falha no plano de continuidade", RiskCategory.GOVERNANCE),
                new RiskRule(s -> s.contains("treinamento") || s.contains("capacitação"),
                        "Falta de conscientização", RiskCategory.OPERATIONAL),
                new RiskRule(s -> s.contains("fornecedor") || s.contains("terceiro"),
                        "Risco de terceiros", RiskCategory.OPERATIONAL),
                new RiskRule(s -> s.contains("rede") || s.contains("infraestrutura de rede"),
                        "Configuração inadequada da rede", RiskCategory.TECHNICAL),
                new RiskRule(s -> s.contains("físico") || s.contains("instalação"),
                        "Acesso físico não autorizado", RiskCategory.PHYSICAL),
                new RiskRule(s -> s.contains("política") || s.contains("procedimento"),
                        "Não conformidade com políticas", RiskCategory.GOVERNANCE),
                new RiskRule(s -> s.contains("dados") || s.contains("informação"),
                        "Perda de integridade dos dados", RiskCategory.DATA)
        );
    }

    private boolean suggestRisksForAssetWithoutAI(Asset asset) {
        try {
            List<Risk> suggestedRisks = new ArrayList<>();

            String assetTypeName = asset.getName().toLowerCase();

            List<Risk> risks = getRiskRules().stream()
                    .filter(rule -> rule.condition.test(assetTypeName))
                    .map(rule -> createRisk(asset, rule.description, rule.category))
                    .toList();

            riskRepository.saveAll(suggestedRisks);
            return true;
        } catch (Exception e) {
            log.error("Error suggesting risks for asset {}: {}", asset.getName(), e.getMessage());
            return false;
        }
    }

    private boolean suggestRisksForAssetUsingOpenAI(Asset asset) {
        try {
            if (openAiApiKey == null || openAiApiKey.isEmpty()) {
                log.warn("Chave da OpenAI não configurada. Usando fallback para regras estáticas.");
                return suggestRisksForAssetWithoutAI(asset);
            }

            String prompt = buildPrompt(asset);
            log.debug("Prompt enviado ao OpenAI: {}", prompt);

            // Monta a estrutura JSON corretamente com Jackson (ObjectNode)
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = mapper.createObjectNode();
            root.put("model", "gpt-4");

            ArrayNode messages = mapper.createArrayNode();
            ObjectNode message = mapper.createObjectNode();
            message.put("role", "user");
            message.put("content", prompt);
            messages.add(message);

            root.set("messages", messages);
            root.put("temperature", 0.7);

            // Envia para OpenAI com JSON estruturado
            String response = webClientBuilder.build()
                    .post()
                    .uri("https://api.openai.com/v1/chat/completions")
                    .header("Authorization", "Bearer " + openAiApiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(mapper.writeValueAsString(root))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("OpenAI raw response: {}", response);

            // Extrai apenas o JSON retornado no campo content
            String content = extractContentFromResponse(response);
            log.debug("Resposta content extraído: {}", content);

            List<Risk> risks = objectMapper.readValue(content, new TypeReference<>() {});
            risks.forEach(risk -> {
                risk.setAsset(asset);
                risk.setSuggestedByAi(true);
            });

            riskRepository.saveAll(risks);
            log.info("Riscos sugeridos pela OpenAI para o ativo '{}': {}", asset.getName(), risks);
            return true;

        } catch (Exception e) {
            log.error("Erro ao sugerir riscos via OpenAI para o ativo '{}': {}", asset.getName(), e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Risk createRisk(Asset asset, String description, RiskCategory category) {
        RiskLevel probability = RiskLevel.MEDIUM;
        RiskLevel impact = RiskLevel.HIGH;

        return Risk.builder()
                .asset(asset)
                .description(description)
                .category(category)
                .probability(probability)
                .impact(impact)
                .riskScore(probability.ordinal() * impact.ordinal()) // ou regra mais avançada
                .treatmentPlan("Avaliação necessária")
                .riskStatus(RiskStatus.HIGH)
                .build();
    }

    private String buildPrompt(Asset asset) {
        return String.format("""
                Dado o ativo: '%s', Asset Type: '%s', classificação: '%s'.
                Quais riscos de segurança da informação este ativo pode representar de acordo com a ISO/IEC 27001?
                Responda no formato JSON com os campos:
                - description
                - category (ex: TECHNICAL, OPERATIONAL, ORGANIZATIONAL, LEGAL)
                - probability (LOW, MEDIUM, HIGH)
                - impact (LOW, MEDIUM, HIGH)
                - treatmentPlan
                - riskStatus (sempre "SUGGESTED")
                """, asset.getName(), asset.getAssetType(), asset.getClassification());
    }

    private String extractContentFromResponse(String json) {
        try {
            return objectMapper.readTree(json)
                    .get("choices").get(0)
                    .get("message").get("content").asText();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao extrair conteúdo do OpenAI", e);
        }
    }
}
