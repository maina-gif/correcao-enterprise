package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.EventoCalendario;

import java.lang.reflect.Method;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Ajuste as constantes abaixo para casar com os nomes das suas colunas reais
 * na tabela `eventos`.
 *
 * Exemplos comuns:
 * - Data: "data_evento"  OU  "data"
 * - Título: "titulo"     OU  "nome"
 * - Tipo: "tipo"         OU  "categoria"
 * - Origem: "origem"     OU  "fonte"
 * - Descrição: "descricao" OU "detalhes"
 */
public class EventoCalendarioDAO {

    // ====== MAPA DE COLUNAS (EDITE CONFORME SEU BANCO) ======
    private static final String TABELA     = "eventos";
    private static final String COL_ID     = "id";
    private static final String COL_TITULO = "titulo";
    private static final String COL_DATA   = "data_evento"; // troque para "data" se for o seu caso
    private static final String COL_TIPO   = "tipo";
    private static final String COL_ORIGEM = "origem";
    private static final String COL_DESC   = "descricao";
    // =========================================================

    /**
     * Obtém conexão tentando compatibilizar projetos que usam
     * ConnectionFactory.getConnection() ou ConnectionFactory.getConexao().
     */
    private Connection getConn() throws SQLException {
        try {
            Class<?> cf = Class.forName("com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory");
            // Tenta getConnection()
            try {
                Method m = cf.getMethod("getConnection");
                Object conn = m.invoke(null);
                if (conn instanceof Connection) return (Connection) conn;
            } catch (NoSuchMethodException ignore) { /* tenta o próximo */ }

            // Tenta getConexao()
            try {
                Method m = cf.getMethod("getConexao");
                Object conn = m.invoke(null);
                if (conn instanceof Connection) return (Connection) conn;
            } catch (NoSuchMethodException ignore) { /* nenhum disponível */ }

            throw new SQLException("Não encontrei getConnection() nem getConexao() em ConnectionFactory.");
        } catch (Exception e) {
            if (e instanceof SQLException) throw (SQLException) e;
            throw new SQLException("Falha ao obter conexão via ConnectionFactory: " + e.getMessage(), e);
        }
    }

    public List<EventoCalendario> listar(LocalDate inicio, LocalDate fim, String tipo) throws SQLException {
        List<EventoCalendario> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder()
                .append("SELECT ")
                .append(COL_ID).append(" AS id, ")
                .append(COL_TITULO).append(" AS titulo, ")
                .append(COL_DATA).append(" AS data_evento, ")
                .append(COL_TIPO).append(" AS tipo, ")
                .append(COL_ORIGEM).append(" AS origem, ")
                .append(COL_DESC).append(" AS descricao ")
                .append("FROM ").append(TABELA)
                .append(" WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (inicio != null) {
            sql.append(" AND ").append(COL_DATA).append(" >= ? ");
            params.add(Date.valueOf(inicio));
        }
        if (fim != null) {
            sql.append(" AND ").append(COL_DATA).append(" <= ? ");
            params.add(Date.valueOf(fim));
        }
        if (tipo != null && !tipo.isBlank() && !"Todos".equalsIgnoreCase(tipo)) {
            sql.append(" AND ").append(COL_TIPO).append(" = ? ");
            params.add(tipo.trim());
        }

        sql.append(" ORDER BY ").append(COL_DATA).append(" ASC, ").append(COL_TITULO).append(" ASC");

        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date d = rs.getDate("data_evento");
                    LocalDate data = d != null ? d.toLocalDate() : null;

                    EventoCalendario ev = new EventoCalendario(
                            rs.getLong("id"),
                            rs.getString("titulo"),
                            data,
                            rs.getString("tipo"),
                            rs.getString("origem"),
                            rs.getString("descricao")
                    );
                    lista.add(ev);
                }
            }
        }

        return lista;
    }

    public List<String> listarTiposExistentes() throws SQLException {
        List<String> tipos = new ArrayList<>();
        String sql = "SELECT DISTINCT " + COL_TIPO + " AS tipo FROM " + TABELA + " WHERE " + COL_TIPO + " IS NOT NULL ORDER BY " + COL_TIPO;

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) tipos.add(rs.getString("tipo"));
        }
        return tipos;
    }
}
