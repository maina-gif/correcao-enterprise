package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.model.AgendaCorporativa;
import com.wayne.wayneen.enterpriseswyne.DAO.AgendaCorporativaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;

public class AgendaCorporativaController {

    @FXML private TextField txtTitulo;
    @FXML private TextArea txtDescricao;
    @FXML private DatePicker dpDataEvento;
    @FXML private TextField txtTipo;
    @FXML private TextField txtLocal;
    @FXML private TextField txtResponsavel;
    @FXML private TableView<AgendaCorporativa> tabela;
    @FXML private TableColumn<AgendaCorporativa, String> colTitulo;
    @FXML private TableColumn<AgendaCorporativa, LocalDate> colData;
    @FXML private TableColumn<AgendaCorporativa, String> colTipo;
    @FXML private TableColumn<AgendaCorporativa, String> colLocal;

    private ObservableList<AgendaCorporativa> listaEventos = FXCollections.observableArrayList();

    public void initialize() {
        colTitulo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitulo()));
        colData.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getDataEvento()));
        colTipo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTipoEvento()));
        colLocal.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLocal()));

        listarEventos();
    }

    private void listarEventos() {
        listaEventos.setAll(AgendaCorporativaDAO.listarTodos());
        tabela.setItems(listaEventos);
    }

    @FXML
    private void salvarEvento() {
        AgendaCorporativa evento = new AgendaCorporativa(
                txtTitulo.getText(),
                txtDescricao.getText(),
                dpDataEvento.getValue(),
                txtTipo.getText(),
                txtLocal.getText(),
                txtResponsavel.getText()
        );
        AgendaCorporativaDAO.inserir(evento);
        listarEventos();
        limparCampos();
    }

    @FXML
    private void excluirSelecionado() {
        AgendaCorporativa evento = tabela.getSelectionModel().getSelectedItem();
        if (evento != null) {
            AgendaCorporativaDAO.excluir(evento.getId());
            listarEventos();
        }
    }

    private void limparCampos() {
        txtTitulo.clear();
        txtDescricao.clear();
        dpDataEvento.setValue(null);
        txtTipo.clear();
        txtLocal.clear();
        txtResponsavel.clear();
    }
}
