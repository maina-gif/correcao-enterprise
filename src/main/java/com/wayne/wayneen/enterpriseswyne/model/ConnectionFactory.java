package com.wayne.wayneen.enterpriseswyne.model;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Fabrica de conexoes com o MySQL.
 *
 * Correcoes em relacao a versao original:
 *   1) Credenciais NAO ficam mais hardcoded no codigo-fonte - vem de
 *      db.properties (fora do controle de versao) ou de variaveis de
 *      ambiente, que sempre tem prioridade sobre o arquivo.
 *   2) Removido o padrao "Connection estatica compartilhada" - cada
 *      chamada a getConnection() abre uma conexao NOVA, que e o que o
 *      resto do codigo ja assume (todo DAO usa try-with-resources).
 *
 * Configuracao:
 *   Crie um arquivo db.properties em src/main/resources com:
 *
 *     db.url=jdbc:mysql://localhost:3306/wayne_db
 *     db.user=wayne_app
 *     db.password=sua_senha_aqui
 *
 *   Ou defina as variaveis de ambiente DB_URL / DB_USER / DB_PASSWORD.
 */
public class ConnectionFactory {

    private static final Logger logger = Logger.getLogger(ConnectionFactory.class.getName());

    private static final String URL;
    private static final String USUARIO;
    private static final String SENHA;

    static {
        Properties props = new Properties();

        try (InputStream in = ConnectionFactory.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
            } else {
                logger.warning("db.properties nao encontrado no classpath. " +
                        "Usando apenas variaveis de ambiente / valores padrao de desenvolvimento.");
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Erro ao ler db.properties.", e);
        }

        URL = getEnvOrProp("DB_URL", props, "db.url", "jdbc:mysql://localhost:3306/wayne_db");
        USUARIO = getEnvOrProp("DB_USER", props, "db.user", "root");
        SENHA = getEnvOrProp("DB_PASSWORD", props, "db.password", "");

        if (USUARIO.equals("root")) {
            logger.warning("Conectando como 'root'. Aceitavel so em desenvolvimento local. " +
                    "Crie um usuario dedicado (ver database/create_app_user.sql) antes de usar em producao.");
        }
    }

    private static String getEnvOrProp(String envKey, Properties props, String propKey, String defaultValue) {
        String fromEnv = System.getenv(envKey);
        if (fromEnv != null && !fromEnv.isBlank()) return fromEnv;

        String fromProps = props.getProperty(propKey);
        if (fromProps != null && !fromProps.isBlank()) return fromProps;

        return defaultValue;
    }

    private ConnectionFactory() {
        // classe utilitaria, nao instanciar
    }

    /**
     * Abre e retorna uma NOVA conexao a cada chamada. Quem chamar e
     * responsavel por fechar (try-with-resources, como ja e o padrao
     * usado em todo o projeto).
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Driver JDBC do MySQL nao encontrado no classpath.", e);
            throw new SQLException("Driver JDBC nao encontrado.", e);
        }

        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao conectar com o banco de dados em " + URL, e);
            throw e;
        }
    }

    // ---- Aliases mantidos por compatibilidade com o codigo ja existente ----

    public static Connection getConexao() throws SQLException {
        return getConnection();
    }

    public static Connection conectar() throws SQLException {
        return getConnection();
    }
}
