package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.ProcessoSeletivoDAO;
import com.wayne.wayneen.enterpriseswyne.model.ProcessoSeletivo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ProcessoSeletivoController {

    @FXML private TableView<ProcessoSeletivo> tabelaProcessos;
    @FXML private TableColumn<ProcessoSeletivo, String> colunaTitulo;
    @FXML private TableColumn<ProcessoSeletivo, String> colunaDescricao;
    @FXML private TableColumn<ProcessoSeletivo, LocalDate> colunaDataInicio;
    @FXML private TableColumn<ProcessoSeletivo, LocalDate> colunaDataFim;

    @FXML private TextField tituloField;
    @FXML private TextField descricaoField;
    @FXML private DatePicker dataInicioPicker;
    @FXML private DatePicker dataFimPicker;

    private final ObservableList<ProcessoSeletivo> listaProcessos = FXCollections.observableArrayList();
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        colunaTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaDataInicio.setCellValueFactory(new PropertyValueFactory<>("dataInicio"));
        colunaDataFim.setCellValueFactory(new PropertyValueFactory<>("dataFim"));

        colunaDataInicio.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : DATE_FMT.format(item));
            }
        });

        colunaDataFim.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : DATE_FMT.format(item));
            }
        });

        carregarProcessos();
    }

    private void carregarProcessos() {
        try {
            List<ProcessoSeletivo> dados = ProcessoSeletivoDAO.listarTodos();
            listaProcessos.setAll(dados);
            tabelaProcessos.setItems(listaProcessos);
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ao carregar", null, e.getMessage());
        }
    }

    @FXML
    private void salvarProcesso() {
        try {
            String titulo = tituloField.getText();
            String descricao = descricaoField.getText();
            LocalDate di = dataInicioPicker.getValue();
            LocalDate df = dataFimPicker.getValue();

            if (titulo == null || titulo.isBlank() ||
                    descricao == null || descricao.isBlank() ||
                    di == null || df == null) {
                mostrarAlerta(Alert.AlertType.WARNING, "Validação", null, "Preencha todos os campos.");
                return;
            }

            ProcessoSeletivo p = new ProcessoSeletivo();
            p.setTitulo(titulo);
            p.setDescricao(descricao);
            p.setDataInicio(di);
            p.setDataFim(df);

            ProcessoSeletivoDAO.inserir(p);
            limparCampos();
            carregarProcessos();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", null, "Processo salvo.");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ao salvar", null, e.getMessage());
        }
    }

    @FXML
    private void excluirProcesso() {
        ProcessoSeletivo sel = tabelaProcessos.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", null, "Selecione um item.");
            return;
        }
        try {
            ProcessoSeletivoDAO.excluir(sel.getId());
            carregarProcessos();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", null, "Excluído.");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ao excluir", null, e.getMessage());
        }
    }

    private void limparCampos() {
        tituloField.clear();
        descricaoField.clear();
        dataInicioPicker.setValue(null);
        dataFimPicker.setValue(null);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String header, String conteudo) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo);
        a.setHeaderText(header);
        a.setContentText(conteudo);
        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        a.showAndWait();
    }
}
