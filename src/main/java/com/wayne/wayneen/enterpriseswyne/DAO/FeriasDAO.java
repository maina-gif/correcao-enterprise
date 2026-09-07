package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;
import com.wayne.wayneen.enterpriseswyne.model.Ferias;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeriasDAO {

    public static void salvar(Ferias f) {
        final String sql = "INSERT INTO ferias (funcionario_id, data_inicio, data_fim, observacao) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, f.getFuncionarioId());
            stmt.setDate(2, Date.valueOf(f.getDataInicio()));
            stmt.setDate(3, Date.valueOf(f.getDataFim()));
            stmt.setString(4, f.getObservacao());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void atualizar(Ferias f) {
        final String sql = "UPDATE ferias SET funcionario_id = ?, data_inicio = ?, data_fim = ?, observacao = ? " +
                "WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, f.getFuncionarioId());
            stmt.setDate(2, Date.valueOf(f.getDataInicio()));
            stmt.setDate(3, Date.valueOf(f.getDataFim()));
            stmt.setString(4, f.getObservacao());
            stmt.setInt(5, f.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Ferias> listarPorFuncionario(int funcionarioId) {
        final String sql = "SELECT id, funcionario_id, data_inicio, data_fim, observacao " +
                "FROM ferias WHERE funcionario_id = ? ORDER BY data_inicio ASC";

        List<Ferias> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, funcionarioId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(map(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /** Compatível com o PDFGenerator (lista todos os períodos do funcionário). */
    public static List<Ferias> buscarTodosPorFuncionarioId(int funcionarioId) {
        return listarPorFuncionario(funcionarioId);
    }

    public static List<Ferias> listarTodas() {
        final String sql = "SELECT id, funcionario_id, data_inicio, data_fim, observacao " +
                "FROM ferias ORDER BY funcionario_id, data_inicio ASC";

        List<Ferias> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static void excluir(int id) {
        final String sql = "DELETE FROM ferias WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Retorna o período mais recente do funcionário (ordem decrescente por data de início). */
    public static Ferias buscarPorFuncionarioId(int funcionarioId) {
        final String sql = "SELECT id, funcionario_id, data_inicio, data_fim, observacao " +
                "FROM ferias WHERE funcionario_id = ? " +
                "ORDER BY data_inicio DESC LIMIT 1";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, funcionarioId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Ferias buscarPorId(int id) {
        final String sql = "SELECT id, funcionario_id, data_inicio, data_fim, observacao " +
                "FROM ferias WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ---------- Helpers ----------

    private static Ferias map(ResultSet rs) throws SQLException {
        Ferias f = new Ferias();
        f.setId(rs.getInt("id"));
        f.setFuncionarioId(rs.getInt("funcionario_id"));

        Date di = rs.getDate("data_inicio");
        Date df = rs.getDate("data_fim");
        f.setDataInicio(di != null ? di.toLocalDate() : null);
        f.setDataFim(df != null ? df.toLocalDate() : null);

        f.setObservacao(rs.getString("observacao"));
        return f;
    }
}
