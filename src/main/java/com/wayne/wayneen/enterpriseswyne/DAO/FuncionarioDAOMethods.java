package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;
import com.wayne.wayneen.enterpriseswyne.model.Funcionario;

import java.lang.reflect.Method;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 * DAO de Funcionários – compatível com model usando LocalDate OU java.sql.Date.
 * Converte datas de forma flexível e evita erros de compilação por tipo.
 */
public class FuncionarioDAOMethods {

    /* ===================== Helpers de datas (flex) ===================== */

    // Converte qualquer objeto de data do model para java.sql.Date
    private static java.sql.Date toSqlDateFlex(Object o) {
        if (o == null) return null;
        if (o instanceof java.sql.Date) return (java.sql.Date) o;
        if (o instanceof LocalDate)     return java.sql.Date.valueOf((LocalDate) o);
        if (o instanceof java.util.Date) return new java.sql.Date(((java.util.Date) o).getTime());
        return null; // tipo inesperado: trataremos como NULL
    }

    // Chama um getter (ex.: "getDataAdmissao") por reflexão
    private static Object callGetter(Object target, String getter) {
        try {
            Method m = target.getClass().getMethod(getter);
            return m.invoke(target);
        } catch (Exception ignored) { return null; }
    }

    // Tenta setar a data no model chamando setX(LocalDate) ou setX(java.sql.Date)
    private static void callSetterDate(Funcionario f, String setter, java.sql.Date d) {
        // 1) tenta LocalDate
        try {
            Method m = f.getClass().getMethod(setter, LocalDate.class);
            m.invoke(f, (d != null) ? d.toLocalDate() : null);
            return;
        } catch (NoSuchMethodException ignored) {
        } catch (Exception e) {
            // se o setter existe mas falhou, registra e segue tentando a outra assinatura
            e.printStackTrace();
        }
        // 2) tenta java.sql.Date
        try {
            Method m = f.getClass().getMethod(setter, java.sql.Date.class);
            m.invoke(f, d);
        } catch (Exception ignored) {
            // se não houver nenhum setter compatível, apenas ignore
        }
    }

    private static void setDateOrNullFlex(PreparedStatement ps, int idx, Object dateFromModel) throws SQLException {
        java.sql.Date d = toSqlDateFlex(dateFromModel);
        if (d != null) ps.setDate(idx, d);
        else ps.setNull(idx, Types.DATE);
    }

    private static String nz(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    /* ===================== KPIs / Agregações ===================== */

    public static int contarFuncionarios() {
        final String sql = "SELECT COUNT(*) FROM funcionarios";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) { e.printStackTrace(); return 0; }
    }

