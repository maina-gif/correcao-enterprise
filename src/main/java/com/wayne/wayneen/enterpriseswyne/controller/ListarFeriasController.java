package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.FeriasDAO;
import com.wayne.wayneen.enterpriseswyne.DAO.FuncionarioDAOMethods;

import com.wayne.wayneen.enterpriseswyne.model.Ferias;
import com.wayne.wayneen.enterpriseswyne.model.Funcionario;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class ListarFeriasController {

    @FXML private TableView<Ferias> tabelaFerias;
    @FXML private TableColumn<Ferias, String> colNome;
    @FXML private TableColumn<Ferias, String> colInicio;
    @FXML private TableColumn<Ferias, String> colFim;
    @FXML private TableColumn<Ferias, String> colObs;

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(data -> {
            Funcionario funcionario = FuncionarioDAOMethods.buscarPorId(data.getValue().getFuncionarioId());
            return new ReadOnlyStringWrapper(funcionario != null ? funcionario.getNomeCompleto() : "Desconhecido");
        });
        colInicio.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getDataInicio().toString()));
        colFim.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getDataFim().toString()));
        colObs.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getObservacao()));

        carregarFerias();
    }

    private void carregarFerias() {
        List<Ferias> lista = FeriasDAO.listarTodas();
        ObservableList<Ferias> dados = FXCollections.observableArrayList(lista);
        tabelaFerias.setItems(dados);
    }
}
