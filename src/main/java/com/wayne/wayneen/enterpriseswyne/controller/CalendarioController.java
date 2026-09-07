package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.EventoCalendarioDAO;
import com.wayne.wayneen.enterpriseswyne.model.EventoCalendario;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CalendarioController {

    @FXML private DatePicker filtroInicio;
    @FXML private DatePicker filtroFim;
    @FXML private ComboBox<String> filtroTipo;

    @FXML private TableView<EventoCalendario> tabelaEventos;
    @FXML private TableColumn<EventoCalendario, String> colTitulo;
    @FXML private TableColumn<EventoCalendario, String> colData;
    @FXML private TableColumn<EventoCalendario, String> colTipo;
    @FXML private TableColumn<EventoCalendario, String> colOrigem;
    @FXML private TableColumn<EventoCalendario, String> colDescricao;

    private final EventoCalendarioDAO dao = new EventoCalendarioDAO();
    private final ObservableList<EventoCalendario> dados = FXCollections.observableArrayList();
    private final DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private void initialize() {
        configurarTabela();
        carregarTiposNoFiltro();
        filtroInicio.setValue(LocalDate.now().minusDays(15));
        filtroFim.setValue(LocalDate.now().plusDays(15));
        carregarEventos();
    }

    private void configurarTabela() {
        colTitulo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTitulo()));
        colTipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipo()));
        colOrigem.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getOrigem()));
        colDescricao.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getDescricao() == null ? "" : c.getValue().getDescricao()
        ));
        colData.setCellValueFactory(c -> {
            LocalDate d = c.getValue().getData();
            return new SimpleStringProperty(d == null ? "" : d.format(df));
        });

        tabelaEventos.setItems(dados);
    }

    private void carregarTiposNoFiltro() {
        List<String> tipos = new ArrayList<>();
        tipos.add("Todos");
        try {
            List<String> doBanco = dao.listarTiposExistentes();
            if (doBanco != null && !doBanco.isEmpty()) tipos.addAll(doBanco);
        } catch (SQLException e) {
            // fallback simples
            if (!tipos.contains("Corporativo")) tipos.add("Corporativo");
            if (!tipos.contains("Funcionário")) tipos.add("Funcionário");
            exibirErro("Falha ao carregar tipos de evento", e);
        }
        filtroTipo.setItems(FXCollections.observableArrayList(tipos));
        filtroTipo.getSelectionModel().selectFirst();
    }

    @FXML
    public void carregarEventos() {
        LocalDate inicio = filtroInicio.getValue();
        LocalDate fim = filtroFim.getValue();
        String tipo = filtroTipo.getValue();

        try {
            List<EventoCalendario> lista = dao.listar(inicio, fim, tipo);
            dados.setAll(lista);
        } catch (SQLException e) {
            exibirErro("Erro ao carregar eventos", e);
        }
    }

    private void exibirErro(String titulo, Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(titulo);
        alert.setContentText(e.getMessage() == null ? e.toString() : e.getMessage());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }
}
