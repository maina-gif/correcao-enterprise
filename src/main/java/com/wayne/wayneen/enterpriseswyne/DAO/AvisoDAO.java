package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.Aviso;
import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvisoDAO {
    public static void salvar(Aviso aviso) throws SQLException {
        String sql = "INSERT INTO avisos (titulo, descricao, data, tipo) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, aviso.getTitulo());
            stmt.setString(2, aviso.getDescricao());
            stmt.setDate(3, Date.valueOf(aviso.getData()));
            stmt.setString(4, aviso.getTipo());
            stmt.executeUpdate();
        }
    }

    public static List<Aviso> listarTodos() throws SQLException {
        List<Aviso> avisos = new ArrayList<>();
        String sql = "SELECT * FROM avisos ORDER BY data DESC";
        try (Connection conn = ConnectionFactory.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Aviso aviso = new Aviso(rs.getInt("id"), rs.getString("titulo"), rs.getString("descricao"), rs.getDate("data").toLocalDate(), rs.getString("tipo"));
                aviso.setId(rs.getInt("id"));
                aviso.setTitulo(rs.getString("titulo"));
                aviso.setDescricao(rs.getString("descricao"));
                aviso.setData(rs.getDate("data").toLocalDate());
                aviso.setTipo(rs.getString("tipo"));
                avisos.add(aviso);
            }
        }
        return avisos;
    }

    public static void excluir(int id) throws SQLException {
        String sql = "DELETE FROM avisos WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}