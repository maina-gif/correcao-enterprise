package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.Chamado;
import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;

import java.sql.*;
import java.util.*;

public class ChamadoDAO {

    public static void salvar(Chamado c) {
        String sql = "INSERT INTO chamados (titulo, descricao, status, prioridade, funcionario, data_abertura) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getTitulo());
            stmt.setString(2, c.getDescricao());
            stmt.setString(3, c.getStatus());
            stmt.setString(4, c.getPrioridade());
            stmt.setString(5, c.getFuncionario());
            stmt.setString(6, c.getDataAbertura());
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Chamado> listarTodos() {
        List<Chamado> lista = new ArrayList<>();
        String sql = "SELECT * FROM chamados";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Chamado c = new Chamado();
                c.setId(rs.getInt("id"));
                c.setTitulo(rs.getString("titulo"));
                c.setDescricao(rs.getString("descricao"));
                c.setStatus(rs.getString("status"));
                c.setPrioridade(rs.getString("prioridade"));
                c.setFuncionario(rs.getString("funcionario"));
                c.setDataAbertura(rs.getString("data_abertura"));
                lista.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
