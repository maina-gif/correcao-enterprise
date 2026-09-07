package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;
import com.wayne.wayneen.enterpriseswyne.model.Treinamento;
import com.wayne.wayneen.enterpriseswyne.model.Treinamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TreinamentoDAO {

    public void inserir(Treinamento t) {
        String sql = "INSERT INTO treinamentos (titulo, tipo, local, data) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, t.getTitulo());
            stmt.setString(2, t.getTipo());
            stmt.setString(3, t.getLocal());
            stmt.setDate(4, Date.valueOf(t.getData()));
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Treinamento> listar() {
        List<Treinamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM treinamentos";

        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Treinamento t = new Treinamento();
                t.setId(rs.getInt("id"));
                t.setTitulo(rs.getString("titulo"));
                t.setTipo(rs.getString("tipo"));
                t.setLocal(rs.getString("local"));
                t.setData(rs.getDate("data").toLocalDate());
                lista.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void atualizar(Treinamento t) {
        String sql = "UPDATE treinamentos SET titulo=?, tipo=?, local=?, data=? WHERE id=?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, t.getTitulo());
            stmt.setString(2, t.getTipo());
            stmt.setString(3, t.getLocal());
            stmt.setDate(4, Date.valueOf(t.getData()));
            stmt.setInt(5, t.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM treinamentos WHERE id=?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
