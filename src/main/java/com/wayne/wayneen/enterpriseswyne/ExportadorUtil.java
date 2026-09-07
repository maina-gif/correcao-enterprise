package com.wayne.wayneen.enterpriseswyne;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ExportadorUtil {

    // Exportar para PDF
    public static void gerarPDF(List<com.wayne.wayneen.enterpriseswyne.Documento> documentos, String destino) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(destino));
        document.open();

        Paragraph titulo = new Paragraph("Relatório de Documentos", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);
        document.add(new Paragraph(" "));

        PdfPTable tabela = new PdfPTable(4);
        tabela.setWidths(new float[]{1, 3, 2, 4});

        adicionarCelulaCabecalho(tabela, "ID");
        adicionarCelulaCabecalho(tabela, "Título");
        adicionarCelulaCabecalho(tabela, "Validade");
        adicionarCelulaCabecalho(tabela, "Caminho do Arquivo");

        for (com.wayne.wayneen.enterpriseswyne.Documento doc : documentos) {
            tabela.addCell(String.valueOf(doc.getId()));
            tabela.addCell(doc.getTitulo());
            tabela.addCell(doc.getDataValidade() != null ? doc.getDataValidade().toString() : "N/A");
            tabela.addCell(doc.getCaminhoArquivo());
        }

        document.add(tabela);
        document.close();
    }

    private static void adicionarCelulaCabecalho(PdfPTable tabela, String texto) {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setPhrase(new Phrase(texto));
        tabela.addCell(header);
    }

    // Exportar para Excel
    public static void gerarExcel(List<com.wayne.wayneen.enterpriseswyne.Documento> documentos, String destino) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream(destino)) {
            Sheet sheet = workbook.createSheet("Documentos");
            String[] colunas = {"ID", "Título", "Validade", "Caminho do Arquivo"};
            Row headerRow = sheet.createRow(0);

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            for (int i = 0; i < colunas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(colunas[i]);
                cell.setCellStyle(headerStyle);
            }

            int linha = 1;
            for (com.wayne.wayneen.enterpriseswyne.Documento doc : documentos) {
                Row row = sheet.createRow(linha++);
                row.createCell(0).setCellValue(doc.getId());
                row.createCell(1).setCellValue(doc.getTitulo());
                row.createCell(2).setCellValue(doc.getDataValidade() != null ? doc.getDataValidade().toString() : "N/A");
                row.createCell(3).setCellValue(doc.getCaminhoArquivo());
            }

            for (int i = 0; i < colunas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(fileOut);
        }
    }

    // Exportar para CSV
    public static void gerarCSV(List<com.wayne.wayneen.enterpriseswyne.Documento> documentos, String destino) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(destino))) {
            writer.write("ID,Título,Validade,Caminho do Arquivo");
            writer.newLine();

            for (com.wayne.wayneen.enterpriseswyne.Documento doc : documentos) {
                writer.write(String.format("%d,\"%s\",%s,\"%s\"",
                        doc.getId(),
                        doc.getTitulo(),
                        doc.getDataValidade() != null ? doc.getDataValidade() : "N/A",
                        doc.getCaminhoArquivo()));
                writer.newLine();
            }
        }
    }

    // Filtro: apenas documentos com validade vencida
    public static List<com.wayne.wayneen.enterpriseswyne.Documento> filtrarVencidos(List<com.wayne.wayneen.enterpriseswyne.Documento> documentos) {
        return documentos.stream()
                .filter(doc -> doc.getDataValidade() != null && doc.getDataValidade().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }

    // Exportar todos ao mesmo tempo
    public static void exportarTudo(List<com.wayne.wayneen.enterpriseswyne.Documento> documentos, String basePath) throws Exception {
        gerarPDF(documentos, basePath + "_documentos.pdf");
        gerarExcel(documentos, basePath + "_documentos.xlsx");
        gerarCSV(documentos, basePath + "_documentos.csv");
    }
}
