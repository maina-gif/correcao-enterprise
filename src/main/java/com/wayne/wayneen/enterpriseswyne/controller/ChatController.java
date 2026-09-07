package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.ChatDAO;
import com.wayne.wayneen.enterpriseswyne.DAO.UsuariosDAO;
import com.wayne.wayneen.enterpriseswyne.DAO.UsuariosDAOJdbc;
import com.wayne.wayneen.enterpriseswyne.model.ChatMessage;
import com.wayne.wayneen.enterpriseswyne.model.ChatMessage.MessageStatus;
import com.wayne.wayneen.enterpriseswyne.model.Conversation;
import com.wayne.wayneen.enterpriseswyne.model.Usuarios;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class ChatController {

    @FXML private ListView<ChatMessage> listaMensagens;
    @FXML private TextField campoMensagem;
    @FXML private TextField campoBuscaUsuario;
    @FXML private ListView<Usuarios> listaUsuarios;
    @FXML private ListView<Conversation> listaConversas;
    @FXML private Label labelDestinatario;
    @FXML private Label labelTyping;
    @FXML private Button btnEnviar;

    private final ChatDAO chatDAO = new ChatDAO();
    private final UsuariosDAO usuariosDAO = new UsuariosDAOJdbc();

    // Estado atual (IDs são long primitivo nas models)
    private Usuarios usuarioLogado;      // defina via setUsuarioLogado
    private Conversation conversaAtual;
    private Usuarios destinatarioAtual;

    private final ObservableList<ChatMessage> mensagensObs = FXCollections.observableArrayList();
    private final ObservableList<Usuarios> usuariosObs     = FXCollections.observableArrayList();
    private final ObservableList<Conversation> conversasObs = FXCollections.observableArrayList();

    private ScheduledService<Void> pollingService;
    private final DateTimeFormatter horaFmt = DateTimeFormatter.ofPattern("HH:mm");

    /** Chame logo após abrir a tela para informar o usuário logado. */
    public void setUsuarioLogado(Usuarios u) {
        this.usuarioLogado = u;
        carregarListasIniciais();
        iniciarPolling();
    }

    @FXML
    public void initialize() {
        // Mensagens
        listaMensagens.setItems(mensagensObs);
        listaMensagens.setCellFactory(v -> new ListCell<>() {
            @Override
            protected void updateItem(ChatMessage m, boolean empty) {
                super.updateItem(m, empty);
                if (empty || m == null) { setText(null); setGraphic(null); return; }
                long idLogado = (usuarioLogado != null) ? usuarioLogado.getId() : -1L;
                String lado = (Objects.equals(m.getSenderId(), idLogado)) ? "Você" : "Eles";
                String status = statusToTicks(m.getStatus());
                String hora = (m.getCreatedAt() != null) ? horaFmt.format(m.getCreatedAt()) : "";
                setText(lado + " [" + hora + "]: " + m.getBody() + "   " + status);
            }
        });

        // Usuários
        listaUsuarios.setItems(usuariosObs);
        listaUsuarios.setCellFactory(v -> new ListCell<>() {
            @Override
            protected void updateItem(Usuarios u, boolean empty) {
                super.updateItem(u, empty);
                if (empty || u == null) { setText(null); return; }
                String nome = (u.getNomeCompleto() != null && !u.getNomeCompleto().isBlank())
                        ? u.getNomeCompleto()
                        : (u.getEmail() != null ? u.getEmail() : "(sem nome)");
                setText((u.isOnline() ? "● " : "○ ") + nome);
            }
        });
        listaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null && usuarioLogado != null && sel.getId() != usuarioLogado.getId()) {
                destinatarioAtual = sel;
                abrirOuTrocarConversaCom(destinatarioAtual);
            }
        });

        // Conversas
        listaConversas.setItems(conversasObs);
        listaConversas.setCellFactory(v -> new ListCell<>() {
            @Override
            protected void updateItem(Conversation c, boolean empty) {
                super.updateItem(c, empty);
                if (empty || c == null) { setText(null); return; }
                setText("Conversa #" + c.getId());
            }
        });
        listaConversas.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                conversaAtual = sel;
                carregarMensagens();
                atualizarEntregueELida();
            }
        });

        // Busca de usuários
        if (campoBuscaUsuario != null) {
            campoBuscaUsuario.textProperty().addListener((obs, old, val) -> carregarUsuarios(val));
        }

        // “digitando...” com debounce
        if (campoMensagem != null) {
            PauseTransition typingDebounce = new PauseTransition(Duration.millis(400));
            campoMensagem.textProperty().addListener((obs, old, val) -> {
                typingDebounce.stop();
                typingDebounce.setOnFinished(e -> setTyping(false));
                setTyping(true);
                typingDebounce.playFromStart();
            });
        }

        // Habilitar botão Enviar
        if (btnEnviar != null && campoMensagem != null) {
            btnEnviar.setDisable(true);
            campoMensagem.textProperty().addListener((o, old, val) ->
                    btnEnviar.setDisable(val == null || val.isBlank())
            );
        }
    }

    // ===== Carregamentos =====
    private void carregarListasIniciais() {
        carregarUsuarios(null);
        carregarConversas();
    }

    private void carregarUsuarios(String filtro) {
        try {
            final List<Usuarios> lista = (filtro == null || filtro.isBlank())
                    ? usuariosDAO.listarTodos()
                    : usuariosDAO.buscarPorTermo(filtro);

            if (usuarioLogado != null) {
                long idLogado = usuarioLogado.getId();
                lista.removeIf(u -> u != null && u.getId() == idLogado);
            }
            usuariosObs.setAll(lista);
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar usuários: " + e.getMessage());
        }
    }

    private void carregarConversas() {
        if (usuarioLogado == null) return;
        try {
            conversasObs.setAll(chatDAO.listConversationsForUser(usuarioLogado.getId()));
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar conversas: " + e.getMessage());
        }
    }

    private void abrirOuTrocarConversaCom(Usuarios destino) {
        if (usuarioLogado == null || destino == null) return;
        try {
            long convId = chatDAO.getOrCreateDirectConversation(usuarioLogado.getId(), destino.getId());

            Conversation c = new Conversation();
            c.setId(convId);
            conversaAtual = c;

            String nome = (destino.getNomeCompleto() != null && !destino.getNomeCompleto().isBlank())
                    ? destino.getNomeCompleto()
                    : (destino.getEmail() != null ? destino.getEmail() : "(sem nome)");
            if (labelDestinatario != null) labelDestinatario.setText(nome);

            carregarConversas();
            carregarMensagens();
            atualizarEntregueELida();
        } catch (SQLException e) {
            mostrarErro("Erro ao abrir conversa: " + e.getMessage());
        }
    }

    private void carregarMensagens() {
        if (conversaAtual == null) {
            mensagensObs.clear();
            return;
        }
        try {
            List<ChatMessage> msgs = chatDAO.listMessages(conversaAtual.getId(), 200);
            mensagensObs.setAll(msgs);
            if (!msgs.isEmpty()) listaMensagens.scrollTo(msgs.size() - 1);
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar mensagens: " + e.getMessage());
        }
    }

    private void atualizarEntregueELida() {
        if (conversaAtual == null || usuarioLogado == null) return;
        try {
            chatDAO.markDelivered(conversaAtual.getId(), usuarioLogado.getId());
            chatDAO.markRead(conversaAtual.getId(), usuarioLogado.getId());
        } catch (SQLException ignored) { }
    }

    // ===== Ações =====
    @FXML
    public void enviarMensagem() {
        if (usuarioLogado == null || conversaAtual == null) return;

        String texto = (campoMensagem != null) ? campoMensagem.getText() : null;
        if (texto == null || texto.isBlank()) return;

        ChatMessage nova = new ChatMessage(conversaAtual.getId(), usuarioLogado.getId(), texto.trim());
        nova.setStatus(MessageStatus.SENT);
        try {
            chatDAO.sendMessage(nova);
            if (campoMensagem != null) campoMensagem.clear();
            carregarMensagens();
            carregarConversas();
        } catch (SQLException e) {
            mostrarErro("Erro ao enviar mensagem: " + e.getMessage());
        }
    }

    private void setTyping(boolean typing) {
        if (conversaAtual == null || usuarioLogado == null) return;
        try {
            chatDAO.setTyping(conversaAtual.getId(), usuarioLogado.getId(), typing);
        } catch (SQLException ignored) { }
    }

    // ===== Polling =====
    private void iniciarPolling() {
        if (pollingService != null) return;

        pollingService = new ScheduledService<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {
                        try {
                            if (conversaAtual != null && usuarioLogado != null) {

                                boolean someoneTyping =
                                        chatDAO.someoneTyping(conversaAtual.getId(), usuarioLogado.getId());
                                Platform.runLater(() -> {
                                    if (labelTyping != null) {
                                        labelTyping.setText(someoneTyping ? "digitando..." : "");
                                    }
                                });

                                List<ChatMessage> msgs = chatDAO.listMessages(conversaAtual.getId(), 200);
                                Platform.runLater(() -> {
                                    mensagensObs.setAll(msgs);
                                    if (!msgs.isEmpty()) listaMensagens.scrollTo(msgs.size() - 1);
                                });

                                chatDAO.markDelivered(conversaAtual.getId(), usuarioLogado.getId());
                            }

                            if (usuarioLogado != null) {
                                List<Conversation> cls = chatDAO.listConversationsForUser(usuarioLogado.getId());
                                Platform.runLater(() -> conversasObs.setAll(cls));
                            }

                            Platform.runLater(() ->
                                    carregarUsuarios(campoBuscaUsuario != null ? campoBuscaUsuario.getText() : null)
                            );
                        } catch (SQLException ignored) { }
                        return null;
                    }
                };
            }
        };

        pollingService.setPeriod(Duration.seconds(2.5));
        pollingService.setRestartOnFailure(true);
        pollingService.start();
    }

    // ===== Util =====
    private String statusToTicks(MessageStatus st) {
        if (st == null) return "";
        return switch (st) {
            case SENT      -> "✓";
            case DELIVERED -> "✓✓";
            case READ      -> "✓✓ (lida)";
        };
    }

    private void mostrarErro(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText("Módulo de Chat");
        a.setContentText(msg);
        a.showAndWait();
    }
}
