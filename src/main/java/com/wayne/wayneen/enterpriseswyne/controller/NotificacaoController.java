package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.NotificacaoDAO;
import com.wayne.wayneen.enterpriseswyne.model.Notificacao;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** Controller da Central de Notificações */
public class NotificacaoController {

    @FXML private TableView<Notificacao> tabelaNotificacoes;

    // Estas três colunas só existirão se o FXML declarar fx:id para elas
    @FXML private TableColumn<Notificacao, String>  colMensagem;
    @FXML private TableColumn<Notificacao, String>  colDataHora;
    @FXML private TableColumn<Notificacao, Boolean> colStatus;

    private final ObservableList<Notificacao> notificacoes = FXCollections.observableArrayList();
    private final NotificacaoDAO dao = new NotificacaoDAO();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        // Se as colunas vieram do FXML com fx:id, configura-as.
        if (colMensagem != null && colDataHora != null && colStatus != null) {
            configurarColunasFXML();
        } else {
            // Caso o FXML não tenha colunas com fx:id, cria programaticamente.
            criarColunasProgramaticas();
        }

        tabelaNotificacoes.setItems(notificacoes);
        carregarNotificacoes();
    }

    /** Configura as colunas já definidas no FXML (com fx:id). */
    private void configurarColunasFXML() {
        // mensagem via propriedade "mensagem"
        colMensagem.setCellValueFactory(new PropertyValueFactory<>("mensagem"));

        // data/hora formatada
        colDataHora.setCellValueFactory(cd -> {
            LocalDateTime dt = cd.getValue().getDataHora();
            return new ReadOnlyStringWrapper(dt == null ? "" : dt.format(FMT));
        });

        // lida como boolean + checkbox (somente leitura)
        colStatus.setCellValueFactory(cd ->
                new ReadOnlyBooleanWrapper(cd.getValue().isLida()).getReadOnlyProperty()
        );
        colStatus.setCellFactory(CheckBoxTableCell.forTableColumn(colStatus));

        tabelaNotificacoes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabelaNotificacoes.setEditable(false);
    }

    /** Cria colunas quando o FXML não as define com fx:id. */
    private void criarColunasProgramaticas() {
        TableColumn<Notificacao, String> cMsg = new TableColumn<>("Mensagem");
        cMsg.setCellValueFactory(new PropertyValueFactory<>("mensagem"));
        cMsg.setMinWidth(300);

        TableColumn<Notificacao, String> cData = new TableColumn<>("Data/Hora");
        cData.setPrefWidth(160);
        cData.setCellValueFactory(cd -> {
            LocalDateTime dt = cd.getValue().getDataHora();
            return new ReadOnlyStringWrapper(dt == null ? "" : dt.format(FMT));
        });

        TableColumn<Notificacao, Boolean> cLida = new TableColumn<>("Lida");
        cLida.setPrefWidth(80);
        cLida.setCellValueFactory(cd ->
                new ReadOnlyBooleanWrapper(cd.getValue().isLida()).getReadOnlyProperty()
        );
        cLida.setCellFactory(CheckBoxTableCell.forTableColumn(cLida));

        tabelaNotificacoes.getColumns().setAll(cMsg, cData, cLida);
        tabelaNotificacoes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabelaNotificacoes.setEditable(false);
    }

    /** Carrega todas as notificações do banco. */
    private void carregarNotificacoes() {
        notificacoes.clear();
        try {
            List<Notificacao> lista = dao.listarTodas();
            notificacoes.addAll(lista);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erro ao carregar notificações: " + e.getMessage()).showAndWait();
        }
    }

    /** Marca a notificação selecionada como lida. */
    @FXML
    private void marcarComoLida() {
        Notificacao selecionada = tabelaNotificacoes.getSelectionModel().getSelectedItem();
        if (selecionada != null && !selecionada.isLida()) {
            try {
                dao.marcarComoLida(selecionada.getId());
                selecionada.setLida(true);
                tabelaNotificacoes.refresh();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Erro ao marcar notificação como lida: " + e.getMessage()).showAndWait();
            }
        }
    }

    /** Abre a tela de envio de notificação (modal) */
    @FXML
    private void abrirFormularioEnvio() {
        try {
            URL url = getClass().getResource("/com/wayne/wayneen/enterpriseswyne/enviar_notificacao.fxml");
            if (url == null) {
                throw new IllegalStateException("enviar_notificacao.fxml não encontrado no classpath.");
            }
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            // injeta a central no formulário
            EnviarNotificacaoController formCtrl = loader.getController();
            formCtrl.setCentralController(this);

            Stage stage = new Stage();
            stage.setTitle("Enviar Notificação");
            stage.initModality(Modality.WINDOW_MODAL);
            if (tabelaNotificacoes != null && tabelaNotificacoes.getScene() != null) {
                stage.initOwner(tabelaNotificacoes.getScene().getWindow());
            }

            Scene scene = new Scene(root);
            URL css = getClass().getResource("/com/wayne/wayneen/enterpriseswyne/app.css");
            if (css != null) scene.getStylesheets().add(css.toExternalForm());
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Falha ao abrir formulário: " + e.getMessage()).showAndWait();
        }
    }

    /** Adiciona uma notificação (usado pelo formulário após salvar) */
    public void adicionarNotificacao(Notificacao notificacao) {
        notificacoes.add(0, notificacao); // topo
        tabelaNotificacoes.refresh();
    }

    /** Overload que casa com a chamada do EnviarNotificacaoController */
    public void adicionarNotificacao(String msg, LocalDateTime dataHora, boolean lida) {
        Notificacao n = new Notificacao(null, msg, dataHora, lida);
        notificacoes.add(0, n);
        tabelaNotificacoes.refresh();
    }
}