    public static double calcularMediaTempoPermanencia() {
        final String sql = "SELECT AVG(DATEDIFF(CURDATE(), data_admissao)) / 365 AS media_anos FROM funcionarios";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble("media_anos") : 0.0;
        } catch (SQLException e) { e.printStackTrace(); return 0.0; }
    }

    public static String setorComMaisFuncionarios() {
        final String sql = "SELECT departamento, COUNT(*) AS total FROM funcionarios GROUP BY departamento ORDER BY total DESC LIMIT 1";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getString("departamento") : "N/D";
        } catch (SQLException e) { e.printStackTrace(); return "N/D"; }
    }

    public static Map<String, Integer> contarPorSetor() {
        final String sql = "SELECT departamento, COUNT(*) AS total FROM funcionarios GROUP BY departamento";
        Map<String, Integer> mapa = new HashMap<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) mapa.put(rs.getString("departamento"), rs.getInt("total"));
        } catch (SQLException e) { e.printStackTrace(); }
        return mapa;
    }

    public static Map<String, Integer> contarCargos() {
        final String sql = "SELECT cargo, COUNT(*) AS total FROM funcionarios GROUP BY cargo";
        Map<String, Integer> mapa = new HashMap<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) mapa.put(rs.getString("cargo"), rs.getInt("total"));
        } catch (SQLException e) { e.printStackTrace(); }
        return mapa;
    }

    public static Map<String, Integer> contarDepartamentos() {
        final String sql = "SELECT departamento, COUNT(*) AS total FROM funcionarios GROUP BY departamento";
        Map<String, Integer> mapa = new HashMap<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) mapa.put(rs.getString("departamento"), rs.getInt("total"));
        } catch (SQLException e) { e.printStackTrace(); }
        return mapa;
    }

    public static double calcularTempoMedioEmpresa() {
        final String sql = "SELECT AVG(DATEDIFF(CURDATE(), data_admissao)) / 365.0 AS media_anos FROM funcionarios";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble("media_anos") : 0.0;
        } catch (SQLException e) { e.printStackTrace(); return 0.0; }
    }

    /* ===================== CRUD ===================== */

    private static Funcionario mapRow(ResultSet rs) throws SQLException {
        Funcionario f = new Funcionario();
        f.setId(rs.getInt("id"));
        f.setNomeCompleto(rs.getString("nome_completo"));
        f.setCpf(rs.getString("cpf"));
        f.setCargo(rs.getString("cargo"));
        f.setDepartamento(rs.getString("departamento"));
        f.setEmail(rs.getString("email"));

        // Datas: setar na assinatura disponível (LocalDate OU java.sql.Date)
        callSetterDate(f, "setDataAdmissao", rs.getDate("data_admissao"));
        try { callSetterDate(f, "setDataNascimento", rs.getDate("data_nascimento")); } catch (Exception ignored) {}

        // Caminhos (se existirem no model)
        try { f.setCaminhoCurriculo(rs.getString("caminho_curriculo")); } catch (Exception ignored) {}
        try { f.setCaminhoContrato(rs.getString("caminho_contrato")); } catch (Exception ignored) {}
        try { f.setCaminhoFoto(rs.getString("caminho_foto")); } catch (Exception ignored) {}

        return f;
    }

    public static Funcionario buscarPorId(int id) {
        final String sql =
                "SELECT id, nome_completo, cpf, cargo, departamento, email, " +
                        "data_admissao, data_nascimento, caminho_curriculo, caminho_contrato, caminho_foto " +
                        "FROM funcionarios WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) { e.printStackTrace(); return null; }
    }

    public static List<Funcionario> listarTodos() {
        final String sql =
                "SELECT id, nome_completo, cpf, cargo, departamento, email, " +
                        "data_admissao, data_nascimento, caminho_curriculo, caminho_contrato, caminho_foto " +
                        "FROM funcionarios ORDER BY id DESC";
        List<Funcionario> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public static List<Funcionario> buscarPorNome(String filtro) {
        final String sql =
                "SELECT id, nome_completo, cpf, cargo, departamento, email, " +
                        "data_admissao, data_nascimento, caminho_curriculo, caminho_contrato, caminho_foto " +
                        "FROM funcionarios WHERE nome_completo LIKE ? ORDER BY id DESC";
        List<Funcionario> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + filtro + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public static boolean excluir(int id) {
        final String sql = "DELETE FROM funcionarios WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static void salvar(Funcionario f) {
        final String sql =
                "INSERT INTO funcionarios (" +
                        "nome_completo, cpf, cargo, departamento, email, " +
                        "data_admissao, data_nascimento, caminho_curriculo, caminho_contrato, caminho_foto" +
                        ") VALUES (?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nz(f.getNomeCompleto()));
            ps.setString(2, nz(f.getCpf()));
            ps.setString(3, nz(f.getCargo()));
            ps.setString(4, nz(f.getDepartamento()));
            ps.setString(5, nz(f.getEmail()));

            // pega do model (LocalDate OU java.sql.Date) e converte
            setDateOrNullFlex(ps, 6, callGetter(f, "getDataAdmissao"));
            setDateOrNullFlex(ps, 7, callGetter(f, "getDataNascimento"));

            ps.setString(8, nz(f.getCaminhoCurriculo()));
            ps.setString(9, nz(f.getCaminhoContrato()));
            ps.setString(10, nz(f.getCaminhoFoto()));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    try { f.setId(rs.getInt(1)); } catch (Exception ignored) {}
                }
            }
        } catch (SQLIntegrityConstraintViolationException dup) {
            throw new RuntimeException("CPF já cadastrado.", dup);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar funcionário.", e);
        }
    }

    public static void atualizar(Funcionario f) {
        final String sql =
                "UPDATE funcionarios SET " +
                        "nome_completo = ?, cpf = ?, cargo = ?, departamento = ?, email = ?, " +
                        "data_admissao = ?, data_nascimento = ?, caminho_curriculo = ?, caminho_contrato = ?, caminho_foto = ? " +
                        "WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nz(f.getNomeCompleto()));
            ps.setString(2, nz(f.getCpf()));
            ps.setString(3, nz(f.getCargo()));
            ps.setString(4, nz(f.getDepartamento()));
            ps.setString(5, nz(f.getEmail()));

            setDateOrNullFlex(ps, 6, callGetter(f, "getDataAdmissao"));
            setDateOrNullFlex(ps, 7, callGetter(f, "getDataNascimento"));

            ps.setString(8, nz(f.getCaminhoCurriculo()));
            ps.setString(9, nz(f.getCaminhoContrato()));
            ps.setString(10, nz(f.getCaminhoFoto()));
            ps.setInt(11, f.getId());

            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException dup) {
            throw new RuntimeException("CPF já pertence a outro registro.", dup);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar funcionário.", e);
        }
    }
}
