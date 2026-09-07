package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.model.Cargo;
import com.wayne.wayneen.enterpriseswyne.DAO.CargoDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PlanoCargosController {

    @FXML private TextField campoNome;
    @FXML private TextField campoSalario;
    @FXML private ComboBox<String> comboNivel;
    @FXML private TableView<Cargo> tabelaCargos;
    @FXML private TableColumn<Cargo, Integer> colId;
    @FXML private TableColumn<Cargo, String> colNome;
    @FXML private TableColumn<Cargo, String> colNivel;
    @FXML private TableColumn<Cargo, Double> colSalario;

    private final CargoDAO dao = new CargoDAO();
    private Cargo selecionado;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()).asObject());
        colNome.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNome()));
        colNivel.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNivel()));
        colSalario.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getSalarioBase()).asObject());

        comboNivel.setItems(FXCollections.observableArrayList("Júnior", "Pleno", "Sênior", "Especialista"));
        comboNivel.setValue("Júnior");

        tabelaCargos.getSelectionModel().selectedItemProperty().addListener((obs, old, novo) -> {
            if (novo != null) preencherCampos(novo);
        });

        carregarTabela();
    }

    private void preencherCampos(Cargo c) {
        selecionado = c;
        campoNome.setText(c.getNome());
        campoSalario.setText(String.valueOf(c.getSalarioBase()));
        comboNivel.setValue(c.getNivel());
    }

    private void carregarTabela() {
        tabelaCargos.setItems(FXCollections.observableArrayList(dao.listar()));
    }

    @FXML
    private void salvar() {
        Cargo c = new Cargo();
        c.setNome(campoNome.getText());
        c.setSalarioBase(Double.parseDouble(campoSalario.getText()));
        c.setNivel(comboNivel.getValue());
        dao.inserir(c);
        limparCampos();
        carregarTabela();
    }

    @FXML
    private void atualizar() {
        if (selecionado != null) {
            selecionado.setNome(campoNome.getText());
            selecionado.setSalarioBase(Double.parseDouble(campoSalario.getText()));
            selecionado.setNivel(comboNivel.getValue());
            dao.atualizar(selecionado);
            limparCampos();
            carregarTabela();
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
        campoNome.clear();
        campoSalario.clear();
        comboNivel.setValue("Júnior");
        tabelaCargos.getSelectionModel().clearSelection();
        selecionado = null;
    }
}
