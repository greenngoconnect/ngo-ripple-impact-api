package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.risk.Risk;
import br.com.oakshield.oakshield.core.domain.risk.RiskCategory;
import br.com.oakshield.oakshield.core.domain.asset.Asset;

public interface RiskSuggestionService {
    boolean suggestRisksForAsset(Asset asset);

    Risk createRisk(Asset asset, String description, RiskCategory category);
}
