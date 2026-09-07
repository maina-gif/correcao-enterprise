package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;
import com.wayne.wayneen.enterpriseswyne.model.ParticipacaoTreinamento;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ParticipacaoTreinamentoDAO {

    private static final Logger logger = Logger.getLogger(ParticipacaoTreinamentoDAO.class.getName());

    private static final String INSERT_SQL =
            "INSERT INTO participacoes_treinamento (id_funcionario, id_treinamento, data_participacao) VALUES (?, ?, ?)";

    private static final String SELECT_ALL_SQL =
            "SELECT id, id_funcionario, id_treinamento, data_participacao FROM participacoes_treinamento";

    private static final String SELECT_BY_FUNC_SQL =
            "SELECT id, id_funcionario, id_treinamento, data_participacao FROM participacoes_treinamento WHERE id_funcionario = ?";

    private static final String SELECT_BY_TREIN_SQL =
            "SELECT id, id_funcionario, id_treinamento, data_participacao FROM participacoes_treinamento WHERE id_treinamento = ?";

    private static final String DELETE_SQL =
            "DELETE FROM participacoes_treinamento WHERE id = ?";

    private static final String EXISTS_DUP_SQL =
            "SELECT 1 FROM participacoes_treinamento WHERE id_funcionario = ? AND id_treinamento = ? AND data_participacao = ? LIMIT 1";

    public int salvar(ParticipacaoTreinamento participacao) throws SQLException {
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, participacao.getIdFuncionario());
            ps.setInt(2, participacao.getIdTreinamento());

            LocalDate data = participacao.getDataParticipacao();
            if (data != null) {
                ps.setDate(3, Date.valueOf(data));
            } else {
                ps.setNull(3, java.sql.Types.DATE);
            }

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGerado = rs.getInt(1);
                        participacao.setId(idGerado);
                        return idGerado;
                    }
                }
            }
            return -1;
        }
    }

    public List<ParticipacaoTreinamento> listar() throws SQLException {
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            return mapLista(rs);
        }
    }

    public List<ParticipacaoTreinamento> listarPorFuncionario(int idFuncionario) throws SQLException {
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_FUNC_SQL)) {
            ps.setInt(1, idFuncionario);
            try (ResultSet rs = ps.executeQuery()) {
                return mapLista(rs);
            }
        }
    }

    public List<ParticipacaoTreinamento> listarPorTreinamento(int idTreinamento) throws SQLException {
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_TREIN_SQL)) {
            ps.setInt(1, idTreinamento);
            try (ResultSet rs = ps.executeQuery()) {
                return mapLista(rs);
            }
        }
    }

    public boolean excluir(int id) throws SQLException {
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean existsDuplicado(int idFuncionario, int idTreinamento, LocalDate dataParticipacao) throws SQLException {
        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement ps = conn.prepareStatement(EXISTS_DUP_SQL)) {
            ps.setInt(1, idFuncionario);
            ps.setInt(2, idTreinamento);
            ps.setDate(3, Date.valueOf(dataParticipacao));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private List<ParticipacaoTreinamento> mapLista(ResultSet rs) throws SQLException {
        List<ParticipacaoTreinamento> lista = new ArrayList<>();
        while (rs.next()) {
            ParticipacaoTreinamento p = new ParticipacaoTreinamento();
            p.setId(rs.getInt("id"));
            p.setIdFuncionario(rs.getInt("id_funcionario"));
            p.setIdTreinamento(rs.getInt("id_treinamento"));
            Date d = rs.getDate("data_participacao");
            p.setDataParticipacao(d != null ? d.toLocalDate() : null);
            lista.add(p);
        }
        return lista;
    }
}