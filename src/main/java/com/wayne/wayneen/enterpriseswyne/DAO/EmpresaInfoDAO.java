package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;
import com.wayne.wayneen.enterpriseswyne.model.EmpresaInfo;

import java.sql.*;

public class EmpresaInfoDAO {

    public EmpresaInfo buscar() throws SQLException {
        String sql = "SELECT * FROM empresa_info WHERE id=1";
        try (Connection c = ConnectionFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return mapear(rs);
        }
        return null;
    }

    public void inserirPadraoSeVazio() throws SQLException {
        if (buscar() == null) {
            String sql = "INSERT INTO empresa_info (id, nome, descricao_html) VALUES (1, 'Wayne Enterprises', '<h2>Sobre a Empresa</h2><p>Texto institucional.</p>')";
            try (Connection c = ConnectionFactory.getConexao();
                 PreparedStatement ps = c.prepareStatement(sql)) { ps.executeUpdate(); }
        }
    }

    public void salvar(EmpresaInfo e) throws SQLException {
        String sql = "UPDATE empresa_info SET nome=?, cnpj=?, descricao_html=?, missao=?, visao=?, valores=?, endereco=?, telefone=?, email=?, site=?, redes=?, logo_path=? WHERE id=1";
        try (Connection c = ConnectionFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, e.getNome());
            ps.setString(2, e.getCnpj());
            ps.setString(3, e.getDescricaoHtml());
            ps.setString(4, e.getMissao());
            ps.setString(5, e.getVisao());
            ps.setString(6, e.getValores());
            ps.setString(7, e.getEndereco());
            ps.setString(8, e.getTelefone());
            ps.setString(9, e.getEmail());
            ps.setString(10, e.getSite());
            ps.setString(11, e.getRedes());
            ps.setString(12, e.getLogoPath());
            ps.executeUpdate();
        }
    }

    private EmpresaInfo mapear(ResultSet rs) throws SQLException {
        EmpresaInfo e = new EmpresaInfo();
        e.setId(rs.getInt("id"));
        e.setNome(rs.getString("nome"));
        e.setCnpj(rs.getString("cnpj"));
        e.setDescricaoHtml(rs.getString("descricao_html"));
        e.setMissao(rs.getString("missao"));
        e.setVisao(rs.getString("visao"));
        e.setValores(rs.getString("valores"));
        e.setEndereco(rs.getString("endereco"));
        e.setTelefone(rs.getString("telefone"));
        e.setEmail(rs.getString("email"));
        e.setSite(rs.getString("site"));
        e.setRedes(rs.getString("redes"));
        e.setLogoPath(rs.getString("logo_path"));
        Timestamp ts = rs.getTimestamp("data_atualizacao");
        e.setDataAtualizacao(ts != null ? ts.toLocalDateTime() : null);
        return e;
    }
}