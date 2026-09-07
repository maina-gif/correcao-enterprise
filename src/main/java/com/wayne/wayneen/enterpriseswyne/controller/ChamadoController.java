package com.wayne.wayneen.enterpriseswyne;

import com.wayne.wayneen.enterpriseswyne.DAO.ChamadoDAO;
import com.wayne.wayneen.enterpriseswyne.model.Chamado;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;

public class ChamadoController {

    @FXML private TextField campoTitulo;
    @FXML private TextField campoFuncionario;
    @FXML private TextArea campoDescricao;
    @FXML private ComboBox<String> comboStatus;
    @FXML private ComboBox<String> comboPrioridade;
    @FXML private TextField campoData;

    @FXML private TableView<Chamado> tabelaChamados;
    @FXML private TableColumn<Chamado, Integer> colId;
    @FXML private TableColumn<Chamado, String> colTitulo;
    @FXML private TableColumn<Chamado, String> colFuncionario;
    @FXML private TableColumn<Chamado, String> colStatus;
    @FXML private TableColumn<Chamado, String> colPrioridade;
    @FXML private TableColumn<Chamado, String> colData;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getId()));
        colTitulo.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getTitulo()));
        colFuncionario.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getFuncionario()));
        colStatus.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getStatus()));
        colPrioridade.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getPrioridade()));
        colData.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getDataAbertura()));

        atualizarTabela();
    }

    @FXML
    private void salvarChamado() {
        Chamado c = new Chamado(
                campoTitulo.getText(),
                campoDescricao.getText(),
                comboStatus.getValue(),
                comboPrioridade.getValue(),
                campoFuncionario.getText(),
                campoData.getText()
        );
        ChamadoDAO.salvar(c);
        limparCampos();
        atualizarTabela();
    }

    private void atualizarTabela() {
        tabelaChamados.setItems(FXCollections.observableArrayList(ChamadoDAO.listarTodos()));
    }

    private void limparCampos() {
        campoTitulo.clear();
        campoFuncionario.clear();
        campoDescricao.clear();
        campoData.clear();
        comboStatus.getSelectionModel().clearSelection();
        comboPrioridade.getSelectionModel().clearSelection();
    }
}
