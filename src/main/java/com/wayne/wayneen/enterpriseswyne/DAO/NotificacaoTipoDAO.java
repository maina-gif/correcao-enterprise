package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;
import com.wayne.wayneen.enterpriseswyne.model.NotificacaoTipoEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificacaoTipoDAO {

    public void inserir(NotificacaoTipoEntity t) throws SQLException {
        String sql = "INSERT INTO notificacao_tipo (codigo, nome, cor_hex, ordem, ativo) VALUES (?,?,?,?,?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getCodigo());
            ps.setString(2, t.getNome());
            ps.setString(3, t.getCorHex());
            ps.setObject(4, t.getOrdem(), Types.INTEGER);
            ps.setBoolean(5, t.isAtivo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) t.setId(rs.getLong(1));
            }
        }
    }

    public void atualizar(NotificacaoTipoEntity t) throws SQLException {
        String sql = "UPDATE notificacao_tipo SET codigo=?, nome=?, cor_hex=?, ordem=?, ativo=? WHERE id=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getCodigo());
            ps.setString(2, t.getNome());
            ps.setString(3, t.getCorHex());
            ps.setObject(4, t.getOrdem(), Types.INTEGER);
            ps.setBoolean(5, t.isAtivo());
            ps.setLong(6, t.getId());
            ps.executeUpdate();
        }
    }

    public void deletar(long id) throws SQLException {
        String sql = "DELETE FROM notificacao_tipo WHERE id=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public NotificacaoTipoEntity buscarPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT id, codigo, nome, cor_hex, ordem, ativo FROM notificacao_tipo WHERE codigo = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public List<NotificacaoTipoEntity> listarTodos(boolean apenasAtivos) throws SQLException {
        String sql = "SELECT id, codigo, nome, cor_hex, ordem, ativo FROM notificacao_tipo "
                + (apenasAtivos ? "WHERE ativo = 1 " : "")
                + "ORDER BY COALESCE(ordem, 999), nome";
        List<NotificacaoTipoEntity> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        }
        return lista;
    }

    public void ativar(long id, boolean ativo) throws SQLException {
        String sql = "UPDATE notificacao_tipo SET ativo=? WHERE id=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, ativo);
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }

    /** Semeia INFO/WARN/ERROR se não existirem. Chame no bootstrap do app. */
    public void garantirDefaults() throws SQLException {
        if (buscarPorCodigo("INFO") == null) {
            inserir(new NotificacaoTipoEntity(null, "INFO", "Informação", "#2196F3", 1, true));
        }
        if (buscarPorCodigo("WARN") == null) {
            inserir(new NotificacaoTipoEntity(null, "WARN", "Atenção", "#FFC107", 2, true));
        }
        if (buscarPorCodigo("ERROR") == null) {
            inserir(new NotificacaoTipoEntity(null, "ERROR", "Erro", "#E53935", 3, true));
        }
    }

    private NotificacaoTipoEntity map(ResultSet rs) throws SQLException {
        return new NotificacaoTipoEntity(
                rs.getLong("id"),
                rs.getString("codigo"),
                rs.getString("nome"),
                rs.getString("cor_hex"),
                (Integer) rs.getObject("ordem"),
                rs.getBoolean("ativo")
        );
    }
}
