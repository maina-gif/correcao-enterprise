package com.wayne.wayneen.enterpriseswyne.model;



import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.wayne.wayneen.enterpriseswyne.Evento;
import javafx.collections.ObservableList;

import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportadorPDF {

    public static void exportar(List<Evento> eventos, String caminhoArquivo) throws Exception {
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream(caminhoArquivo));
        doc.open();

        Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font corpoFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        doc.add(new Paragraph("Relatório de Eventos", tituloFont));
        doc.add(new Paragraph(" "));

        PdfPTable tabela = new PdfPTable(5);
        tabela.setWidths(new int[]{2, 2, 2, 2, 3});
        tabela.setWidthPercentage(100);

        tabela.addCell("Título");
        tabela.addCell("Tipo");
        tabela.addCell("Data");
        tabela.addCell("Local");
        tabela.addCell("Descrição");

        for (Evento evento : eventos) {
            tabela.addCell(evento.getTitulo());
            tabela.addCell(evento.getTipo());
            tabela.addCell(evento.getData().format(formatador));
            tabela.addCell(evento.getLocal());
            tabela.addCell(evento.getDescricao());
        }

        doc.add(tabela);
        doc.close();
    }

    public static void exportar(ObservableList<Funcionario> items, String absolutePath) {
    }
}
