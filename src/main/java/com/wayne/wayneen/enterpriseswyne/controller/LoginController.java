package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.LogDAO;
import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;
import com.wayne.wayneen.enterpriseswyne.model.SessionManager;
import com.wayne.wayneen.enterpriseswyne.model.Usuarios;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class LoginController {

    @FXML private TextField usuarioField;
    @FXML private PasswordField senhaField;

    /**
     * Handler do botão de login no FXML (onAction="#realizarLogin").
     * Autentica, guarda o usuário na sessão e registra o log com o objeto Usuarios.
     */
    @FXML
    public void realizarLogin() {
        final String usuario = usuarioField.getText();
        final String senha = senhaField.getText();

        if (usuario == null || usuario.isBlank() || senha == null || senha.isBlank()) {
            mostrarAlerta("Erro", "Informe usuário e senha.", Alert.AlertType.ERROR);
            return;
        }

        // Busca só pelo login (usuario); a senha é conferida em Java contra o hash,
        // nunca comparada em texto puro dentro do SQL.
        final String sql = "SELECT * FROM usuarios WHERE usuario = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && com.wayne.wayneen.enterpriseswyne.model.PasswordUtil.verifica(
                        senha, getStringSafe(rs, "senha"))) {
                    // Monta objeto Usuarios a partir das colunas disponíveis
                    long id = getLongSafe(rs, "id", 0L);
                    String nome = firstNonBlank(
                            getStringSafe(rs, "nome_completo"),
                            getStringSafe(rs, "nome"),
                            getStringSafe(rs, "usuario"),
                            getStringSafe(rs, "email"),
                            "Usuario#" + id
                    );
                    String email = getStringSafe(rs, "email");

                    Usuarios u = new Usuarios(id, nome);
                    if (email != null) u.setEmail(email);

                    // Guarda na sessão e registra log (OPÇÃO A: passa o objeto Usuarios)
                    SessionManager.setUsuarioLogado(u);
                    LogDAO.registrar(u, "[Login] Usuário autenticado com sucesso");

                    abrirTelaPrincipal();
                } else {
                    mostrarAlerta("Erro", "Usuário ou senha inválidos", Alert.AlertType.ERROR);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao autenticar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /* ================= Helpers de UI ================= */

    private void abrirTelaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/wayne/wayneen/enterpriseswyne/painel_geral.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("Sistema - Wayne Enterprises");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            // Fecha a janela de login usando qualquer controle da cena atual
            Stage loginStage = (Stage) usuarioField.getScene().getWindow();
            loginStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao abrir tela principal: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /* ================= Helpers de ResultSet (tolerantes a schema) ================= */

    private static String getStringSafe(ResultSet rs, String col) {
        try {
            if (hasColumn(rs, col)) {
                String v = rs.getString(col);
                return (v != null && !v.isBlank()) ? v : null;
            }
        } catch (Exception ignored) {}
        return null;
    }

    private static long getLongSafe(ResultSet rs, String col, long def) {
        try {
            if (hasColumn(rs, col)) {
                return rs.getLong(col);
            }
        } catch (Exception ignored) {}
        return def;
    }

    private static boolean hasColumn(ResultSet rs, String column) {
        try {
            ResultSetMetaData md = rs.getMetaData();
            int cols = md.getColumnCount();
            for (int i = 1; i <= cols; i++) {
                if (column.equalsIgnoreCase(md.getColumnLabel(i)) ||
                        column.equalsIgnoreCase(md.getColumnName(i))) {
                    return true;
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    private static String firstNonBlank(String... vals) {
        if (vals == null) return null;
        for (String v : vals) {
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }
}
