package com.wayne.wayneen.enterpriseswyne.model;

import com.wayne.wayneen.enterpriseswyne.Documento;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;

public class ExcelExportUtil {

    public static void gerarRelatorioExcel(List<Documento> documentos, String destino) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Documentos");

        // Cabeçalho
        String[] colunas = {"ID", "Título", "Validade", "Caminho do Arquivo"};
        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < colunas.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(colunas[i]);
        }

        // Dados
        int linha = 1;
        for (Documento doc : documentos) {
            Row row = sheet.createRow(linha++);
            row.createCell(0).setCellValue(doc.getId());
            row.createCell(1).setCellValue(doc.getTitulo());
            row.createCell(2).setCellValue(doc.getDataValidade() != null ? doc.getDataValidade().toString() : "N/A");
            row.createCell(3).setCellValue(doc.getCaminhoArquivo());
        }

        // Auto-ajuste de colunas
        for (int i = 0; i < colunas.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Salva arquivo
        FileOutputStream fileOut = new FileOutputStream(destino);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }
}
