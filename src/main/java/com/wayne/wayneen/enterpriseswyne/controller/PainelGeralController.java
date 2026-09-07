package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.LogDAO;
import com.wayne.wayneen.enterpriseswyne.controller.ExportacaoController;
import com.wayne.wayneen.enterpriseswyne.model.ProcessoSeletivo;
import com.wayne.wayneen.enterpriseswyne.model.SessionManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import com.wayne.wayneen.enterpriseswyne.controller.NotificacaoController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;



import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.net.URL;
import java.util.Objects;
public class PainelGeralController {

    @FXML
    private void abrirCadastroFuncionario() {
        abrirTela("/com/wayne/wayneen/enterpriseswyne/cadastro_funcionario.fxml", "Cadastro de Funcionários");
        LogDAO.registrar("Funcionários", "Novo funcionário cadastrado");
    }


    @FXML
    private void abrirPainelAvaliacao() {
        abrirTela("/com/wayne/wayneen/enterpriseswyne/avaliacao.fxml", "Avaliação de Desempenho");
    }

    @FXML
    private void abrirTelaFerias() {
        abrirTela("/com/wayne/wayneen/enterpriseswyne/listar_ferias.fxml", "Férias e Licenças");
    }

    @FXML
    private void abrirModuloKPI() {
        abrirTela("/com/wayne/wayneen/enterpriseswyne/relatorio_kpis.fxml", "Inteligência de Dados - KPIs");
    }

    @FXML
    private void abrirRelatorioGrafico() {
        abrirTela("/com/wayne/wayneen/enterpriseswyne/relatorio_grafico.fxml", "Relatório Gráfico de Funcionários");
    }

    @FXML
    private void abrirTelaBackup() {
        abrirTela("/com/wayne/wayneen/enterpriseswyne/restauracao.fxml", "Backup e Restauração");
    }

