package com.wayne.wayneen.enterpriseswyne.model;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.wayne.wayneen.enterpriseswyne.Documento;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

public class PdfExportUtil {

    public static void gerarRelatorio(List<Documento> documentos, String destino) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(destino));
        document.open();

        adicionarCabecalho(document);
        document.add(new Paragraph(" ")); // Espaço
        adicionarTabela(document, documentos);

        document.close();
    }

    private static void adicionarCabecalho(Document document) throws Exception {
        PdfPTable tabelaCabecalho = new PdfPTable(2);
        tabelaCabecalho.setWidthPercentage(100);
        tabelaCabecalho.setWidths(new int[]{1, 4});

        // Logo (ajuste o caminho conforme a estrutura do seu projeto)
        InputStream logoStream = PdfExportUtil.class.getResourceAsStream("/images/logo.png");
        if (logoStream != null) {
            Image logo = Image.getInstance(javax.imageio.ImageIO.read(logoStream), null);
            logo.scaleAbsolute(60, 60);
            PdfPCell cellLogo = new PdfPCell(logo, false);
            cellLogo.setBorder(Rectangle.NO_BORDER);
            tabelaCabecalho.addCell(cellLogo);
        } else {
            PdfPCell vazio = new PdfPCell(new Phrase(" "));
            vazio.setBorder(Rectangle.NO_BORDER);
            tabelaCabecalho.addCell(vazio);
        }

        // Título
        Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
        Paragraph titulo = new Paragraph("Relatório de Documentos", fontTitulo);
        titulo.setAlignment(Element.ALIGN_LEFT);

        PdfPCell cellTitulo = new PdfPCell(titulo);
        cellTitulo.setBorder(Rectangle.NO_BORDER);
        cellTitulo.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tabelaCabecalho.addCell(cellTitulo);

        document.add(tabelaCabecalho);
    }

    private static void adicionarTabela(Document document, List<Documento> documentos) throws Exception {
        PdfPTable tabela = new PdfPTable(4);
        tabela.setWidthPercentage(100);
        tabela.setSpacingBefore(10f);
        tabela.setWidths(new float[]{1f, 3f, 2f, 4f});

        adicionarCabecalhosTabela(tabela);
        adicionarLinhasTabela(tabela, documentos);

        document.add(tabela);
    }

    private static void adicionarCabecalhosTabela(PdfPTable tabela) {
        String[] colunas = {"ID", "Título", "Validade", "Caminho do Arquivo"};
        for (String coluna : colunas) {
            PdfPCell cell = new PdfPCell(new Phrase(coluna, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabela.addCell(cell);
        }
    }

    private static void adicionarLinhasTabela(PdfPTable tabela, List<Documento> documentos) {
        Font fontLinha = new Font(Font.FontFamily.HELVETICA, 11);

        for (Documento doc : documentos) {
            tabela.addCell(new PdfPCell(new Phrase(String.valueOf(doc.getId()), fontLinha)));
            tabela.addCell(new PdfPCell(new Phrase(doc.getTitulo(), fontLinha)));
            tabela.addCell(new PdfPCell(new Phrase(doc.getDataValidade() != null ? doc.getDataValidade().toString() : "N/A", fontLinha)));
            tabela.addCell(new PdfPCell(new Phrase(doc.getCaminhoArquivo(), fontLinha)));
        }
    }

    public static void gerarPDF(List<Documento> docs, String s) {
    }
}
