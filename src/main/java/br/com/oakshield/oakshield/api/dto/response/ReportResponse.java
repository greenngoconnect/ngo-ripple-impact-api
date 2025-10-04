package br.com.oakshield.oakshield.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse implements Serializable {
    private String title;
    private LocalDate generatedAt;
    private List<String> headers;
    private List<List<String>> data;
    private String generatedBy;
}
