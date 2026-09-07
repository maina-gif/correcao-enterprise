package com.wayne.wayneen.enterpriseswyne.model;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.wayne.wayneen.enterpriseswyne.model.Funcionario;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class RelatorioUtil {

    public static void gerarRelatorioPDF(File destino, List<Funcionario> funcionarios) {
        Document documento = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(documento, new FileOutputStream(destino));
            documento.open();

            // Título
            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph("Relatório de Funcionários", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            documento.add(titulo);

            // Tabela
            PdfPTable tabela = new PdfPTable(6);
            tabela.setWidthPercentage(100);
            tabela.setWidths(new int[]{2, 4, 3, 3, 3, 4});

            // Cabeçalho
            String[] cabecalhos = {"ID", "Nome", "CPF", "Cargo", "Departamento", "E-mail"};
            for (String cab : cabecalhos) {
                PdfPCell cell = new PdfPCell(new Phrase(cab));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                tabela.addCell(cell);
            }

            // Dados dos funcionários
            for (Funcionario f : funcionarios) {
                tabela.addCell(String.valueOf(f.getId()));
                tabela.addCell(f.getNomeCompleto());
                tabela.addCell(f.getCpf());
                tabela.addCell(f.getCargo());
                tabela.addCell(f.getDepartamento());
                tabela.addCell(f.getEmail());
            }

            documento.add(tabela);
            documento.close();

        } catch (Exception e) {
            e.printStackTrace();
            // Em vez de mostrar alerta aqui, trate a exceção no Controller se quiser exibir uma mensagem
        }
    }
}
