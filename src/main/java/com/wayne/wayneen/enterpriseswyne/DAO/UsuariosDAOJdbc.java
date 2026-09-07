package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;
import com.wayne.wayneen.enterpriseswyne.model.Usuarios;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuariosDAOJdbc implements UsuariosDAO {

    private static final String TABELA = "usuarios";

    // caches process-wide
    private volatile String cachedReadNomeCol;   // para SELECT/ORDER
    private volatile String cachedWriteNomeCol;  // para INSERT/UPDATE

    // =========================================================
    // ========================= SELECTS =======================
    // =========================================================

    @Override
    public List<Usuarios> listarTodos() throws SQLException {
        List<Usuarios> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection()) {
            final String nomeCol = resolveReadNomeCol(conn);                 // nome_completo/nome/.../email
            final boolean hasLastSeen = colExiste(conn, TABELA, "last_seen");

            final String sql =
                    "SELECT id, " + bt(nomeCol) + " AS nome_completo, email, " +
                            "COALESCE(online, 0) AS online, " +
                            (hasLastSeen ? "last_seen" : "NULL AS last_seen") + " " +
                            "FROM " + TABELA + " " +
                            "ORDER BY " + ("email".equalsIgnoreCase(nomeCol) ? "id" : bt(nomeCol));

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapUsuario(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public Optional<Usuarios> buscarPorId(long id) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            final String nomeCol = resolveReadNomeCol(conn);
            final boolean hasLastSeen = colExiste(conn, TABELA, "last_seen");

            final String sql =
                    "SELECT id, " + bt(nomeCol) + " AS nome_completo, email, " +
                            "COALESCE(online, 0) AS online, " +
                            (hasLastSeen ? "last_seen" : "NULL AS last_seen") + " " +
                            "FROM " + TABELA + " WHERE id = ?";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? Optional.of(mapUsuario(rs)) : Optional.empty();
                }
            }
        }
    }

    @Override
    public Optional<Usuarios> buscarPorEmail(String email) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            final String nomeCol = resolveReadNomeCol(conn);
            final boolean hasLastSeen = colExiste(conn, TABELA, "last_seen");

            final String sql =
                    "SELECT id, " + bt(nomeCol) + " AS nome_completo, email, " +
                            "COALESCE(online, 0) AS online, " +
                            (hasLastSeen ? "last_seen" : "NULL AS last_seen") + " " +
                            "FROM " + TABELA + " WHERE email = ?";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, email);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? Optional.of(mapUsuario(rs)) : Optional.empty();
                }
            }
        }
    }

    @Override
    public List<Usuarios> buscarPorTermo(String termo) throws SQLException {
        List<Usuarios> lista = new ArrayList<>();
        if (termo == null) termo = "";
        String like = "%" + termo + "%";

        try (Connection conn = ConnectionFactory.getConnection()) {
            final String nomeCol = resolveReadNomeCol(conn);
            final boolean hasLastSeen = colExiste(conn, TABELA, "last_seen");

            final String sql =
                    "SELECT id, " + bt(nomeCol) + " AS nome_completo, email, " +
                            "COALESCE(online, 0) AS online, " +
                            (hasLastSeen ? "last_seen" : "NULL AS last_seen") + " " +
                            "FROM " + TABELA + " " +
                            "WHERE " + bt(nomeCol) + " LIKE ? OR email LIKE ? " +
                            "ORDER BY " + ("email".equalsIgnoreCase(nomeCol) ? "id" : bt(nomeCol));

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, like);
                ps.setString(2, like);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) lista.add(mapUsuario(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public List<Usuarios> listarUsuariosAtivosOuPorNome(String filtro) {
        List<Usuarios> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection()) {
            final String nomeCol = resolveReadNomeCol(conn);
            final boolean hasLastSeen = colExiste(conn, TABELA, "last_seen");

            StringBuilder sb = new StringBuilder();
            sb.append("SELECT id, ").append(bt(nomeCol)).append(" AS nome_completo, email, ")
                    .append("COALESCE(online,0) AS online, ")
                    .append(hasLastSeen ? "last_seen " : "NULL AS last_seen ")
                    .append("FROM ").append(TABELA).append(" ");

            boolean temFiltro = filtro != null && !filtro.isBlank();
            if (temFiltro) {
                sb.append("WHERE ").append(bt(nomeCol)).append(" LIKE ? OR email LIKE ? ");
            }

            sb.append("ORDER BY ").append("email".equalsIgnoreCase(nomeCol) ? "id" : bt(nomeCol));

            try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
                if (temFiltro) {
                    String like = "%" + filtro + "%";
                    ps.setString(1, like);
                    ps.setString(2, like);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) lista.add(mapUsuario(rs));
                }
            }
        } catch (SQLException e) {
            // você pode logar se quiser
        }
        return lista;
    }

    // =========================================================
    // ================ INSERT / UPDATE / DELETE ===============
    // =========================================================

    @Override
    public long salvar(Usuarios u) throws SQLException {
        if (u == null) throw new SQLException("Usuário não pode ser nulo.");

        try (Connection conn = ConnectionFactory.getConnection()) {
            final String writeCol = resolveWriteNomeCol(conn); // pode ser null se não existir coluna de nome

            String sql;
            if (writeCol != null) {
                sql = "INSERT INTO " + TABELA + " (" + bt(writeCol) + ", email, online, last_seen) VALUES (?, ?, ?, ?)";
            } else {
                sql = "INSERT INTO " + TABELA + " (email, online, last_seen) VALUES (?, ?, ?)";
            }

            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                int idx = 1;
                if (writeCol != null) {
                    ps.setString(idx++, safe(u.getNomeCompleto()));
                }
                ps.setString(idx++, safe(u.getEmail()));
                ps.setBoolean(idx++, u.isOnline());

                if (u.getLastSeen() != null) {
                    ps.setTimestamp(idx++, timestampFrom(u));
                } else {
                    ps.setNull(idx++, Types.TIMESTAMP);
                }

                if (ps.executeUpdate() == 0) throw new SQLException("Inserção falhou.");

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        long id = keys.getLong(1);
                        u.setId(id);
                        return id;
                    }
                    throw new SQLException("Sem ID gerado.");
                }
            }
        }
    }

    @Override
    public boolean atualizar(Usuarios u) throws SQLException {
        if (u == null) throw new SQLException("Usuário não pode ser nulo.");
        if (u.getId() == 0L) throw new SQLException("ID obrigatório para atualizar.");

        try (Connection conn = ConnectionFactory.getConnection()) {
            final String writeCol = resolveWriteNomeCol(conn);

            String sql;
            if (writeCol != null) {
                sql = "UPDATE " + TABELA +
                        " SET " + bt(writeCol) + " = ?, email = ?, online = ?, last_seen = ? WHERE id = ?";
            } else {
                sql = "UPDATE " + TABELA +
                        " SET email = ?, online = ?, last_seen = ? WHERE id = ?";
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                int idx = 1;
                if (writeCol != null) {
                    ps.setString(idx++, safe(u.getNomeCompleto()));
                }
                ps.setString(idx++, safe(u.getEmail()));
                ps.setBoolean(idx++, u.isOnline());

                if (u.getLastSeen() != null) {
                    ps.setTimestamp(idx++, timestampFrom(u));
                } else {
                    ps.setNull(idx++, Types.TIMESTAMP);
                }

                ps.setLong(idx, u.getId());
                return ps.executeUpdate() > 0;
            }
        }
    }

    @Override
    public boolean remover(long id) throws SQLException {
        String sql = "DELETE FROM " + TABELA + " WHERE id = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean setOnline(long id, boolean online) throws SQLException {
        String sql = "UPDATE " + TABELA + " SET online = ? WHERE id = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setBoolean(1, online);
            ps.setLong(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean tocarLastSeen(long id) throws SQLException {
        if (!colExiste(ConnectionFactory.getConnection(), TABELA, "last_seen")) return false;
        String sql = "UPDATE " + TABELA + " SET last_seen = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // =========================================================
    // ========================= Helpers =======================
    // =========================================================

    private Usuarios mapUsuario(ResultSet rs) throws SQLException {
        Usuarios u = new Usuarios();
        u.setId(rs.getLong("id"));
        u.setNomeCompleto(rs.getString("nome_completo")); // vem do alias
        u.setEmail(rs.getString("email"));
        try { u.setOnline(rs.getBoolean("online")); } catch (SQLException ignored) {}

        try {
            Timestamp ts = rs.getTimestamp("last_seen");
            if (ts != null) u.setLastSeen(Instant.ofEpochMilli(ts.getTime()));
        } catch (SQLException ignored) {}
        return u;
    }

    private static String safe(String s) {
        return (s == null) ? "" : s.trim();
    }

    private static Timestamp timestampFrom(Usuarios u) {
        // preferimos Instant
        Instant i = u.getLastSeen();
        return (i == null) ? null : Timestamp.from(i);
    }

    private String bt(String identifier) {
        return "`" + identifier + "`";
    }

    /** Detecta a melhor coluna de NOME para leitura (SELECT/ORDER). */
    private String resolveReadNomeCol(Connection conn) throws SQLException {
        if (cachedReadNomeCol != null && !cachedReadNomeCol.isBlank()) return cachedReadNomeCol;

        final String[] candidatos = {"nome_completo", "nome", "nome_usuario", "nomeUsuario", "full_name"};
        DatabaseMetaData md = conn.getMetaData();
        String catalog = conn.getCatalog();

        for (String tabela : new String[]{TABELA, TABELA.toUpperCase()}) {
            for (String c : candidatos) {
                try (ResultSet rs = md.getColumns(catalog, null, tabela, c)) {
                    if (rs.next()) return cachedReadNomeCol = c;
                }
                try (ResultSet rs = md.getColumns(null, null, tabela, c)) {
                    if (rs.next()) return cachedReadNomeCol = c;
                }
            }
        }
        return cachedReadNomeCol = "email"; // fallback seguro
    }

    /** Detecta a melhor coluna para ESCRITA do nome (INSERT/UPDATE). */
    private String resolveWriteNomeCol(Connection conn) throws SQLException {
        if (cachedWriteNomeCol != null) return cachedWriteNomeCol;

        final String[] candidatos = {"nome_completo", "nome", "nome_usuario", "nomeUsuario", "full_name"};
        for (String c : candidatos) {
            if (colExiste(conn, TABELA, c)) {
                return cachedWriteNomeCol = c;
            }
        }
        // se nenhuma coluna de nome existir, retornamos null → SQL de escrita omitirá o nome
        return cachedWriteNomeCol = null;
    }

    /** Verifica se uma coluna existe (tenta com e sem catalog). */
    private boolean colExiste(Connection conn, String tabela, String coluna) throws SQLException {
        DatabaseMetaData md = conn.getMetaData();
        String catalog = conn.getCatalog();

        try (ResultSet rs = md.getColumns(catalog, null, tabela, coluna)) {
            if (rs.next()) return true;
        }
        try (ResultSet rs = md.getColumns(null, null, tabela, coluna)) {
            return rs.next();
        }
    }
}
