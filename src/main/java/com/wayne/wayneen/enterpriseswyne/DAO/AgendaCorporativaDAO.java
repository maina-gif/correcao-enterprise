package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.AgendaCorporativa;
import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgendaCorporativaDAO {

    public static void inserir(AgendaCorporativa evento) {
        String sql = "INSERT INTO eventos_corporativos (titulo, descricao, data_evento, tipo_evento, local, responsavel) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, evento.getTitulo());
            stmt.setString(2, evento.getDescricao());
            stmt.setDate(3, Date.valueOf(evento.getDataEvento()));
            stmt.setString(4, evento.getTipoEvento());
            stmt.setString(5, evento.getLocal());
            stmt.setString(6, evento.getResponsavel());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<AgendaCorporativa> listarTodos() {
        List<AgendaCorporativa> lista = new ArrayList<>();
        String sql = "SELECT * FROM eventos_corporativos ORDER BY data_evento";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                AgendaCorporativa evento = new AgendaCorporativa();
                evento.setId(rs.getInt("id"));
                evento.setTitulo(rs.getString("titulo"));
                evento.setDescricao(rs.getString("descricao"));
                evento.setDataEvento(rs.getDate("data_evento").toLocalDate());
                evento.setTipoEvento(rs.getString("tipo_evento"));
                evento.setLocal(rs.getString("local"));
                evento.setResponsavel(rs.getString("responsavel"));
                lista.add(evento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static void excluir(int id) {
        String sql = "DELETE FROM eventos_corporativos WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
