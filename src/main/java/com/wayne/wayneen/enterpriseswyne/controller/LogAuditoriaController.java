package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.LogAuditoriaDAO;
import com.wayne.wayneen.enterpriseswyne.model.SessionManager;
import com.wayne.wayneen.enterpriseswyne.model.LogAuditoria;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LogAuditoriaController {

    @FXML private TableView<LogAuditoria> tabelaLogs;
    @FXML private TableColumn<LogAuditoria, String> colUsuario;
    @FXML private TableColumn<LogAuditoria, String> colAcao;
    @FXML private TableColumn<LogAuditoria, String> colModulo;
    @FXML private TableColumn<LogAuditoria, String> colDataHora;

    @FXML
    public void initialize() {
        colUsuario.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getUsuario()));
        colAcao.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getAcao()));
        colModulo.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getModulo()));
        colDataHora.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getDataHora().toString()));

        carregarLogs();
    }

    private void carregarLogs() {
        ObservableList<LogAuditoria> logs = FXCollections.observableArrayList((LogAuditoria) LogAuditoriaDAO.listarTodos());
        tabelaLogs.setItems(logs);
    }

    @FXML
    private void fechar() {
        Stage stage = (Stage) tabelaLogs.getScene().getWindow();
        stage.close();
    }
}
