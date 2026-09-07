package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.LogDAO;

import com.wayne.wayneen.enterpriseswyne.model.LogAcao;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class AuditoriaController {

    @FXML private TableView<LogAcao> tabela;
    @FXML private TableColumn<LogAcao, String> colUsuario;
    @FXML private TableColumn<LogAcao, String> colAcao;
    @FXML private TableColumn<LogAcao, String> colModulo;
    @FXML private TableColumn<LogAcao, String> colMomento;
    @FXML private TextField campoBusca;
    @FXML private Label labelStatus;

    private final ObservableList<LogAcao> dados = FXCollections.observableArrayList();
    private ScheduledService<Void> refreshService;

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    private void initialize() {
        // Factories de coluna
        colUsuario.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getUsuario()));
        colAcao.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getAcao()));
        colModulo.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getModulo()));
        colMomento.setCellValueFactory(cd -> {
            String v = cd.getValue().getMomento() != null ? fmt.format(cd.getValue().getMomento()) : "";
            return new javafx.beans.property.SimpleStringProperty(v);
        });

        tabela.setItems(dados);

        // Filtro com debounce
        PauseTransition debounce = new PauseTransition(Duration.millis(250));
        campoBusca.textProperty().addListener((o, old, val) -> {
            debounce.stop();
            debounce.setOnFinished(e -> aplicarFiltro(val));
            debounce.playFromStart();
        });

        // Carrega primeira vez e inicia auto-refresh
        atualizarAgora();
        iniciarRefreshAuto();
    }

    @FXML
    public void atualizarAgora() {
        labelStatus.setText("Carregando...");
        new Thread(() -> {
            List<LogAcao> lista = LogDAO.listarTodos();
            Platform.runLater(() -> {
                dados.setAll(lista);
                aplicarFiltro(campoBusca.getText());
                labelStatus.setText("Registros: " + dados.size());
            });
        }).start();
    }

    private void aplicarFiltro(String termo) {
        if (termo == null || termo.isBlank()) {
            tabela.setItems(dados);
            labelStatus.setText("Registros: " + dados.size());
            return;
        }
        String t = termo.toLowerCase().trim();
        List<LogAcao> filtrados = dados.stream()
                .filter(l ->
                        (l.getUsuario() != null && l.getUsuario().toLowerCase().contains(t)) ||
                                (l.getAcao() != null && l.getAcao().toLowerCase().contains(t)) ||
                                (l.getModulo() != null && l.getModulo().toLowerCase().contains(t))
                )
                .collect(Collectors.toList());
        tabela.setItems(FXCollections.observableArrayList(filtrados));
        labelStatus.setText("Registros filtrados: " + filtrados.size());
    }

    private void iniciarRefreshAuto() {
        refreshService = new ScheduledService<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {
                        List<LogAcao> lista = LogDAO.listarTodos();
                        Platform.runLater(() -> {
                            dados.setAll(lista);
                            aplicarFiltro(campoBusca.getText());
                            labelStatus.setText("Atualizado: " + dados.size() + " itens");
                        });
                        return null;
                    }
                };
            }
        };
        refreshService.setPeriod(Duration.seconds(5)); // atualiza a cada 5s
        refreshService.setRestartOnFailure(true);
        refreshService.start();
    }

    @FXML
    private void fechar() {
        // fecha a janela atual
        tabela.getScene().getWindow().hide();
    }
}
