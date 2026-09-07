package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.NotificacaoDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

import java.time.LocalDateTime;
import java.util.function.UnaryOperator;

public class EnviarNotificacaoController {

    @FXML private TextArea mensagemArea;
    @FXML private Label contadorLabel;
    @FXML private Button btnEnviar;

    private static final int LIMITE = 280;

    private final NotificacaoDAO notificacaoDAO = new NotificacaoDAO();

    /** Referência opcional para atualizar a Central em tempo real (se estiver aberta). */
    private NotificacaoController centralController;

    /** Injete a referência da central ao abrir o formulário, se disponível. */
    public void setCentralController(NotificacaoController centralController) {
        this.centralController = centralController;
    }

    @FXML
    private void initialize() {
        // Impõe limite com TextFormatter (evita loop de setText)
        StringConverter<String> converter = new DefaultStringConverter();
        UnaryOperator<TextFormatter.Change> filtro = change -> {
            String novo = change.getControlNewText();
            if (novo != null && novo.length() > LIMITE) {
                change.setText(novo.substring(0, LIMITE));
                change.setRange(0, change.getControlText().length());
            }
            return change;
        };
        mensagemArea.setTextFormatter(new TextFormatter<>(converter, "", filtro));

        // Atualiza contador e estado do botão
        mensagemArea.textProperty().addListener((obs, o, n) -> {
            int len = (n == null) ? 0 : n.length();
            contadorLabel.setText(len + "/" + LIMITE);
            btnEnviar.setDisable(len == 0);
        });

        Platform.runLater(() -> {
            String txt = mensagemArea.getText();
            int len = (txt == null) ? 0 : txt.length();
            contadorLabel.setText(len + "/" + LIMITE);
            btnEnviar.setDisable(len == 0);
        });

        // Atalho Ctrl+Enter
        mensagemArea.addEventFilter(KeyEvent.KEY_PRESSED, evt -> {
            if (evt.isControlDown() && evt.getCode() == KeyCode.ENTER) {
                enviarNotificacao();
                evt.consume();
            }
        });
        mensagemArea.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, evt -> {
                    if (evt.isControlDown() && evt.getCode() == KeyCode.ENTER) {
                        enviarNotificacao();
                        evt.consume();
                    }
                });
            }
        });
    }

    @FXML
    private void enviarNotificacao() {
        String msg = (mensagemArea.getText() == null) ? "" : mensagemArea.getText().trim();
        if (msg.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Mensagem vazia", "Digite a mensagem da notificação.");
            return;
        }

        try {
            // 1) grava no banco
            notificacaoDAO.inserirMensagem(msg);

            // 2) se a Central estiver aberta, atualiza a tabela imediatamente
            if (centralController != null) {
                centralController.adicionarNotificacao(msg, LocalDateTime.now(), false);
            }

            mostrarAlerta(Alert.AlertType.INFORMATION, "Enviado", "Notificação cadastrada com sucesso.");
            fecharJanela();
        } catch (Exception e) {
            String detalhe = (e.getMessage() == null || e.getMessage().isBlank())
                    ? e.getClass().getSimpleName()
                    : e.getMessage();
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ao enviar", detalhe);
        }
    }

    @FXML
    private void fecharJanela() {
        Scene scene = (btnEnviar != null) ? btnEnviar.getScene() : null;
        if (scene != null && scene.getWindow() instanceof Stage stage) {
            stage.close();
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String msg) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
