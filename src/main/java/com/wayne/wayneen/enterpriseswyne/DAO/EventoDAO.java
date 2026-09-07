package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;
import com.wayne.wayneen.enterpriseswyne.Evento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {

    public static boolean salvar(Evento evento) {
        String sql = "INSERT INTO eventos (titulo, descricao, data, local, tipo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, evento.getTitulo());
            stmt.setString(2, evento.getDescricao());
            stmt.setDate(3, Date.valueOf(evento.getData()));
            stmt.setString(4, evento.getLocal());
            stmt.setString(5, evento.getTipo());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        evento.setId(rs.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao salvar evento: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public static List<Evento> listar() {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT * FROM eventos ORDER BY data ASC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Evento evento = new Evento();

                evento.setId(rs.getInt("id"));
                evento.setTitulo(rs.getString("titulo"));
                evento.setDescricao(rs.getString("descricao"));

                Date dataEvento = rs.getDate("data");
                if (dataEvento != null) {
                    evento.setData(dataEvento.toLocalDate());
                } else {
                    evento.setData(null);
                }

                evento.setLocal(rs.getString("local"));
                evento.setTipo(rs.getString("tipo"));

                eventos.add(evento);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar eventos: " + e.getMessage());
            e.printStackTrace();
        }

        return eventos;
    }

    public static List<Evento> listarTodos() {
        return listar(); // reutiliza o método anterior
    }

    public static boolean atualizar(Evento evento) {
        String sql = "UPDATE eventos SET titulo = ?, descricao = ?, data = ?, local = ?, tipo = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, evento.getTitulo());
            stmt.setString(2, evento.getDescricao());
            stmt.setDate(3, Date.valueOf(evento.getData()));
            stmt.setString(4, evento.getLocal());
            stmt.setString(5, evento.getTipo());
            stmt.setInt(6, evento.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar evento: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public static boolean excluir(int id) {
        String sql = "DELETE FROM eventos WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir evento: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}