    @FXML
    private void abrirTelaAtualizacaoMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/atualizar_funcionario.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Atualizar Funcionário");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            exibirErro("Erro ao abrir tela de atualização: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void excluirFuncionario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/confirmar_exclusao.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Excluir Funcionário");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            exibirErro("Erro ao abrir tela de exclusão: " + e.getMessage());
            e.printStackTrace();
        }
        LogDAO.registrar("Funcionários", "Novo funcionário cadastrado");
    }

    // Método utilitário para abrir janelas
    private void abrirTela(String caminhoFXML, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFXML));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            exibirErro("Erro ao abrir a tela: " + titulo + "\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------
    @FXML

    protected void abrirTelaListagemFuncionarios() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/listar_funcionarios.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Listagem de Funcionários");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Erro ao carregar a tela de listagem.");
            e.printStackTrace();
        } catch (Exception e) {
            mostrarAlerta("Erro inesperado.");
            e.printStackTrace();
        }
        LogDAO.registrar("Funcionários", "Novo funcionário cadastrado");
    }

    //-------------------------------------------------------------------------------------------------------------------------
    @FXML
    private void abrirTelaCadastroFerias() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/cadastro_ferias.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Cadastro de Férias");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            mostrarAlerta("Erro");
            e.printStackTrace();
        }
        LogDAO.registrar("Funcionários", "Novo funcionário cadastrado");
    }

    //---------------------------------------------------------------------------------------------------------------------------
    @FXML
    private void abrirDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Dashboard Corporativo");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            exibirErro("Erro ao abrir o Dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------------
    @FXML

    private void abrirAgendaCorporativa() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/agenda_corporativa.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Agenda Corporativa");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("❌ Erro ao carregar o FXML da Agenda Corporativa:");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado ao abrir a Agenda Corporativa:");
            e.printStackTrace();
        }
    }

    //--------------------------------------------------------------------------------------------------------------------
    @FXML
    private void abrirAvisosInternos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/avisos.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Avisos Internos");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Erro ao carregar a tela de Avisos Internos:");
            e.printStackTrace();
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------------
    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exportação de PDF");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    //-------------------------------------------------------------------------------------------------------------------------------
    public void mostrarAlertaExport(String mensagem) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Exportação de Relatório");
            alert.setHeaderText(null);
            alert.setContentText(mensagem);
            alert.showAndWait();
        });
    }

    //-------------------------------------------------------------------------------------------------------------------------
    @FXML
    private void abrirExportacaoDocumentos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/controle_documentos.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Gestão de Documentos");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            ExportacaoController.mostrarAlerta("Erro ao abrir a tela de exportação: " + e.getMessage());
        }
    }

    //------------------------------------------------------------------------------------------------------------------------
    @FXML
    private void abrirControleEquipamentos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/equipamento.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Controle de Equipamentos");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao abrir a tela de Controle de Equipamentos:\n" + e.getMessage());
        }
    }

    //----------------------------------------------------------------------------------------------------------------------------------------
    @FXML
    private void abrirChamados() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/chamados.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Chamados de Suporte");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------------------------------------------
    @FXML
    private void abrirAvisos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/avisos.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Avisos Internos");
            stage.setScene(new Scene(root));
            stage.setResizable(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Não foi possível abrir o módulo de Avisos.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    //---------------------------------------------------------------------------------------------------------------------------
    @FXML
    private void abrirPainelEventos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/painel_eventos.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Painel Geral de Eventos");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao abrir o Painel de Eventos: " + e.getMessage());
            alert.showAndWait();
        }
    }


    //----------------------------------------------------------------------------------------------------------------------------
    @FXML
    private Button btnSair;

    //-------------------------------------------------------------------------------------------------------------------------------
    @FXML
    private void sairDoSistema() {
        // Fecha apenas a janela atual (Painel Geral)
        Stage stage = (Stage) btnSair.getScene().getWindow();
        stage.close();

        // Se desejar encerrar todo o sistema:
        // Platform.exit(); // <-- descomente se quiser encerrar a aplicação inteira
    }

    //---------------------------------------------------------------------------------------------------------------------------------
    private void exibirErro(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Erro");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    //--------------------------------------------------------------------------------------------------------------------------------

    @FXML
    private void abrirLogAuditoria() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/log_auditoria.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Histórico de Ações do Sistema");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirCargos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/cargo_salario.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Plano de Cargos e Salários");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarErro("Erro ao abrir a tela de Cargos");
        }
    }

    //----------------------------------------------------------------------------------------------------------------------------------------
    @FXML
    private void abrirBeneficios() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/beneficios.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Benefícios");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao abrir a tela de Benefícios");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------
    @FXML
    private void abrirTreinamentos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/treinamentos.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Treinamentos");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarErro("Erro ao abrir a tela de Treinamentos");
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------------------
    @FXML
    private void abrirParticipacoes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/participacao_treinamento.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Participações em Treinamentos");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erro ao abrir a tela de Participações em Treinamentos:");
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------
    @FXML
    private void abrirRecrutamento() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/recrutamento.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestão de Recrutamento");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erro ao abrir a tela de Recrutamento:");
            e.printStackTrace();
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------------------
    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    //-----------------------------------------------------------------------------------------------------------------------
    @FXML
    private void abrirPlanoCargos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/plano_cargos.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Plano de Cargos");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao abrir a tela de Cargos");
            alert.showAndWait();
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------
    @FXML
    private TableView<ProcessoSeletivo> tabelaProcessos; // Definindo a tabela corretamente

    // PainelGeralController.java
    @FXML
    private void abrirProcessoSeletivo() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/wayne/wayneen/enterpriseswyne/processo_seletivo.fxml")
            );
            Parent root = loader.load();

            // Pegue o controller da tela de processos (se precisar passar algo):
            ProcessoSeletivoController controller =
                    loader.getController();
            // Ex.: controller.preSelecionar(processo); // se precisar

            Stage stage = new Stage();
            stage.setTitle("Processos Seletivos");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // log/alert
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------------
    private void showErrorAlert(String title, String message) {
        // Exibindo um alerta de erro
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------

    @FXML
    private void abrirCadastroCurriculo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/curriculo_form.fxml"));
            Parent root = loader.load();
            Stage st = new Stage();
            st.setTitle("Cadastro de Currículo");
            st.setScene(new Scene(root));
            st.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------------------

    @FXML
    private void abrirListaCurriculos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/curriculo_list.fxml"));
            Parent root = loader.load();
            Stage st = new Stage();
            st.setTitle("Banco de Currículos");
            st.setScene(new Scene(root));
            st.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------

    @FXML
    private void abrirSobreEmpresa() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sobre a Empresa");
        alert.setHeaderText("Wayne Enterprises");
        alert.setContentText("A Wayne Enterprises é líder global em soluções inovadoras, " +
                "atuando em diversos setores como tecnologia, segurança e pesquisa avançada.");
        alert.showAndWait();
    }

    /**
     * Abre um diálogo de informações genéricas "Sobre"
     */
    @FXML
    private void abrirSobreDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sobre");
        alert.setHeaderText("Informações do Sistema");
        alert.setContentText("Sistema de Gerenciamento Corporativo\nVersão: 1.0\n" +
                "Desenvolvido por: Equipe de TI - Wayne Enterprises");
        alert.showAndWait();
    }

    //-----------------------------------------------------------------------------------------------------------------------

    @FXML
    private void abrirCentralNotificacoes() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/wayne/wayneen/enterpriseswyne/notificacao_central.fxml")
            );
            Parent root = loader.load();

            // guarda a referência, se quiser injetar no Enviar depois
            NotificacaoController centralCtrl = loader.getController();
            setNotificacaoCentralController(centralCtrl); // opcional, se você tiver esse setter

            Stage stage = new Stage();
            stage.setTitle("Central de Notificações");
            Scene scene = new Scene(root);
            try {
                scene.getStylesheets().add(
                        getClass().getResource("/com/wayne/wayneen/enterpriseswyne/app.css").toExternalForm()
                );
            } catch (Exception ignore) {
            }

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,
                    (e.getMessage() == null || e.getMessage().isBlank())
                            ? e.getClass().getSimpleName() : e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }
