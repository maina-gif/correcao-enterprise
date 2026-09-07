package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.CandidatoDAO;
import com.wayne.wayneen.enterpriseswyne.model.Candidato;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RecrutamentoController {

    @FXML private TextField campoNome, campoEmail, campoCargo, campoLink;
    @FXML private TableView<Candidato> tabelaCandidatos;
    @FXML private TableColumn<Candidato, Integer> colId;
    @FXML private TableColumn<Candidato, String> colNome, colEmail, colCargo, colLink;

    private final CandidatoDAO dao = new CandidatoDAO();
    private Candidato selecionado;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()).asObject());
        colNome.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNome()));
        colEmail.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEmail()));
        colCargo.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCargoPretendido()));
        colLink.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getLinkCurriculo()));

        tabelaCandidatos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) preencherCampos(newVal);
        });

        carregarTabela();
    }

    private void carregarTabela() {
        tabelaCandidatos.setItems(FXCollections.observableArrayList(dao.listar()));
    }

    private void preencherCampos(Candidato c) {
        selecionado = c;
        campoNome.setText(c.getNome());
        campoEmail.setText(c.getEmail());
        campoCargo.setText(c.getCargoPretendido());
        campoLink.setText(c.getLinkCurriculo());
    }

    @FXML
    private void salvar() {
        Candidato c = new Candidato();
        c.setNome(campoNome.getText());
        c.setEmail(campoEmail.getText());
        c.setCargoPretendido(campoCargo.getText());
        c.setLinkCurriculo(campoLink.getText());
        dao.inserir(c);
        limparCampos();
        carregarTabela();
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
        campoEmail.clear();
        campoCargo.clear();
        campoLink.clear();
        tabelaCandidatos.getSelectionModel().clearSelection();
        selecionado = null;
    }
}
