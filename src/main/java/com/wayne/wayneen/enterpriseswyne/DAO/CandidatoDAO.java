package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;
import com.wayne.wayneen.enterpriseswyne.model.Candidato;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidatoDAO {

    public void inserir(Candidato candidato) {
        String sql = "INSERT INTO candidatos (nome, email, cargo_pretendido, link_curriculo) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, candidato.getNome());
            stmt.setString(2, candidato.getEmail());
            stmt.setString(3, candidato.getCargoPretendido());
            stmt.setString(4, candidato.getLinkCurriculo());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Candidato> listar() {
        List<Candidato> lista = new ArrayList<>();
        String sql = "SELECT * FROM candidatos";

        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Candidato c = new Candidato();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setEmail(rs.getString("email"));
                c.setCargoPretendido(rs.getString("cargo_pretendido"));
                c.setLinkCurriculo(rs.getString("link_curriculo"));
                lista.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void excluir(int id) {
        String sql = "DELETE FROM candidatos WHERE id=?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
