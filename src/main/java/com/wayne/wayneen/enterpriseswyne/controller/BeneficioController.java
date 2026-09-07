package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.model.Beneficio;
import com.wayne.wayneen.enterpriseswyne.DAO.BeneficioDAO;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class BeneficioController {

    @FXML private TextField campoTipo;
    @FXML private TextField campoValor;
    @FXML private ComboBox<String> comboStatus;
    @FXML private TableView<Beneficio> tabelaBeneficios;
    @FXML private TableColumn<Beneficio, Integer> colId;
    @FXML private TableColumn<Beneficio, String> colTipo;
    @FXML private TableColumn<Beneficio, Double> colValor;
    @FXML private TableColumn<Beneficio, String> colStatus;

    private final BeneficioDAO dao = new BeneficioDAO();
    private Beneficio selecionado;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getId()).asObject());
        colTipo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTipo()));
        colValor.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getValor()).asObject());
        colStatus.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus()));

        comboStatus.setItems(FXCollections.observableArrayList("Ativo", "Inativo"));
        comboStatus.setValue("Ativo");

        tabelaBeneficios.getSelectionModel().selectedItemProperty().addListener((obs, old, novo) -> {
            if (novo != null) preencherCampos(novo);
        });

        carregarTabela();
    }

    private void carregarTabela() {
        tabelaBeneficios.setItems(FXCollections.observableArrayList(dao.listar()));
    }

    private void preencherCampos(Beneficio b) {
        selecionado = b;
        campoTipo.setText(b.getTipo());
        campoValor.setText(String.valueOf(b.getValor()));
        comboStatus.setValue(b.getStatus());
    }

    @FXML
    private void salvar() {
        try {
            Beneficio b = new Beneficio();
            b.setTipo(campoTipo.getText());
            b.setValor(Double.parseDouble(campoValor.getText()));
            b.setStatus(comboStatus.getValue());
            dao.inserir(b);
            limparCampos();
            carregarTabela();
        } catch (NumberFormatException e) {
            mostrarErro("Valor inválido. Informe um número válido.");
        }
    }

    @FXML
    private void atualizar() {
        if (selecionado != null) {
            try {
                selecionado.setTipo(campoTipo.getText());
                selecionado.setValor(Double.parseDouble(campoValor.getText()));
                selecionado.setStatus(comboStatus.getValue());
                dao.atualizar(selecionado);
                limparCampos();
                carregarTabela();
            } catch (NumberFormatException e) {
                mostrarErro("Valor inválido. Informe um número válido.");
            }
        }
    }

    @FXML
    private void excluir() {
        if (selecionado != null) {
            dao.excluir(selecionado.getId());
            limparCampos();
            carregarTabela();
        }
    }

    @FXML
    private void limparCampos() {
        campoTipo.clear();
        campoValor.clear();
        comboStatus.setValue("Ativo");
        tabelaBeneficios.getSelectionModel().clearSelection();
        selecionado = null;
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText("Erro de entrada");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
