package com.wayne.wayneen.enterpriseswyne.model;

import com.wayne.wayneen.enterpriseswyne.DAO.LogDAO;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Backup e restauracao do banco via mysqldump/mysql.
 *
 * Correcao de seguranca: antes, o comando era montado por concatenacao de
 * String e executado via "cmd.exe /c" (shell). Isso permitia command
 * injection (um caminho de arquivo com caracteres especiais podia alterar
 * o comando executado) e expunha a senha do banco na linha de comando
 * (visivel para outros processos do sistema operacional).
 *
 * Agora: ProcessBuilder com argumentos em lista separada (nunca via shell),
 * e a senha e passada para o processo filho via variavel de ambiente
 * MYSQL_PWD, nunca como texto na linha de comando.
 */
public class BackupRestauracao {

    private static final String USUARIO = "root";
    private static final String NOME_BANCO = "wayne_db";

    public static boolean realizarBackup(Window parentWindow) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Backup do Banco de Dados");
        fileChooser.setInitialFileName("backup_wayne_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".sql");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivo SQL", "*.sql"));

        File arquivo = fileChooser.showSaveDialog(parentWindow);
        if (arquivo == null) {
            return false;
        }

        // Para backup nao precisamos de senha na maioria dos setups locais;
        // se o usuario 'root' tiver senha, passe-a aqui pela mesma via
        // segura usada em restaurarBackup (variavel MYSQL_PWD).
        return executarComando(
                arquivo,
                new String[]{"mysqldump", "-u" + USUARIO, NOME_BANCO},
                null,
                true,
                "Backup realizado com sucesso: ",
                "Erro ao realizar backup. Codigo de saida: ",
                "Erro durante o backup: "
        );
    }

    public static boolean restaurarBackup(Window parentWindow, String senha) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Arquivo de Backup");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivo SQL", "*.sql"));

        File arquivo = fileChooser.showOpenDialog(parentWindow);
        if (arquivo == null) {
            return false;
        }

        return executarComando(
                arquivo,
                new String[]{"mysql", "-u" + USUARIO, NOME_BANCO},
                senha,
                false,
                "Banco restaurado com sucesso: ",
                "Erro ao restaurar banco. Codigo de saida: ",
                "Erro na restauracao: "
        );
    }

    /**
     * Executa mysqldump/mysql com o arquivo redirecionado via
     * ProcessBuilder.Redirect (sem passar por shell nenhum) e a senha
     * (se houver) via variavel de ambiente MYSQL_PWD do processo filho.
     *
     * @param saidaParaArquivo true = comando escreve no arquivo (backup);
     *                         false = comando le do arquivo (restauracao)
     */
    private static boolean executarComando(File arquivo, String[] comandoBase, String senha,
                                            boolean saidaParaArquivo,
                                            String msgSucesso, String msgErroCodigo, String msgErroExcecao) {
        try {
            ProcessBuilder pb = new ProcessBuilder(comandoBase);

            if (senha != null && !senha.isBlank()) {
                pb.environment().put("MYSQL_PWD", senha);
            }

            if (saidaParaArquivo) {
                pb.redirectOutput(arquivo);
            } else {
                pb.redirectInput(arquivo);
            }
            pb.redirectErrorStream(false);

            Process processo = pb.start();
            int status = processo.waitFor();

            if (status == 0) {
                LogDAO.registrar(msgSucesso + arquivo.getAbsolutePath());
                return true;
            } else {
                LogDAO.registrar(msgErroCodigo + status);
                return false;
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            LogDAO.registrar(msgErroExcecao + e.getMessage());
            return false;
        }
    }
}
