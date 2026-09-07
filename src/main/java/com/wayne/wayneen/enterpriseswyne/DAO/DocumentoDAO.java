package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;
import com.wayne.wayneen.enterpriseswyne.Documento;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DocumentoDAO {

    public static boolean salvar(Documento doc) {
        String sql = "INSERT INTO documentos (titulo, tipo, data_validade, caminho_arquivo, funcionario_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, doc.getTitulo());
            stmt.setString(2, doc.getTipo());
            if (doc.getDataValidade() != null) {
                stmt.setDate(3, Date.valueOf(doc.getDataValidade()));
            } else {
                stmt.setNull(3, Types.DATE);
            }
            stmt.setString(4, doc.getCaminhoArquivo());
            stmt.setInt(5, doc.getFuncionarioId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean atualizar(Documento doc) {
        String sql = "UPDATE documentos SET titulo = ?, tipo = ?, data_validade = ?, caminho_arquivo = ?, funcionario_id = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, doc.getTitulo());
            stmt.setString(2, doc.getTipo());
            if (doc.getDataValidade() != null) {
                stmt.setDate(3, Date.valueOf(doc.getDataValidade()));
            } else {
                stmt.setNull(3, Types.DATE);
            }
            stmt.setString(4, doc.getCaminhoArquivo());
            stmt.setInt(5, doc.getFuncionarioId());
            stmt.setInt(6, doc.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Documento> listarTodos() {
        String sql = "SELECT * FROM documentos";
        List<Documento> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapResultSetToDocumento(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static boolean excluir(int id) {
        String sql = "DELETE FROM documentos WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Documento> buscarPorFuncionario(int funcionarioId) {
        String sql = "SELECT * FROM documentos WHERE funcionario_id = ?";
        List<Documento> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, funcionarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToDocumento(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static List<Documento> buscarComFiltro(String termoFuncionario, LocalDate dataInicio, LocalDate dataFim) {
        StringBuilder sql = new StringBuilder(
                "SELECT d.* FROM documentos d JOIN funcionarios f ON d.funcionario_id = f.id WHERE 1=1 "
        );
        List<Documento> lista = new ArrayList<>();

        if (termoFuncionario != null && !termoFuncionario.isBlank()) {
            sql.append("AND (f.nome_completo LIKE ? OR f.id = ?) ");
        }
        if (dataInicio != null) sql.append("AND d.data_validade >= ? ");
        if (dataFim != null) sql.append("AND d.data_validade <= ? ");

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int i = 1;
            if (termoFuncionario != null && !termoFuncionario.isBlank()) {
                stmt.setString(i++, "%" + termoFuncionario + "%");
                try {
                    stmt.setInt(i++, Integer.parseInt(termoFuncionario));
                } catch (NumberFormatException ex) {
                    stmt.setNull(i++, Types.INTEGER);
                }
            }
            if (dataInicio != null) stmt.setDate(i++, Date.valueOf(dataInicio));
            if (dataFim != null) stmt.setDate(i++, Date.valueOf(dataFim));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToDocumento(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static List<Documento> listarVencidos() {
        String sql = "SELECT * FROM documentos WHERE data_validade < CURRENT_DATE";
        List<Documento> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToDocumento(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Mapeia dados do ResultSet para Documento
    private static Documento mapResultSetToDocumento(ResultSet rs) throws SQLException {
        Documento doc = new Documento();
        doc.setId(rs.getInt("id"));
        doc.setFuncionarioId(rs.getInt("funcionario_id"));
        doc.setTitulo(rs.getString("titulo"));
        doc.setTipo(rs.getString("tipo"));
        Date dt = rs.getDate("data_validade");
        doc.setDataValidade(dt != null ? dt.toLocalDate() : null);
        doc.setCaminhoArquivo(rs.getString("caminho_arquivo"));
        return doc;
    }
}
