package com.wayne.wayneen.enterpriseswyne.model;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.wayne.wayneen.enterpriseswyne.Documento;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class CSVExportUtil {

    public static void gerarCSV(List<Documento> documentos, String caminhoDestino) throws Exception {
        try (PrintWriter writer = new PrintWriter(new FileWriter(caminhoDestino))) {
            // Cabeçalho
            writer.println("ID,Título,Tipo,Data de Validade,Caminho do Arquivo,Funcionário ID");

            for (Documento doc : documentos) {
                String linha = String.format("%d,%s,%s,%s,%s,%d",
                        doc.getId(),
                        sanitize(doc.getTitulo()),
                        sanitize(doc.getTipo()),
                        doc.getDataValidade() != null ? doc.getDataValidade().toString() : "",
                        sanitize(doc.getCaminhoArquivo()),
                        doc.getFuncionarioId()
                );
                writer.println(linha);
            }
        }
    }

    private static String sanitize(String texto) {
        return texto != null ? texto.replace(",", " ").replace("\n", " ") : "";
    }

    public static void gerarCSVSimples(List<Documento> documentos, String caminhoDestino) throws Exception {
        try (PrintWriter writer = new PrintWriter(new FileWriter(caminhoDestino))) {
            writer.println("Título,Data de Validade");
            for (Documento doc : documentos) {
                String linha = String.format("%s,%s",
                        sanitize(doc.getTitulo()),
                        doc.getDataValidade() != null ? doc.getDataValidade().toString() : "N/A"
                );
                writer.println(linha);
            }
        }
    }

    public static void gerarPDF(List<Documento> documentos, String caminhoDestino) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(caminhoDestino));
        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        document.add(new Paragraph("Relatório de Documentos", font));
        document.add(new Paragraph("\n"));

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.addCell("ID");
        table.addCell("Título");
        table.addCell("Tipo");
        table.addCell("Validade");
        table.addCell("Caminho");
        table.addCell("Funcionário ID");

        for (Documento doc : documentos) {
            table.addCell(String.valueOf(doc.getId()));
            table.addCell(sanitize(doc.getTitulo()));
            table.addCell(sanitize(doc.getTipo()));
            table.addCell(doc.getDataValidade() != null ? doc.getDataValidade().toString() : "");
            table.addCell(sanitize(doc.getCaminhoArquivo()));
            table.addCell(String.valueOf(doc.getFuncionarioId()));
        }

        document.add(table);
        document.close();
    }
}
