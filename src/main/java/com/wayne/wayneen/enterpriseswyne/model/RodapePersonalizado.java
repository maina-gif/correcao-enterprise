package com.wayne.wayneen.enterpriseswyne.model;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RodapePersonalizado extends PdfPageEventHelper {

    private final Font fontRodape = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, BaseColor.GRAY);
    private final Font fontAssinatura = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.DARK_GRAY);
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private PdfTemplate total;

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(30, 16);
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();

        // Rodapé: data
        String data = "Gerado em: " + sdf.format(new Date());
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(data, fontRodape),
                document.left(), document.bottom() - 10, 0);

        // Rodapé: página x
        String textoPagina = "Página " + writer.getPageNumber();
        ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, new Phrase(textoPagina, fontRodape),
                document.right(), document.bottom() - 10, 0);
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        try {
            PdfContentByte cb = writer.getDirectContent();
            Phrase assinatura = new Phrase("Assinado eletronicamente por: Wayne Enterprises", fontAssinatura);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, assinatura,
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom() - 35, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
