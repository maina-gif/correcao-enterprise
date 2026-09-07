package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.Avaliacao;
import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AvaliacaoDAO {

    public static List<Avaliacao> filtrarAvaliacoes(Integer idFuncionario, LocalDate dataInicio, LocalDate dataFim) {
        List<Avaliacao> avaliacoes = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM avaliacoes WHERE 1=1");

        if (idFuncionario != null) {
            sql.append(" AND funcionario_id = ?");
        }
        if (dataInicio != null) {
            sql.append(" AND data_avaliacao >= ?");
        }
        if (dataFim != null) {
            sql.append(" AND data_avaliacao <= ?");
        }

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            if (idFuncionario != null) {
                stmt.setInt(paramIndex++, idFuncionario);
            }
            if (dataInicio != null) {
                stmt.setDate(paramIndex++, Date.valueOf(dataInicio));
            }
            if (dataFim != null) {
                stmt.setDate(paramIndex++, Date.valueOf(dataFim));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Avaliacao av = new Avaliacao();
                av.setId(rs.getInt("id"));
                av.setFuncionarioId(rs.getInt("funcionario_id"));
                av.setDataAvaliacao(rs.getDate("data_avaliacao").toLocalDate());
                av.setPontualidade(rs.getInt("pontualidade"));
                av.setProdutividade(rs.getInt("produtividade"));
                av.setTrabalhoEquipe(rs.getInt("trabalho_equipe"));
                av.setObservacoes(rs.getString("observacoes"));

                avaliacoes.add(av);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao filtrar avaliações: " + e.getMessage());
        }

        return avaliacoes;
    }

    public static void salvar(Avaliacao a) {
        String sql = "INSERT INTO avaliacoes (funcionario_id, data_avaliacao, pontualidade, produtividade, trabalho_equipe, observacoes) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, a.getFuncionarioId());
            stmt.setDate(2, Date.valueOf(a.getDataAvaliacao()));
            stmt.setInt(3, a.getPontualidade());
            stmt.setInt(4, a.getProdutividade());
            stmt.setInt(5, a.getTrabalhoEquipe());
            stmt.setString(6, a.getObservacoes());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao salvar avaliação: " + e.getMessage());
        }
    }

    public static List<Avaliacao> listarTodas() {
        List<Avaliacao> avaliacoes = new ArrayList<>();
        String sql = "SELECT * FROM avaliacoes";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Avaliacao av = new Avaliacao();
                av.setId(rs.getInt("id"));
                av.setFuncionarioId(rs.getInt("funcionario_id"));
                av.setDataAvaliacao(rs.getDate("data_avaliacao").toLocalDate());
                av.setPontualidade(rs.getInt("pontualidade"));
                av.setProdutividade(rs.getInt("produtividade"));
                av.setTrabalhoEquipe(rs.getInt("trabalho_equipe"));
                av.setObservacoes(rs.getString("observacoes"));

                avaliacoes.add(av);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar todas as avaliações: " + e.getMessage());
        }

        return avaliacoes;
    }
}
