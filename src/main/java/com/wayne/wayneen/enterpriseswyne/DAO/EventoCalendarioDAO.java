package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.EventoCalendario;

import java.lang.reflect.Method;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de leitura da tabela `eventos`, usado pela tela de calendário (CalendarioController).
 *
 * Colunas reais da tabela (conferir em database/schema.sql):
 * id, titulo, descricao, data, local, tipo.
 *
 * CORREÇÃO: a versão anterior buscava as colunas "data_evento" e "origem",
 * que não existem na tabela, e qualquer consulta lançava SQLException.
 * - A data agora lê a coluna real "data" (com apelido data_evento no SELECT,
 *   pra não precisar mudar a leitura do ResultSet).
 * - A tabela não tem coluna de origem. Como a tela exibe uma coluna "Origem",
 *   o SELECT devolve o valor fixo 'Evento' para todos os registros.
 */
public class EventoCalendarioDAO {

    // ====== MAPA DE COLUNAS (nomes reais da tabela `eventos`) ======
    private static final String TABELA     = "eventos";
    private static final String COL_ID     = "id";
    private static final String COL_TITULO = "titulo";
    private static final String COL_DATA   = "data";
    private static final String COL_TIPO   = "tipo";
    private static final String COL_DESC   = "descricao";

    // Valor fixo usado como "origem", já que a tabela não tem essa coluna
    private static final String ORIGEM_PADRAO = "Evento";
    // ================================================================

    /**
     * Obtém conexão tentando compatibilizar projetos que usam
     * ConnectionFactory.getConnection() ou ConnectionFactory.getConexao().
     * ATENÇÃO: o pacote da ConnectionFactory está escrito como texto abaixo.
     * Se a classe estiver em outro pacote, isso só falha em tempo de execução.
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

    /**
     * Lista os eventos entre as datas informadas, opcionalmente filtrando por tipo.
     * Qualquer filtro nulo (ou tipo "Todos") é ignorado.
     */
    public List<EventoCalendario> listar(LocalDate inicio, LocalDate fim, String tipo) throws SQLException {
        List<EventoCalendario> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder()
                .append("SELECT ")
                .append(COL_ID).append(" AS id, ")
                .append(COL_TITULO).append(" AS titulo, ")
                .append(COL_DATA).append(" AS data_evento, ")          // coluna real "data", apelidada
                .append(COL_TIPO).append(" AS tipo, ")
                .append("'").append(ORIGEM_PADRAO).append("' AS origem, ") // valor fixo, não é coluna
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

    /**
     * Lista os tipos de evento distintos cadastrados, para preencher o filtro da tela.
     */
    public List<String> listarTiposExistentes() throws SQLException {
        List<String> tipos = new ArrayList<>();
        String sql = "SELECT DISTINCT " + COL_TIPO + " AS tipo FROM " + TABELA
                + " WHERE " + COL_TIPO + " IS NOT NULL ORDER BY " + COL_TIPO;

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) tipos.add(rs.getString("tipo"));
        }
        return tipos;
    }
}