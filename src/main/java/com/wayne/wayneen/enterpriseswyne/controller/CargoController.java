package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.model.Cargo;
import com.wayne.wayneen.enterpriseswyne.DAO.CargoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CargoController {

    @FXML private TextField campoNome;
    @FXML private TextField campoNivel;
    @FXML private TextField campoSalario;
    @FXML private TextArea campoCriterios;

    @FXML private TableView<Cargo> tabelaCargos;
    @FXML private TableColumn<Cargo, Integer> colId;
    @FXML private TableColumn<Cargo, String> colNome;
    @FXML private TableColumn<Cargo, String> colNivel;
    @FXML private TableColumn<Cargo, String> colCriterios;
    @FXML private TableColumn<Cargo, Double> colSalario;

    private final CargoDAO dao = new CargoDAO();
    private Cargo cargoSelecionado;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colNome.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNomeCargo()));
        colNivel.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNivel()));
        colSalario.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getSalarioBase()).asObject());
        colCriterios.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCriteriosPromocao()));

        tabelaCargos.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                preencherCampos(novo);
            }
        });

        carregarTabela();
    }

    private void carregarTabela() {
        ObservableList<Cargo> cargos = FXCollections.observableArrayList(dao.listar());
        tabelaCargos.setItems(cargos);
    }

    private void preencherCampos(Cargo cargo) {
        cargoSelecionado = cargo;
        campoNome.setText(cargo.getNomeCargo());
        campoNivel.setText(cargo.getNivel());
        campoSalario.setText(String.valueOf(cargo.getSalarioBase()));
        campoCriterios.setText(cargo.getCriteriosPromocao());
    }

    @FXML
    private void salvarCargo() {
        if (validarCampos()) {
            try {
                Cargo cargo = new Cargo();
                cargo.setNomeCargo(campoNome.getText());
                cargo.setNivel(campoNivel.getText());
                cargo.setSalarioBase(Double.parseDouble(campoSalario.getText()));
                cargo.setCriteriosPromocao(campoCriterios.getText());

                dao.inserir(cargo);
                limparCampos();
                carregarTabela();

            } catch (NumberFormatException e) {
                exibirAlerta("Erro de entrada", "O salário deve ser um número válido.");
            }
        }
    }

    @FXML
    private void atualizarCargo() {
        if (cargoSelecionado != null && validarCampos()) {
            try {
                cargoSelecionado.setNomeCargo(campoNome.getText());
                cargoSelecionado.setNivel(campoNivel.getText());
                cargoSelecionado.setSalarioBase(Double.parseDouble(campoSalario.getText()));
                cargoSelecionado.setCriteriosPromocao(campoCriterios.getText());

                dao.atualizar(cargoSelecionado);
                limparCampos();
                carregarTabela();

            } catch (NumberFormatException e) {
                exibirAlerta("Erro de entrada", "O salário deve ser um número válido.");
            }
        } else {
            exibirAlerta("Seleção obrigatória", "Selecione um cargo para atualizar.");
        }
    }

    @FXML
    private void excluirCargo() {
        if (cargoSelecionado != null) {
            dao.excluir(cargoSelecionado.getId());
            limparCampos();
            carregarTabela();
        } else {
            exibirAlerta("Seleção obrigatória", "Selecione um cargo para excluir.");
        }
    }

    @FXML
    private void limparCampos() {
        campoNome.clear();
        campoNivel.clear();
        campoSalario.clear();
        campoCriterios.clear();
        tabelaCargos.getSelectionModel().clearSelection();
        cargoSelecionado = null;
    }

    private boolean validarCampos() {
        if (campoNome.getText().isEmpty() || campoNivel.getText().isEmpty()
                || campoSalario.getText().isEmpty() || campoCriterios.getText().isEmpty()) {
            exibirAlerta("Campos obrigatórios", "Preencha todos os campos antes de salvar ou atualizar.");
            return false;
        }
        return true;
    }

    private void exibirAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
