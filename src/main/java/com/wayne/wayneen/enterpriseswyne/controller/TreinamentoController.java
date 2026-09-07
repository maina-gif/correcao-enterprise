package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.TreinamentoDAO;
import com.wayne.wayneen.enterpriseswyne.model.Treinamento;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TreinamentoController {

    @FXML private TextField campoTitulo, campoLocal;
    @FXML private ComboBox<String> comboTipo;
    @FXML private DatePicker campoData;

    @FXML private TableView<Treinamento> tabelaTreinamentos;
    @FXML private TableColumn<Treinamento, Integer> colId;
    @FXML private TableColumn<Treinamento, String> colTitulo, colTipo, colLocal;
    @FXML private TableColumn<Treinamento, LocalDate> colData; // <-- use LocalDate

    private final TreinamentoDAO dao = new TreinamentoDAO();
    private Treinamento selecionado;

    private static final DateTimeFormatter BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        // Bind das colunas
        colId.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getId()).asObject());
        colTitulo.setCellValueFactory(cell -> new SimpleStringProperty(safe(cell.getValue().getTitulo())));
        colTipo.setCellValueFactory(cell -> new SimpleStringProperty(safe(cell.getValue().getTipo())));
        colLocal.setCellValueFactory(cell -> new SimpleStringProperty(safe(cell.getValue().getLocal())));
        colData.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getData()));

        // Formatação dd/MM/yyyy para a data
        colData.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : BR.format(item));
            }
        });

        // Combo tipo
        comboTipo.setItems(FXCollections.observableArrayList("Interno", "Externo"));
        comboTipo.setValue("Interno");

        // Placeholder quando não há dados
        tabelaTreinamentos.setPlaceholder(new Label("Nenhum treinamento cadastrado."));

        // Listener de seleção
        tabelaTreinamentos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                preencherCampos(newVal);
            } else {
                limparCampos();
            }
        });

        carregarTabela();
    }

    private void carregarTabela() {
        try {
            List<Treinamento> lista = dao.listar();
            ObservableList<Treinamento> obs = FXCollections.observableArrayList(lista);
            tabelaTreinamentos.setItems(obs);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Falha ao carregar treinamentos.", Alert.AlertType.ERROR);
        }
    }

    private void preencherCampos(Treinamento t) {
        selecionado = t;
        campoTitulo.setText(safe(t.getTitulo()));
        comboTipo.setValue(safe(t.getTipo()).isBlank() ? "Interno" : t.getTipo());
        campoLocal.setText(safe(t.getLocal()));
        campoData.setValue(t.getData());
    }

    @FXML
    private void salvar() {
        if (!validarCampos()) return;

        try {
            Treinamento t = new Treinamento();
            t.setTitulo(campoTitulo.getText().trim());
            t.setTipo(comboTipo.getValue());
            t.setLocal(campoLocal.getText().trim());
            t.setData(campoData.getValue());

            dao.inserir(t);
            mostrarAlerta("Sucesso", "Treinamento cadastrado com sucesso!", Alert.AlertType.INFORMATION);
            limparCampos();
            carregarTabela();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Falha ao salvar o treinamento.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void atualizar() {
        if (selecionado == null) {
            mostrarAlerta("Atenção", "Selecione um treinamento para atualizar.", Alert.AlertType.WARNING);
            return;
        }
        if (!validarCampos()) return;

        try {
            selecionado.setTitulo(campoTitulo.getText().trim());
            selecionado.setTipo(comboTipo.getValue());
            selecionado.setLocal(campoLocal.getText().trim());
            selecionado.setData(campoData.getValue());

            dao.atualizar(selecionado);
            mostrarAlerta("Sucesso", "Treinamento atualizado com sucesso!", Alert.AlertType.INFORMATION);
            limparCampos();
            carregarTabela();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Falha ao atualizar o treinamento.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void excluir() {
        if (selecionado == null) {
            mostrarAlerta("Atenção", "Selecione um treinamento para excluir.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmação");
        confirm.setHeaderText(null);
        confirm.setContentText("Deseja excluir o treinamento \"" + selecionado.getTitulo() + "\"?");
        confirm.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                try {
                    dao.excluir(selecionado.getId());
                    mostrarAlerta("Sucesso", "Treinamento excluído com sucesso!", Alert.AlertType.INFORMATION);
                    limparCampos();
                    carregarTabela();
                } catch (Exception e) {
                    e.printStackTrace();
                    mostrarAlerta("Erro", "Falha ao excluir o treinamento.", Alert.AlertType.ERROR);
                }
            }
        });
    }

    @FXML
    private void limparCampos() {
        campoTitulo.clear();
        campoLocal.clear();
        campoData.setValue(null);
        comboTipo.setValue("Interno");
        tabelaTreinamentos.getSelectionModel().clearSelection();
        selecionado = null;
    }

    // ----------------- Helpers -----------------

    private boolean validarCampos() {
        String titulo = campoTitulo.getText() != null ? campoTitulo.getText().trim() : "";
        String local = campoLocal.getText() != null ? campoLocal.getText().trim() : "";
        LocalDate data = campoData.getValue();
        String tipo = comboTipo.getValue();

        if (titulo.isBlank()) {
            mostrarAlerta("Validação", "Informe o título do treinamento.", Alert.AlertType.WARNING);
            return false;
        }
        if (tipo == null || tipo.isBlank()) {
            mostrarAlerta("Validação", "Selecione o tipo do treinamento.", Alert.AlertType.WARNING);
            return false;
        }
        if (local.isBlank()) {
            mostrarAlerta("Validação", "Informe o local do treinamento.", Alert.AlertType.WARNING);
            return false;
        }
        if (data == null) {
            mostrarAlerta("Validação", "Selecione a data do treinamento.", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private static String safe(String s) {
        return (s == null) ? "" : s;
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
