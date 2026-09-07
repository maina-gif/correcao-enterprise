// ExportacaoController.java - Controller completo para importar e exportar documentos

package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.DocumentoDAO;
import com.wayne.wayneen.enterpriseswyne.Documento;
import com.wayne.wayneen.enterpriseswyne.model.CSVExportUtil;
import com.wayne.wayneen.enterpriseswyne.model.ExcelExportUtil;
import com.wayne.wayneen.enterpriseswyne.model.PdfExportUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class ExportacaoController {

    @FXML private javafx.scene.control.TextField campoTitulo;
    @FXML private javafx.scene.control.ComboBox<String> comboTipo;
    @FXML private javafx.scene.control.DatePicker campoValidade;
    @FXML private javafx.scene.control.TextField campoFuncionarioId;
    @FXML private javafx.scene.control.Label labelArquivoSelecionado;
    @FXML private javafx.scene.control.TextField campoFiltroFuncionario;
    @FXML private javafx.scene.control.DatePicker dataInicio;
    @FXML private javafx.scene.control.DatePicker dataFim;

    private File arquivoSelecionado;

    @FXML
    public void initialize() {
        comboTipo.getItems().addAll("RG", "CPF", "Contrato", "Currículo", "Outros");
    }

    @FXML
    public void selecionarArquivo() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Selecionar Documento");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos", "*.pdf", "*.jpg", "*.png"));
        arquivoSelecionado = chooser.showOpenDialog(null);
        if (arquivoSelecionado != null) {
            labelArquivoSelecionado.setText(arquivoSelecionado.getName());
        }
    }

    @FXML
    public void importarDocumento() {
        try {
            Documento doc = new Documento();
            doc.setTitulo(campoTitulo.getText());
            doc.setTipo(comboTipo.getValue());
            doc.setDataValidade(campoValidade.getValue());
            doc.setFuncionarioId(Integer.parseInt(campoFuncionarioId.getText()));
            doc.setCaminhoArquivo(arquivoSelecionado.getAbsolutePath());

            boolean sucesso = DocumentoDAO.salvar(doc);
            mostrarAlerta(sucesso ? "Documento importado com sucesso." : "Erro ao importar.");
        } catch (Exception e) {
            mostrarAlerta("Erro ao importar documento: " + e.getMessage());
        }
    }

    @FXML
    public void exportarExcel() {
        exportarFormato("Excel", "*.xlsx", ExcelExportUtil::gerarRelatorioExcel);
    }

    @FXML
    public void exportarCSV() {
        exportarFormato("CSV", "*.csv", CSVExportUtil::gerarCSV);
    }

    @FXML
    public void exportarPDF() {
        exportarFormato("PDF", "*.pdf", PdfExportUtil::gerarPDF);
    }

    @FXML
    public void exportarTudo() {
        List<Documento> docs = DocumentoDAO.listarTodos();
        try {
            String basePath = System.getProperty("user.home") + "/Desktop/";
            ExcelExportUtil.gerarRelatorioExcel(docs, basePath + "documentos.xlsx");
            CSVExportUtil.gerarCSV(docs, basePath + "documentos.csv");
            PdfExportUtil.gerarPDF(docs, basePath + "documentos.pdf");
            mostrarAlerta("Exportações concluídas na área de trabalho.");
        } catch (Exception e) {
            mostrarAlerta("Erro ao exportar todos formatos.");
        }
    }

    @FXML
    public void exportarVencidos() {
        List<Documento> docs = DocumentoDAO.listarVencidos();
        try {
            String path = System.getProperty("user.home") + "/Desktop/documentos_vencidos.pdf";
            PdfExportUtil.gerarPDF(docs, path);
            mostrarAlerta("PDF com documentos vencidos gerado na área de trabalho.");
        } catch (Exception e) {
            mostrarAlerta("Erro ao exportar vencidos.");
        }
    }

    @FXML
    public void aplicarFiltro() {
        try {
            String termo = campoFiltroFuncionario.getText();
            LocalDate inicio = dataInicio.getValue();
            LocalDate fim = dataFim.getValue();
            List<Documento> filtrados = DocumentoDAO.buscarComFiltro(termo, inicio, fim);
            FileChooser fc = new FileChooser();
            fc.setInitialFileName("documentos_filtrados.pdf");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
            File destino = fc.showSaveDialog(null);
            if (destino != null) {
                PdfExportUtil.gerarPDF(filtrados, destino.getAbsolutePath());
                mostrarAlerta("Exportado com sucesso.");
            }
        } catch (Exception e) {
            mostrarAlerta("Erro ao aplicar filtro: " + e.getMessage());
        }
    }

    @FXML
    private void exportarFormato(String titulo, String extensao, Exportador exportador) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Exportar para " + titulo);
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(titulo, extensao));
            File destino = fileChooser.showSaveDialog(null);
            if (destino != null) {
                List<Documento> docs = DocumentoDAO.listarTodos();
                exportador.exportar(docs, destino.getAbsolutePath());
                mostrarAlerta(titulo + " exportado: " + destino.getAbsolutePath());
            }
        } catch (Exception e) {
            mostrarAlerta("Erro ao exportar: " + e.getMessage());
        }
    }

    public static void mostrarAlerta(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Exportação de Documentos");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    interface Exportador {
        void exportar(List<Documento> docs, String destino) throws Exception;
    }
}
