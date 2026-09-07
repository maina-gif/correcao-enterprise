package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.LogAuditoria;
import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogAuditoriaDAO {

    public static void registrar(String usuario, String acao, String modulo) {
        String sql = "INSERT INTO log_auditoria (usuario, acao, modulo, data_hora) VALUES (?, ?, ?, NOW())";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, acao);
            stmt.setString(3, modulo);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<LogAuditoria> listarTodos() {
        List<LogAuditoria> logs = new ArrayList<>();
        String sql = "SELECT * FROM log_auditoria ORDER BY data_hora DESC";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LogAuditoria log = new LogAuditoria(
                        rs.getInt("id"),
                        rs.getString("usuario"),
                        rs.getString("acao"),
                        rs.getString("modulo"),
                        rs.getTimestamp("data_hora").toLocalDateTime()
                );
                logs.add(log);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs;
    }
}
