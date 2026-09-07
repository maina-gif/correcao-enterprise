package com.wayne.wayneen.enterpriseswyne.model;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.wayne.wayneen.enterpriseswyne.DAO.FeriasDAO;
import javafx.collections.ObservableList;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PDFGenerator {

    private static final DateTimeFormatter BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void gerar(ObservableList<Funcionario> lista, String caminho) {
        if (lista == null || lista.isEmpty()) return;

        Document doc = new Document();
        try (FileOutputStream fos = new FileOutputStream(caminho)) {
            PdfWriter.getInstance(doc, fos);
            doc.addAuthor("Wayne Enterprises");
            doc.addTitle("Relatório de Funcionários");
            doc.addCreationDate();
            doc.open();

            Font titulo = new Font(FontFamily.HELVETICA, 16, Font.BOLD);
            doc.add(new Paragraph("Relatório de Funcionários", titulo));
            doc.add(new Paragraph(" "));

            PdfPTable tabela = new PdfPTable(8);
            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[]{3f, 3f, 2f, 2f, 4f, 2f, 2f, 4f});

            Font cabecalhoFont = new Font(FontFamily.HELVETICA, 10, Font.BOLD);
            addHeaderCell(tabela, "Nome", cabecalhoFont);
            addHeaderCell(tabela, "CPF", cabecalhoFont);
            addHeaderCell(tabela, "Cargo", cabecalhoFont);
            addHeaderCell(tabela, "Departamento", cabecalhoFont);
            addHeaderCell(tabela, "E-mail", cabecalhoFont);
            addHeaderCell(tabela, "Admissão", cabecalhoFont);
            addHeaderCell(tabela, "Nascimento", cabecalhoFont);
            addHeaderCell(tabela, "Férias (todos os períodos)", cabecalhoFont);
            tabela.setHeaderRows(1);

            Font conteudoFont = new Font(FontFamily.HELVETICA, 10, Font.NORMAL);

            for (Funcionario f : lista) {
                addCell(tabela, safe(f != null ? f.getNomeCompleto() : null), conteudoFont);
                addCell(tabela, safe(f != null ? f.getCpf() : null), conteudoFont);
                addCell(tabela, safe(f != null ? f.getCargo() : null), conteudoFont);
                addCell(tabela, safe(f != null ? f.getDepartamento() : null), conteudoFont);
                addCell(tabela, safe(f != null ? f.getEmail() : null), conteudoFont);
                addCell(tabela, formatar(f != null ? f.getDataAdmissao().toLocalDate() : null), conteudoFont);
                addCell(tabela, formatar(f != null ? f.getDataNascimento() : null), conteudoFont);

                // Todos os períodos de férias (concat)
                String feriasConcat = "-";
                try {
                    List<Ferias> periodos = (f != null) ? FeriasDAO.buscarTodosPorFuncionarioId(f.getId()) : null;
                    if (periodos != null && !periodos.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < periodos.size(); i++) {
                            Ferias fer = periodos.get(i);
                            String ini = formatar(fer != null ? fer.getDataInicio() : null);
                            String fim = formatar(fer != null ? fer.getDataFim() : null);
                            if (i > 0) sb.append("; ");
                            sb.append(ini).append(" a ").append(fim);
                        }
                        feriasConcat = sb.toString();
                    } else {
                        feriasConcat = "-";
                    }
                } catch (Exception ex) {
                    feriasConcat = "Erro ao consultar";
                }
                addCell(tabela, feriasConcat, conteudoFont);
            }

            doc.add(tabela);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (doc.isOpen()) doc.close();
        }
    }

    // ===== Helpers =====
    private static void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
    }

    private static void addCell(PdfPTable table, String text, Font font) {
        table.addCell(new Phrase(safe(text), font));
    }

    private static String safe(String s) {
        return (s == null || s.isBlank()) ? "-" : s;
    }

    private static String formatar(LocalDate data) {
        return (data != null) ? BR.format(data) : "-";
    }
}
