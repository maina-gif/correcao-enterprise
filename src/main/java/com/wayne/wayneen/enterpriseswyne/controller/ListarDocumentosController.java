package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.DocumentoDAO;
import com.wayne.wayneen.enterpriseswyne.Documento;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;

import java.io.IOException;

public class ListarDocumentosController {

    @FXML private TableView<Documento> tabelaDocumentos;
    @FXML private TableColumn<Documento, String> colTitulo;
    @FXML private TableColumn<Documento, String> colTipo;
    @FXML private TableColumn<Documento, String> colValidade;
    @FXML private TableColumn<Documento, String> colCaminho;

    private final ObservableList<Documento> documentos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        colTitulo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTitulo()));
        colTipo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTipo()));
        colValidade.setCellValueFactory(cell -> {
            var data = cell.getValue().getDataValidade();
            String texto = data != null ? data.format(formatter) : "";
            return new SimpleStringProperty(texto);
        });
        colCaminho.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCaminhoArquivo()));

        tabelaDocumentos.setItems(documentos);
        carregarDocumentos();
    }

    @FXML
    public void carregarDocumentos() {
        documentos.clear();
        List<Documento> lista = DocumentoDAO.listarTodos();
        if (lista != null) {
            documentos.addAll(lista);
        }
    }

    @FXML
    public void abrirDocumento() {
        Documento doc = tabelaDocumentos.getSelectionModel().getSelectedItem();
        if (doc == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Seleção necessária", "Selecione um documento para abrir.");
            return;
        }

        File file = new File(doc.getCaminhoArquivo());
        if (!file.exists()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Arquivo não encontrado", "Arquivo não existe no caminho informado.");
            return;
        }

        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                // Abre em background para evitar travamento na UI
                new Thread(() -> {
                    try {
                        Desktop.getDesktop().open(file);
                    } catch (IOException io) {
                        Platform.runLater(() ->
                                mostrarAlerta(Alert.AlertType.ERROR, "Erro ao abrir", "Não foi possível abrir o arquivo:\n" + io.getMessage()));
                        io.printStackTrace();
                    }
                }).start();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Operação não suportada",
                        "Abrir arquivos não é suportado nesse sistema.");
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro interno", "Ocorreu um erro ao tentar abrir o arquivo.");
            e.printStackTrace();
        }
    }


    @FXML
    public void excluirDocumento() {
        Documento doc = tabelaDocumentos.getSelectionModel().getSelectedItem();
        if (doc == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Seleção necessária", "Selecione um documento para excluir.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmação");
        confirm.setHeaderText("Excluir Documento");
        confirm.setContentText("Tem certeza que deseja excluir \"" + doc.getTitulo() + "\"?");
        confirm.initOwner(tabelaDocumentos.getScene().getWindow());

        confirm.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    boolean sucesso = DocumentoDAO.excluir(doc.getId());
                    if (sucesso) {
                        mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Documento excluído com sucesso.");
                        carregarDocumentos();
                    } else {
                        mostrarAlerta(Alert.AlertType.ERROR, "Falha", "Erro ao excluir o documento.");
                    }
                });
    }

    @FXML
    public void fecharJanela() {
        Stage stage = (Stage) tabelaDocumentos.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String conteudo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(conteudo);
        alert.initOwner(tabelaDocumentos.getScene().getWindow());
        alert.showAndWait();
    }
}
