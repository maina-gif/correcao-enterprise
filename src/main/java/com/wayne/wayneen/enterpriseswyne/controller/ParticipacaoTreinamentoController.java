package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.ParticipacaoTreinamentoDAO;
import com.wayne.wayneen.enterpriseswyne.model.ParticipacaoTreinamento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.time.LocalDate;

public class ParticipacaoTreinamentoController {

    @FXML private TextField txtFuncionario;
    @FXML private TextField txtTreinamento;
    @FXML private DatePicker dateParticipacao;
    @FXML private TableView<ParticipacaoTreinamento> tabelaParticipacoes;
    @FXML private TableColumn<ParticipacaoTreinamento, Integer> colFuncionario;
    @FXML private TableColumn<ParticipacaoTreinamento, Integer> colTreinamento;
    @FXML private TableColumn<ParticipacaoTreinamento, LocalDate> colData;

    private ParticipacaoTreinamentoDAO dao = new ParticipacaoTreinamentoDAO();

    @FXML
    public void initialize() throws SQLException {
        colFuncionario.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getIdFuncionario()).asObject());
        colTreinamento.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getIdTreinamento()).asObject());
        colData.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getDataParticipacao()));

        atualizarTabela();
    }

    private void atualizarTabela() throws SQLException {
        ObservableList<ParticipacaoTreinamento> lista = FXCollections.observableArrayList(dao.listar());
        tabelaParticipacoes.setItems(lista);
    }

    @FXML
    private void salvarParticipacao() throws SQLException {
        ParticipacaoTreinamento p = new ParticipacaoTreinamento();
        p.setIdFuncionario(Integer.parseInt(txtFuncionario.getText()));
        p.setIdTreinamento(Integer.parseInt(txtTreinamento.getText()));
        p.setDataParticipacao(dateParticipacao.getValue());

        dao.salvar(p);
        atualizarTabela();

        txtFuncionario.clear();
        txtTreinamento.clear();
        dateParticipacao.setValue(null);
    }

    @FXML
    private void excluirParticipacao() throws SQLException {
        ParticipacaoTreinamento selecionado = tabelaParticipacoes.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            dao.excluir(selecionado.getId());
            atualizarTabela();
        }
    }
}
