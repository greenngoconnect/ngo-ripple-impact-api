package br.com.oakshield.oakshield.utils;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.io.ByteArrayOutputStream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PdfReportUtil {

    public static byte[] buildCompliancePdf(ComplianceReportResponse r) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(doc, baos);
            doc.addTitle("Relatório de Conformidade");
            doc.open();

            Font title = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font normal = new Font(Font.HELVETICA, 10, Font.NORMAL);
            Font bold = new Font(Font.HELVETICA, 10, Font.BOLD);

            // Cabeçalho
            doc.add(new Paragraph("Relatório de Conformidade", title));
            doc.add(new Paragraph("Projeto: " + r.project(), normal));
            doc.add(new Paragraph("Sprint: " + r.sprint(), normal));
            doc.add(new Paragraph("Período: " + r.periodFrom() + " a " + r.periodTo(), normal));
            doc.add(new Paragraph("Gerado em: " + r.generatedAt(), normal));
            doc.add(Chunk.NEWLINE);

            // Resumo
            doc.add(new Paragraph("Resumo", bold));
            String resumo = "Score de Conformidade: " + r.complianceScore() + "%  |  Totais por Severidade: " + r.totalsBySeverity();
            doc.add(new Paragraph(resumo, normal));
            doc.add(Chunk.NEWLINE);

            // Tabela de itens
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{14, 16, 16, 18, 12, 24});

            addHeader(table, "Severity");
            addHeader(table, "Service/Rule");
            addHeader(table, "Resource");
            addHeader(table, "Env");
            addHeader(table, "Pessoais?");
            addHeader(table, "Remediação (1º passo)");

            for (var it : r.items()) {
                table.addCell(new PdfPCell(new Phrase(String.valueOf(it.businessSeverity()), normal)));
                table.addCell(new PdfPCell(new Phrase(it.service() + " / " + it.ruleId(), normal)));
                table.addCell(new PdfPCell(new Phrase(it.resourceName(), normal)));
                table.addCell(new PdfPCell(new Phrase(it.environment(), normal)));
                table.addCell(new PdfPCell(new Phrase(it.involvesPersonalData() ? "Sim" : "Não", normal)));
                String firstStep = (it.remediationSteps() != null && !it.remediationSteps().isEmpty())
                        ? it.remediationSteps().get(0) : "-";
                table.addCell(new PdfPCell(new Phrase(firstStep, normal)));
            }
            doc.add(table);

            doc.add(Chunk.NEWLINE);
            doc.add(new Paragraph("Notas / Lições:", bold));
            doc.add(new Paragraph(r.notes(), normal));

            doc.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF de conformidade", e);
        }
    }

    private static void addHeader(PdfPTable table, String txt) {
        Font header = new Font(Font.HELVETICA, 10, Font.BOLD);
        PdfPCell cell = new PdfPCell(new Phrase(txt, header));
        cell.setGrayFill(0.9f);
        table.addCell(cell);
    }
}
