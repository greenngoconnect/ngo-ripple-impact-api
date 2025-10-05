package br.com.greenngoconnect.rippleimpact.api.dto.request;

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
@Schema(description = "Generate Request", name = "DataSubjectRequests")
@Tag(name = "Generate Request", description = "Operations related to DataSubjectRequests")
public class GenerateRequest implements Serializable {
    private String templateCode;
    private String language;
    private Map<String, Object> variables;
    private String outputFormat = "md"; // md|html
    private boolean autoFillFromOrg = true;
    private boolean autoMapCompliance = true;
}
