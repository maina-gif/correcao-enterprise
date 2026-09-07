package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;
import com.wayne.wayneen.enterpriseswyne.model.LogAcao;
import com.wayne.wayneen.enterpriseswyne.model.SessionManager;
import com.wayne.wayneen.enterpriseswyne.model.Usuarios;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class LogDAO {

    private LogDAO() { }

    /** Insere um registro de log usando o usuário atual da sessão. */
    public static void registrar(String descricao) {
        if (descricao == null || descricao.isBlank()) return;

        Usuarios usuario = SessionManager.getUsuarioLogado();
        String quem = (usuario != null) ? resolveUsuario(usuario) : "Sistema";

        final String sql = "INSERT INTO log_acoes (usuario, acao, momento) VALUES (?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, quem);
            stmt.setString(2, descricao.trim());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao registrar log: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Insere um registro de log com usuário explícito (útil para rotinas de sistema). */
    public static void registrar(Usuarios usuario, String descricao) {
        if (usuario == null || descricao == null || descricao.isBlank()) return;

        final String sql = "INSERT INTO log_acoes (usuario, acao, momento) VALUES (?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, resolveUsuario(usuario));
            stmt.setString(2, descricao.trim());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao registrar log (explícito): " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Variante para quando só temos o login/identificador textual e a descrição.
     * (Substitui o método vazio que havia no final do arquivo.)
     */
    public static void registrar(String login, String descricao, boolean usandoLoginDireto) {
        if (login == null || login.isBlank() || descricao == null || descricao.isBlank()) return;

        final String sql = "INSERT INTO log_acoes (usuario, acao, momento) VALUES (?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login.trim());
            stmt.setString(2, descricao.trim());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao registrar log (login+descricao): " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Lista todos os logs, mais recentes primeiro. */
    public static List<LogAcao> listarTodos() {
        List<LogAcao> logs = new ArrayList<>();
        final String sql = "SELECT usuario, acao, momento FROM log_acoes ORDER BY momento DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                logs.add(map(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar logs: " + e.getMessage());
            e.printStackTrace();
        }
        return logs;
    }

    /** Lista logs por período. */
    public static List<LogAcao> listarPorPeriodo(LocalDateTime de, LocalDateTime ate) {
        List<LogAcao> logs = new ArrayList<>();
        if (de == null || ate == null) return logs;

        final String sql =
                "SELECT usuario, acao, momento " +
                        "FROM log_acoes " +
                        "WHERE momento BETWEEN ? AND ? " +
                        "ORDER BY momento DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(de));
            stmt.setTimestamp(2, Timestamp.valueOf(ate));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    logs.add(map(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar logs por período: " + e.getMessage());
            e.printStackTrace();
        }
        return logs;
    }

    // ================= Helpers =================

    private static LogAcao map(ResultSet rs) throws SQLException {
        LogAcao log = new LogAcao();
        log.setUsuario(rs.getString("usuario"));
        log.setAcao(rs.getString("acao"));
        Timestamp ts = rs.getTimestamp("momento");
        log.setMomento(ts != null ? ts.toLocalDateTime() : LocalDateTime.now());
        return log;
    }

    /** Escolhe um identificador amigável do usuário para aparecer nos logs. */
    private static String resolveUsuario(Usuarios u) {
        if (u == null) return "desconhecido";
        String nome = u.getNomeCompleto();
        if (nome == null || nome.isBlank()) nome = u.getEmail();
        if (nome == null || nome.isBlank()) nome = "id=" + u.getId();
        return nome;
    }

    /**
     * Registra log genérico a partir de duas strings:
     * - 'origem'   → quem/qual módulo fez a ação (ex: "Funcionários", "Sistema", "Login")
     * - 'mensagem' → descrição da ação (ex: "Novo funcionário cadastrado")
     */
    public static void registrar(String origem, String mensagem) {
        if (origem == null || origem.isBlank() || mensagem == null || mensagem.isBlank()) {
            return; // não registra nada inválido
        }

        final String sql = "INSERT INTO log_acoes (usuario, acao, momento) VALUES (?, ?, CURRENT_TIMESTAMP)";

        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, origem.trim());
            stmt.setString(2, mensagem.trim());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao registrar log (Strings): " + e.getMessage());
            e.printStackTrace();
        }
    }

}
