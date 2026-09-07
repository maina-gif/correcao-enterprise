package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.Cargo;
import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CargoDAO {
    public void inserir(Cargo cargo) {
        String sql = "INSERT INTO cargos (nome, salario_base, nivel) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cargo.getNome());
            stmt.setDouble(2, cargo.getSalarioBase());
            stmt.setString(3, cargo.getNivel());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizar(Cargo cargo) {
        String sql = "UPDATE cargos SET nome = ?, salario_base = ?, nivel = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cargo.getNome());
            stmt.setDouble(2, cargo.getSalarioBase());
            stmt.setString(3, cargo.getNivel());
            stmt.setInt(4, cargo.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM cargos WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Cargo> listar() {
        List<Cargo> lista = new ArrayList<>();
        String sql = "SELECT * FROM cargos";
        try (Connection conn = ConnectionFactory.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cargo c = new Cargo();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setSalarioBase(rs.getDouble("salario_base"));
                c.setNivel(rs.getString("nivel"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