//---------------------------------------------------------------------------------------------------------------------------

    private NotificacaoController notificacaoCentralController;

    /**
     * Setter para guardar a central quando ela for aberta neste painel.
     */
    private void setNotificacaoCentralController(NotificacaoController centralCtrl) {
        this.notificacaoCentralController = centralCtrl;
    }

    /**
     * (Opcional) Abre a Central de Notificações e guarda a referência do controller,
     * permitindo que o envio injete e atualize a central em tempo real.
     */
   //----------------------------------------------------------------------------------------------------------------------------------

    @FXML
    public void abrirCentralNotificacoes(ActionEvent event) {
        final String ABS = "/com/wayne/wayneen/enterpriseswyne/notificacao_central.fxml";
        try {
            // 1) Tenta pelo caminho absoluto em resources/
            URL url = getClass().getResource(ABS);

            // 2) Fallback: se o FXML estiver ao lado do controller (não recomendado, mas ajuda a diagnosticar)
            if (url == null) {
                url = NotificacaoController.class.getResource("notificacao_central.fxml");
            }

            // 3) Se ainda não achou, loga candidatos e avisa
            if (url == null) {
                System.err.println(">> ABS = " + ABS);
                System.err.println(">> getClass().getResource(ABS) = " + getClass().getResource(ABS));
                System.err.println(">> NotificacaoController.class.getResource('notificacao_central.fxml') = "
                        + NotificacaoController.class.getResource("notificacao_central.fxml"));
                System.err.println(">> ContextCL = " +
                        Thread.currentThread().getContextClassLoader()
                                .getResource("com/wayne/wayneen/enterpriseswyne/notificacao_central.fxml"));
                new Alert(Alert.AlertType.ERROR,
                        "Recurso FXML 'notificacao_central.fxml' não encontrado em: " + ABS +
                                "\nVerifique se foi copiado para target/classes.").showAndWait();
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            // guarda a referência da central (para uso no envio)
            NotificacaoController centralCtrl = loader.getController();
            setNotificacaoCentralController(centralCtrl);

            Stage stage = new Stage();
            stage.setTitle("Central de Notificações");

            if (event != null && event.getSource() instanceof Node n && n.getScene() != null) {
                stage.initOwner(n.getScene().getWindow());
            }

            Scene scene = new Scene(root);
            URL css = getClass().getResource("/com/wayne/wayneen/enterpriseswyne/app.css");
            if (css != null) scene.getStylesheets().add(css.toExternalForm());

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            // Mostra a causa real (inclui linha do FXML quando houver)
            Throwable cause = e;
            while (cause.getCause() != null) cause = cause.getCause();
            String msg = (cause.getMessage() == null || cause.getMessage().isBlank())
                    ? cause.getClass().getSimpleName()
                    : cause.getMessage();
            new Alert(Alert.AlertType.ERROR, "Não foi possível abrir a Central.\n\n" + msg).showAndWait();
            e.printStackTrace();
        }
    }



    //----------------------------------------------------------------------------------------------------------------------------
    public final class FxLoader {

        private FxLoader() {}

        /**
         * Tenta carregar um FXML experimentando múltiplos caminhos.
         * Ex.: FxLoader.loadFXML(this, "enviar_notificacao.fxml", "view/enviar_notificacao.fxml",
         *                        "/com/wayne/wayneen/enterpriseswyne/enviar_notificacao.fxml",
         *                        "/com/wayne/wayneen/enterpriseswyne/view/enviar_notificacao.fxml");
         */
        public static FXMLLoader loadFXML(Object caller, String... candidates) throws Exception {
            Class<?> base = (caller instanceof Class<?> c) ? c : caller.getClass();
            List<String> tentados = new ArrayList<>();
            for (String c : candidates) {
                if (c == null || c.isBlank()) continue;

                // 1) Absoluto com a classe do caller (suporta módulos)
                URL url = base.getResource(c.startsWith("/") ? c : "/" + c);
                if (url != null) {
                    System.out.println("[FxLoader] Carregado (abs): " + url);
                    return new FXMLLoader(url);
                }

                // 2) Relativo ao pacote do caller
                url = base.getResource(c.startsWith("/") ? c.substring(1) : c);
                if (url != null) {
                    System.out.println("[FxLoader] Carregado (rel): " + url);
                    return new FXMLLoader(url);
                }

                // 3) ContextClassLoader raiz (classpath “puro”)
                url = Thread.currentThread().getContextClassLoader()
                        .getResource(c.startsWith("/") ? c.substring(1) : c);
                if (url != null) {
                    System.out.println("[FxLoader] Carregado (ctx): " + url);
                    return new FXMLLoader(url);
                }

                tentados.add(c);
            }
            throw new IllegalStateException(
                    "FXML não encontrado. Caminhos tentados: " + tentados +
                            "\nDica: verifique se o arquivo está em src/main/resources e o nome (maiúsc/minúsc)."
            );
        }

        /** Atalho que já faz o load e retorna o root. */
        public static Parent loadRoot(Object caller, String... candidates) throws Exception {
            FXMLLoader loader = loadFXML(caller, candidates);
            return loader.load();
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------------


    @FXML
    public void abrirEnviarNotificacoes(ActionEvent e) {
        try {
            FXMLLoader loader = FxLoader.loadFXML(this,
                    "enviar_notificacao.fxml",
                    "view/enviar_notificacao.fxml",
                    "/com/wayne/wayneen/enterpriseswyne/enviar_notificacao.fxml",
                    "/com/wayne/wayneen/enterpriseswyne/view/enviar_notificacao.fxml"
            );
            Parent root = loader.load();

            EnviarNotificacaoController enviarCtrl = loader.getController();
            if (enviarCtrl != null && notificacaoCentralController != null) {
                enviarCtrl.setCentralController(notificacaoCentralController);
            }

            Stage dialog = new Stage();
            dialog.setTitle("Enviar Notificação");
            dialog.initModality(Modality.APPLICATION_MODAL);
            if (e != null && e.getSource() instanceof Node n && n.getScene() != null) {
                dialog.initOwner(n.getScene().getWindow());
            }
            Scene scene = new Scene(root);
            URL css = getClass().getResource("/com/wayne/wayneen/enterpriseswyne/app.css");
            if (css != null) scene.getStylesheets().add(css.toExternalForm());
            dialog.setScene(scene);
            dialog.showAndWait();

        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR,
                    "Falha ao abrir Enviar Notificação.\n\n" + (ex.getMessage()==null?ex.getClass().getSimpleName():ex.getMessage())
            ).showAndWait();
            ex.printStackTrace();
        }
    }

   //-------------------------------------------------------------------------------------------------------------------------------------------

    @FXML
    private void abrirChat() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/chat.fxml"));
            Parent root = loader.load();

            ChatController ctrl = loader.getController();
            SessionManager UsuarioProvider = null;
            ctrl.setUsuarioLogado(UsuarioProvider.getUsuarioLogado()); // ou passe o Usuario atual

            Stage stage = new Stage();
            stage.setTitle("Chat Interno - Wayne Enterprises");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}









