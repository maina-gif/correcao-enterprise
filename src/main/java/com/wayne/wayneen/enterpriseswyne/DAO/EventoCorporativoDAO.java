package com.wayne.wayneen.enterpriseswyne;


import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventoCorporativoDAO {

    public static List<EventoCorporativo> listarTodos() {
        List<EventoCorporativo> eventos = new ArrayList<>();
        String sql = "SELECT * FROM eventos_corporativos ORDER BY data_evento ASC";

        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                EventoCorporativo evento = new EventoCorporativo();
                evento.setId(rs.getInt("id"));
                evento.setTitulo(rs.getString("titulo"));
                evento.setDescricao(rs.getString("descricao"));
                evento.setData(rs.getDate("data_evento").toLocalDate());
                evento.setTipo(rs.getString("tipo_evento"));
                evento.setLocal(rs.getString("local"));
                evento.setResponsavel(rs.getString("responsavel"));
                eventos.add(evento);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eventos;
    }
}

