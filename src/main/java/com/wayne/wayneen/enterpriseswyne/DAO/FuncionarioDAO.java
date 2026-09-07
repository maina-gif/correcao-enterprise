
package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;
import com.wayne.wayneen.enterpriseswyne.DAO.FuncionarioDAOMethods;
import com.wayne.wayneen.enterpriseswyne.model.Funcionario;
import javafx.fxml.FXML;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.sql.Date; // java.sql.Date


public class FuncionarioDAO {

    public static void salvar(Funcionario f) {
        String sql = "INSERT INTO funcionarios (nome_completo, cpf, cargo, departamento, email, data_admissao, data_nascimento, caminho_curriculo, caminho_contrato, caminho_foto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, f.getNomeCompleto());
            stmt.setString(2, f.getCpf());
            stmt.setString(3, f.getCargo());
            stmt.setString(4, f.getDepartamento());
            stmt.setString(5, f.getEmail());
            stmt.setDate(6, f.getDataAdmissao() != null ? f.getDataAdmissao() : null);
            stmt.setDate(7, f.getDataNascimento() != null ? Date.valueOf(f.getDataNascimento()) : null);
            stmt.setString(8, f.getCaminhoCurriculo());
            stmt.setString(9, f.getCaminhoContrato());
            stmt.setString(10, f.getCaminhoFoto());

            stmt.executeUpdate();
            LogDAO.registrar("Cadastrou novo funcionário: " + f.getNomeCompleto());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void atualizar(Funcionario f) {
        String sql = "UPDATE funcionarios SET nome_completo=?, cpf=?, cargo=?, departamento=?, email=?, data_admissao=?, data_nascimento=?, caminho_curriculo=?, caminho_contrato=?, caminho_foto=? WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, f.getNomeCompleto());
            stmt.setString(2, f.getCpf());
            stmt.setString(3, f.getCargo());
            stmt.setString(4, f.getDepartamento());
            stmt.setString(5, f.getEmail());
            stmt.setDate(6, f.getDataAdmissao() != null ? f.getDataAdmissao() : null);
            stmt.setDate(7, f.getDataNascimento() != null ? Date.valueOf(f.getDataNascimento()) : null);
            stmt.setString(8, f.getCaminhoCurriculo());
            stmt.setString(9, f.getCaminhoContrato());
            stmt.setString(10, f.getCaminhoFoto());
            stmt.setInt(11, f.getId());

            stmt.executeUpdate();
            LogDAO.registrar("Atualizou dados do funcionário: " + f.getNomeCompleto());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean excluir(int id) {
        String sql = "DELETE FROM funcionarios WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static List<Funcionario> listarTodos() {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM funcionarios";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Funcionario f = new Funcionario();
                f.setId(rs.getInt("id"));
                f.setNomeCompleto(rs.getString("nome_completo"));
                f.setCpf(rs.getString("cpf"));
                f.setCargo(rs.getString("cargo"));
                f.setDepartamento(rs.getString("departamento"));
                f.setEmail(rs.getString("email"));
                f.setCaminhoCurriculo(rs.getString("caminho_curriculo"));
                f.setCaminhoContrato(rs.getString("caminho_contrato"));
                f.setCaminhoFoto(rs.getString("caminho_foto"));

                f.setDataAdmissao(rs.getDate("data_admissao"));
                Date dataNascimento = rs.getDate("data_nascimento");
                f.setDataNascimento(dataNascimento != null ? dataNascimento.toLocalDate() : null);

                lista.add(f);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static Funcionario buscarPorId(int id) {
        String sql = "SELECT * FROM funcionarios WHERE id=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Funcionario f = new Funcionario();
                f.setId(rs.getInt("id"));
                f.setNomeCompleto(rs.getString("nome_completo"));
                f.setCpf(rs.getString("cpf"));
                f.setCargo(rs.getString("cargo"));
                f.setDepartamento(rs.getString("departamento"));
                f.setEmail(rs.getString("email"));
                f.setCaminhoCurriculo(rs.getString("caminho_curriculo"));
                f.setCaminhoContrato(rs.getString("caminho_contrato"));
                f.setCaminhoFoto(rs.getString("caminho_foto"));

                f.setDataAdmissao(rs.getDate("data_admissao"));
                Date dataNascimento = rs.getDate("data_nascimento");
                f.setDataNascimento(dataNascimento != null ? dataNascimento.toLocalDate() : null);

                return f;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static double calcularTempoMedioEmpresa() {
        String sql = "SELECT data_admissao FROM funcionarios WHERE data_admissao IS NOT NULL";
        double somaAnos = 0;
        int total = 0;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Date dataAdmissao = rs.getDate("data_admissao");
                if (dataAdmissao != null) {
                    LocalDate admissao = dataAdmissao.toLocalDate();
                    double anos = ChronoUnit.DAYS.between(admissao, LocalDate.now()) / 365.0;
                    somaAnos += anos;
                    total++;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total > 0 ? somaAnos / total : 0.0;
    }

    public static Map<String, Integer> contarCargos() {
        Map<String, Integer> mapa = new HashMap<>();
        String sql = "SELECT cargo, COUNT(*) AS total FROM funcionarios GROUP BY cargo";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String cargo = rs.getString("cargo");
                int total = rs.getInt("total");
                mapa.put(cargo != null ? cargo : "Desconhecido", total);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mapa;
    }

    public static Map<String, Integer> contarDepartamentos() {
        Map<String, Integer> mapa = new HashMap<>();
        String sql = "SELECT departamento, COUNT(*) AS total FROM funcionarios GROUP BY departamento";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String departamento = rs.getString("departamento");
                int total = rs.getInt("total");
                mapa.put(departamento != null ? departamento : "Desconhecido", total);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mapa;
    }

    @FXML

    public static List<Funcionario> buscarPorNome(String nome) {
        List<Funcionario> funcionarios = new ArrayList<>();

        String sql = "SELECT * FROM funcionarios WHERE nome_completo LIKE ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Funcionario f = new Funcionario();
                f.setId(rs.getInt("id"));
                f.setNomeCompleto(rs.getString("nome_completo"));
                f.setCpf(rs.getString("cpf"));
                f.setCargo(rs.getString("cargo"));
                f.setDepartamento(rs.getString("departamento"));
                f.setEmail(rs.getString("email"));
                f.setDataAdmissao(rs.getDate("data_admissao"));
                // Se houver mais campos, adicione aqui

                funcionarios.add(f);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return funcionarios;
    }


    public class InteligenciaDadosDAO {

      @FXML
        public static int contarFuncionarios() {
            String sql = "SELECT COUNT(*) FROM funcionarios";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        @FXML
        // Calcula o tempo médio de permanência dos funcionários na empresa (em anos)
        public static double calcularMediaTempoPermanencia() {
            String sql = "SELECT data_admissao FROM funcionarios";
            int totalDias = 0;
            int totalFuncionarios = 0;

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Date dataAdmissao = rs.getDate("data_admissao");
                    if (dataAdmissao != null) {
                        long dias = ChronoUnit.DAYS.between(dataAdmissao.toLocalDate(), LocalDate.now());
                        totalDias += dias;
                        totalFuncionarios++;
                    }
                }

                if (totalFuncionarios > 0) {
                    return (double) totalDias / totalFuncionarios / 365.0; // Convertendo dias para anos
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return 0.0;
        }

        // Retorna o nome do setor com maior número de funcionários
        public static String setorComMaisFuncionarios() {
            String sql = "SELECT setor, COUNT(*) AS total FROM funcionarios GROUP BY setor ORDER BY total DESC LIMIT 1";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return rs.getString("setor");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return "Desconhecido";
        }

        // Retorna um mapa com a contagem de funcionários por setor
        public static Map<String, Integer> contarPorSetor() {
            Map<String, Integer> mapa = new HashMap<>();
            String sql = "SELECT setor, COUNT(*) AS total FROM funcionarios GROUP BY setor";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    mapa.put(rs.getString("setor"), rs.getInt("total"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return mapa;
        }


}
}
