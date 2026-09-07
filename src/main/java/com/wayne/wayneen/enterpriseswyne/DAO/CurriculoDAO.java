package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;
import com.wayne.wayneen.enterpriseswyne.model.Curriculo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CurriculoDAO {

    public void inserir(Curriculo c) throws SQLException {
        String sql = "INSERT INTO curriculos (nome, email, telefone, cargo_desejado, skills, experiencia, escolaridade, linkedin, status_processo, caminho_pdf, data_cadastro) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getTelefone());
            ps.setString(4, c.getCargoDesejado());
            ps.setString(5, c.getSkills());
            ps.setString(6, c.getExperiencia());
            ps.setString(7, c.getEscolaridade());
            ps.setString(8, c.getLinkedin());
            ps.setString(9, c.getStatusProcesso());
            ps.setString(10, c.getCaminhoPdf());
            ps.setDate(11, Date.valueOf(c.getDataCadastro() != null ? c.getDataCadastro() : LocalDate.now()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setId(rs.getInt(1));
            }
        }
    }

    public void atualizar(Curriculo c) throws SQLException {
        String sql = "UPDATE curriculos SET nome=?, email=?, telefone=?, cargo_desejado=?, skills=?, experiencia=?, escolaridade=?, linkedin=?, status_processo=?, caminho_pdf=?, data_cadastro=? WHERE id=?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getTelefone());
            ps.setString(4, c.getCargoDesejado());
            ps.setString(5, c.getSkills());
            ps.setString(6, c.getExperiencia());
            ps.setString(7, c.getEscolaridade());
            ps.setString(8, c.getLinkedin());
            ps.setString(9, c.getStatusProcesso());
            ps.setString(10, c.getCaminhoPdf());
            ps.setDate(11, Date.valueOf(c.getDataCadastro() != null ? c.getDataCadastro() : LocalDate.now()));
            ps.setInt(12, c.getId());
            ps.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM curriculos WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Curriculo buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM curriculos WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public List<Curriculo> listarTodos() throws SQLException {
        String sql = "SELECT * FROM curriculos ORDER BY data_cadastro DESC, id DESC";
        List<Curriculo> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Curriculo> filtrar(String cargo, String status, LocalDate de, LocalDate ate, String buscaTexto) throws SQLException {
        StringBuilder sb = new StringBuilder("SELECT * FROM curriculos WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (cargo != null && !cargo.isBlank()) {
            sb.append(" AND cargo_desejado LIKE ?");
            params.add('%' + cargo.trim() + '%');
        }
        if (status != null && !status.isBlank() && !"TODOS".equalsIgnoreCase(status)) {
            sb.append(" AND status_processo = ?");
            params.add(status);
        }
        if (de != null) { sb.append(" AND data_cadastro >= ?"); params.add(Date.valueOf(de)); }
        if (ate != null) { sb.append(" AND data_cadastro <= ?"); params.add(Date.valueOf(ate)); }
        if ( buscaTexto != null && !buscaTexto.isBlank()) {
            sb.append(" AND (nome LIKE ? OR skills LIKE ? OR experiencia LIKE ?)");
            String like = '%' + buscaTexto.trim() + '%';
            params.add(like); params.add(like); params.add(like);
        }
        sb.append(" ORDER BY data_cadastro DESC, id DESC");

        List<Curriculo> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    private Curriculo mapear(ResultSet rs) throws SQLException {
        Curriculo c = new Curriculo();
        c.setId(rs.getInt("id"));
        c.setNome(rs.getString("nome"));
        c.setEmail(rs.getString("email"));
        c.setTelefone(rs.getString("telefone"));
        c.setCargoDesejado(rs.getString("cargo_desejado"));
        c.setSkills(rs.getString("skills"));
        c.setExperiencia(rs.getString("experiencia"));
        c.setEscolaridade(rs.getString("escolaridade"));
        c.setLinkedin(rs.getString("linkedin"));
        c.setStatusProcesso(rs.getString("status_processo"));
        c.setCaminhoPdf(rs.getString("caminho_pdf"));
        Date d = rs.getDate("data_cadastro");
        c.setDataCadastro(d != null ? d.toLocalDate() : null);
        return c;
    }
}