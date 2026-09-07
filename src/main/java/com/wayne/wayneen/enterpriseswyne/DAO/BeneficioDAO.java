package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.Beneficio;
import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BeneficioDAO {

    public void inserir(Beneficio beneficio) {
        String sql = "INSERT INTO beneficios (tipo, valor, status) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, beneficio.getTipo());
            stmt.setDouble(2, beneficio.getValor());
            stmt.setString(3, beneficio.getStatus());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Beneficio> listar() {
        List<Beneficio> lista = new ArrayList<>();
        String sql = "SELECT * FROM beneficios";

        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Beneficio b = new Beneficio();
                b.setId(rs.getInt("id"));
                b.setTipo(rs.getString("tipo"));
                b.setValor(rs.getDouble("valor"));
                b.setStatus(rs.getString("status"));

                lista.add(b);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void atualizar(Beneficio beneficio) {
        String sql = "UPDATE beneficios SET tipo = ?, valor = ?, status = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, beneficio.getTipo());
            stmt.setDouble(2, beneficio.getValor());
            stmt.setString(3, beneficio.getStatus());
            stmt.setInt(4, beneficio.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM beneficios WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
