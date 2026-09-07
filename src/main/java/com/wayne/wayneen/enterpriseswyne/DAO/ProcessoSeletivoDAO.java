package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.ProcessoSeletivo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProcessoSeletivoDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/wayne_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String SENHA = "";

    public static List<ProcessoSeletivo> listarTodos() throws SQLException {
        String sql = "SELECT id, titulo, descricao, data_inicio, data_fim FROM processos_seletivos ORDER BY data_inicio DESC";
        List<ProcessoSeletivo> lista = new ArrayList<>();

        try (Connection c = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProcessoSeletivo p = new ProcessoSeletivo();
                p.setId(rs.getInt("id"));
                p.setTitulo(rs.getString("titulo"));
                p.setDescricao(rs.getString("descricao"));
                Date di = rs.getDate("data_inicio");
                Date df = rs.getDate("data_fim");
                if (di != null) p.setDataInicio(di.toLocalDate());
                if (df != null) p.setDataFim(df.toLocalDate());
                lista.add(p);
            }
        }
        return lista;
    }

    public static void inserir(ProcessoSeletivo p) throws SQLException {
        String sql = "INSERT INTO processos_seletivos (titulo, descricao, data_inicio, data_fim) VALUES (?,?,?,?)";
        try (Connection c = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getTitulo());
            ps.setString(2, p.getDescricao());
            ps.setDate(3, p.getDataInicio() != null ? Date.valueOf(p.getDataInicio()) : null);
            ps.setDate(4, p.getDataFim() != null ? Date.valueOf(p.getDataFim()) : null);
            ps.executeUpdate();
        }
    }

    public static void excluir(int id) throws SQLException {
        String sql = "DELETE FROM processos_seletivos WHERE id = ?";
        try (Connection c = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public static void atualizar(ProcessoSeletivo p) throws SQLException {
        String sql = "UPDATE processos_seletivos SET titulo=?, descricao=?, data_inicio=?, data_fim=? WHERE id=?";
        try (Connection c = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getTitulo());
            ps.setString(2, p.getDescricao());
            ps.setDate(3, p.getDataInicio() != null ? Date.valueOf(p.getDataInicio()) : null);
            ps.setDate(4, p.getDataFim() != null ? Date.valueOf(p.getDataFim()) : null);
            ps.setInt(5, p.getId());
            ps.executeUpdate();
        }
    }
}
