package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;
import com.wayne.wayneen.enterpriseswyne.model.Notificacao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificacaoDAO {

    private static final int LIMITE_MENSAGEM = 280;

    /** Valida e sanitiza a mensagem (trim + limite). */
    private String saneMensagem(String mensagem) {
        String m = (mensagem == null) ? "" : mensagem.trim();
        if (m.length() > LIMITE_MENSAGEM) {
            m = m.substring(0, LIMITE_MENSAGEM);
        }
        return m;
    }

    /** Converte Timestamp -> LocalDateTime com segurança. */
    private LocalDateTime toLdt(Timestamp ts) {
        return (ts == null) ? null : ts.toLocalDateTime();
    }

    /** Mapeia uma linha do ResultSet para o objeto Notificacao. */
    private Notificacao mapRow(ResultSet rs) throws SQLException {
        return new Notificacao(
                rs.getLong("id"),
                rs.getString("mensagem"),
                toLdt(rs.getTimestamp("data_hora")),
                rs.getBoolean("lida")
        );
    }

    /** Insere a notificação e preenche o ID gerado. */
    public void inserir(Notificacao n) throws SQLException {
        final String sql = "INSERT INTO notificacoes (mensagem, data_hora, lida) VALUES (?, ?, ?)";
        if (n == null) {
            throw new IllegalArgumentException("Notificacao nula.");
        }
        String mensagem = saneMensagem(n.getMensagem());
        LocalDateTime dataHora = (n.getDataHora() == null) ? LocalDateTime.now() : n.getDataHora();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, mensagem);
            ps.setTimestamp(2, Timestamp.valueOf(dataHora));
            ps.setBoolean(3, n.isLida());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) n.setId(rs.getLong(1));
            }
        }
    }

    /** Utilitário rápido: cria com data/hora = agora e lida=false. */
    public void inserirMensagem(String mensagem) throws SQLException {
        Notificacao n = new Notificacao(null, saneMensagem(mensagem), LocalDateTime.now(), false);
        inserir(n);
    }

    /** Lista todas as notificações (mais recentes primeiro). */
    public List<Notificacao> listarTodas() throws SQLException {
        final String sql = "SELECT id, mensagem, data_hora, lida FROM notificacoes ORDER BY data_hora DESC";
        List<Notificacao> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    /** Lista com paginação. */
    public List<Notificacao> listarPaginado(int limit, int offset) throws SQLException {
        final String sql = "SELECT id, mensagem, data_hora, lida FROM notificacoes " +
                "ORDER BY data_hora DESC LIMIT ? OFFSET ?";
        List<Notificacao> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Math.max(1, limit));
            ps.setInt(2, Math.max(0, offset));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        }
        return lista;
    }

    /** Marca uma notificação como lida; retorna linhas afetadas (0/1). */
    public int marcarComoLida(long id) throws SQLException {
        final String sql = "UPDATE notificacoes SET lida = 1 WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate();
        }
    }

    /** Marca todas como lidas; retorna quantidade afetada. */
    public int marcarTodasComoLidas() throws SQLException {
        final String sql = "UPDATE notificacoes SET lida = 1 WHERE lida = 0";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            return ps.executeUpdate();
        }
    }

    /** Conta não lidas. */
    public int contarNaoLidas() throws SQLException {
        final String sql = "SELECT COUNT(*) FROM notificacoes WHERE lida = 0";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /** Deleta por ID; retorna linhas afetadas (0/1). */
    public int deletar(long id) throws SQLException {
        final String sql = "DELETE FROM notificacoes WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate();
        }
    }

    /** Busca por ID (útil para confirmar operações). */
    public Notificacao buscarPorId(long id) throws SQLException {
        final String sql = "SELECT id, mensagem, data_hora, lida FROM notificacoes WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    /** Atualiza somente o campo lida; retorna linhas afetadas (0/1). */
    public int atualizarLida(long id, boolean lida) throws SQLException {
        final String sql = "UPDATE notificacoes SET lida = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, lida);
            ps.setLong(2, id);
            return ps.executeUpdate();
        }
    }

    /** Atualiza a mensagem (aplica limite) e opcionalmente a data/hora. */
    public int atualizarMensagem(long id, String novaMensagem, LocalDateTime novaDataHora) throws SQLException {
        final boolean alterarData = (novaDataHora != null);
        final String sql = alterarData
                ? "UPDATE notificacoes SET mensagem = ?, data_hora = ? WHERE id = ?"
                : "UPDATE notificacoes SET mensagem = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, saneMensagem(novaMensagem));
            if (alterarData) {
                ps.setTimestamp(2, Timestamp.valueOf(novaDataHora));
                ps.setLong(3, id);
            } else {
                ps.setLong(2, id);
            }
            return ps.executeUpdate();
        }
    }

    /** Lista somente não lidas (útil para badget/contador). */
    public List<Notificacao> listarNaoLidas() throws SQLException {
        final String sql = "SELECT id, mensagem, data_hora, lida FROM notificacoes WHERE lida = 0 ORDER BY data_hora DESC";
        List<Notificacao> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        }
        return lista;
    }
}
