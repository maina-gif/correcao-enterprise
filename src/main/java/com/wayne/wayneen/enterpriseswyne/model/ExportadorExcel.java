package com.wayne.wayneen.enterpriseswyne.model;

import com.wayne.wayneen.enterpriseswyne.Evento;
import com.wayne.wayneen.enterpriseswyne.model.Funcionario;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportadorExcel {

    public static void exportar(List<Evento> eventos, String caminhoArquivo) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Eventos");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Row cabecalho = sheet.createRow(0);
        String[] colunas = {"Título", "Tipo", "Data", "Local", "Descrição"};

        for (int i = 0; i < colunas.length; i++) {
            cabecalho.createCell(i).setCellValue(colunas[i]);
        }

        int linha = 1;
        for (Evento evento : eventos) {
            Row row = sheet.createRow(linha++);
            row.createCell(0).setCellValue(evento.getTitulo());
            row.createCell(1).setCellValue(evento.getTipo());
            row.createCell(2).setCellValue(evento.getData().format(formatter));
            row.createCell(3).setCellValue(evento.getLocal());
            row.createCell(4).setCellValue(evento.getDescricao());
        }

        for (int i = 0; i < colunas.length; i++) {
            sheet.autoSizeColumn(i);
        }

        FileOutputStream out = new FileOutputStream(caminhoArquivo);
        workbook.write(out);
        out.close();
        workbook.close();
    }

    public static void exportar(ObservableList<Funcionario> items, String absolutePath) {
    }
}
