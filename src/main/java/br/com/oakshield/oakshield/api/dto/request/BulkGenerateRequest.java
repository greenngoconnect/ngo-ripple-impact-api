package br.com.oakshield.oakshield.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Schema(description = "DataSubjectRequest Entity", name = "DataSubjectRequests")
@Tag(name = "DataSubjectRequest Entity", description = "Operations related to DataSubjectRequests")
public class BulkGenerateRequest implements Serializable {
    private String templateCode;
    private String language;
    private Map<String, Object> variables;
    private String outputFormat = "md"; // md|html
    private boolean autoFillFromOrg = true;
    private boolean autoMapCompliance = true;
    private int quantity = 1; // Número de políticas a gerar
}
