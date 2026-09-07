package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;
import com.wayne.wayneen.enterpriseswyne.Equipamento;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class EquipamentoDAO {

    /**
     * Salva um novo equipamento no banco de dados.
     */
    public static void salvar(Equipamento e) {
        String sql = "INSERT INTO equipamentos (tipo, numero_serie, funcionario_responsavel, status, data_aquisicao) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, e.getTipo());
            stmt.setString(2, e.getNumeroSerie());
            stmt.setString(3, e.getFuncionarioResponsavel());
            stmt.setString(4, e.getStatus());
            stmt.setDate(5, Date.valueOf(e.getDataAquisicao())); // ✅ LocalDate → java.sql.Date

            stmt.executeUpdate();

        } catch (SQLException ex) {
            System.err.println("Erro ao salvar equipamento:");
            ex.printStackTrace();
        }
    }

    /**
     * Lista todos os equipamentos do banco de dados.
     */
    public static List<Equipamento> listarTodos() {
        List<Equipamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM equipamentos";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Equipamento e = new Equipamento();
                e.setId(rs.getInt("id"));
                e.setTipo(rs.getString("tipo"));
                e.setNumeroSerie(rs.getString("numero_serie"));
                e.setFuncionarioResponsavel(rs.getString("funcionario_responsavel"));
                e.setStatus(rs.getString("status"));

                // ✅ Converte SQL Date para LocalDate
                Date dataSql = rs.getDate("data_aquisicao");
                e.setDataAquisicao(dataSql != null ? dataSql.toLocalDate() : null);

                lista.add(e);
            }

        } catch (SQLException ex) {
            System.err.println("Erro ao listar equipamentos:");
            ex.printStackTrace();
        }

        return lista;
    }

    /**
     * Atualiza os dados de um equipamento existente.
     */
    public static void atualizar(Equipamento e) {
        String sql = "UPDATE equipamentos SET tipo = ?, numero_serie = ?, funcionario_responsavel = ?, status = ?, data_aquisicao = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, e.getTipo());
            stmt.setString(2, e.getNumeroSerie());
            stmt.setString(3, e.getFuncionarioResponsavel());
            stmt.setString(4, e.getStatus());
            stmt.setDate(5, Date.valueOf(e.getDataAquisicao())); // ✅ LocalDate → java.sql.Date
            stmt.setInt(6, e.getId());

            stmt.executeUpdate();

        } catch (SQLException ex) {
            System.err.println("Erro ao atualizar equipamento:");
            ex.printStackTrace();
        }
    }
}
