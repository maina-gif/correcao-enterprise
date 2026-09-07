package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.FuncionarioDAOMethods;
import com.wayne.wayneen.enterpriseswyne.DAO.LogDAO;
import com.wayne.wayneen.enterpriseswyne.model.Funcionario;
import com.wayne.wayneen.enterpriseswyne.model.PDFGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListagemController {

    @FXML private TextField filtroNomeField;
    @FXML private TableView<Funcionario> tabelaFuncionarios;
    @FXML private TableColumn<Funcionario, Integer> colunaId;
    @FXML private TableColumn<Funcionario, String> colunaNome;
    @FXML private TableColumn<Funcionario, String> colunaCpf;
    @FXML private TableColumn<Funcionario, String> colunaCargo;
    @FXML private TableColumn<Funcionario, String> colunaDepartamento;
    @FXML private TableColumn<Funcionario, String> colunaEmail;
    @FXML private TableColumn<Funcionario, LocalDate> colunaDataAdmissao; // <-- LocalDate
    @FXML private TableColumn<Funcionario, Void> colunaEditar;
    @FXML private TableColumn<Funcionario, Void> colunaExcluir;

    private final ObservableList<Funcionario> listaFuncionarios = FXCollections.observableArrayList();
    private static final DateTimeFormatter BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        configurarColunas();
        // Placeholder quando não há dados
        tabelaFuncionarios.setPlaceholder(new Label("Nenhum funcionário encontrado."));
        carregarFuncionarios();
    }

    private void configurarColunas() {
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nomeCompleto"));
        colunaCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colunaCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        colunaDepartamento.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        colunaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colunaDataAdmissao.setCellValueFactory(new PropertyValueFactory<>("dataAdmissao"));

        // Formatar a data como dd/MM/yyyy
        colunaDataAdmissao.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(BR.format(item));
                }
            }
        });

        adicionarBotaoEditar();
        adicionarBotaoExcluir();
    }

    private void carregarFuncionarios() {
        listaFuncionarios.clear();
        String filtro = (filtroNomeField != null && filtroNomeField.getText() != null)
                ? filtroNomeField.getText().trim()
                : "";
        List<Funcionario> funcionarios = FuncionarioDAOMethods.buscarPorNome(filtro);
        listaFuncionarios.addAll(funcionarios);
        tabelaFuncionarios.setItems(listaFuncionarios);
    }

    private void adicionarBotaoEditar() {
        colunaEditar.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Editar");

            {
                btn.setOnAction(event -> {
                    int idx = getIndex();
                    if (idx >= 0 && idx < getTableView().getItems().size()) {
                        Funcionario funcionario = getTableView().getItems().get(idx);
                        abrirTelaAtualizar(funcionario);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
                setText(null);
            }
        });
    }

    private void adicionarBotaoExcluir() {
        colunaExcluir.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Excluir");

            {
                btn.setOnAction(event -> {
                    int idx = getIndex();
                    if (idx >= 0 && idx < getTableView().getItems().size()) {
                        Funcionario funcionario = getTableView().getItems().get(idx);

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmar Exclusão");
                        alert.setHeaderText(null);
                        alert.setContentText("Deseja excluir o funcionário " + funcionario.getNomeCompleto() + "?");

                        alert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                boolean sucesso = FuncionarioDAOMethods.excluir(funcionario.getId());
                                if (sucesso) {
                                    LogDAO.registrar("Excluiu funcionário: " + funcionario.getNomeCompleto());
                                    mostrarAlerta("Sucesso", "Funcionário excluído com sucesso!", Alert.AlertType.INFORMATION);
                                    carregarFuncionarios();
                                } else {
                                    mostrarAlerta("Erro", "Erro ao excluir o funcionário.", Alert.AlertType.ERROR);
                                }
                            }
                        });
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
                setText(null);
            }
        });
    }

    private void abrirTelaAtualizar(Funcionario funcionario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/tela_atualizar.fxml"));
            AnchorPane root = loader.load();

            AtualizarController controller = loader.getController();
            controller.carregarFuncionario(funcionario);

            Stage stage = new Stage();
            stage.setTitle("Atualizar Funcionário");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            carregarFuncionarios(); // Atualiza a tabela após a edição
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível abrir a tela de atualização.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void exportarPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Relatório PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        fileChooser.setInitialFileName("relatorio_funcionarios.pdf");

        // Obtém a janela atual (pode ser null se não estiver em cena ainda)
        Stage owner = (Stage) (tabelaFuncionarios.getScene() != null
                ? tabelaFuncionarios.getScene().getWindow()
                : null);

        File file = (owner != null)
                ? fileChooser.showSaveDialog(owner)
                : fileChooser.showSaveDialog(null);

        if (file != null) {
            // Chama diretamente a classe (sem sombrear o nome com variável local)
            PDFGenerator.gerar(listaFuncionarios, file.getAbsolutePath());
            LogDAO.registrar("Relatório PDF exportado: " + file.getAbsolutePath());
            mostrarAlerta("Sucesso", "Relatório PDF gerado com sucesso!", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    public void aplicarFiltro(ActionEvent event) {
        carregarFuncionarios();
    }

    @FXML
    public void abrirTelaCadastro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/cadastro_funcionario.fxml"));
            AnchorPane root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Cadastro de Funcionário");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            carregarFuncionarios(); // Atualiza a lista após o cadastro
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao abrir tela de cadastro.", Alert.AlertType.ERROR);
        }
    }

    public void abrirTelaListagemFuncionarios() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/tela_listagem.fxml"));
            AnchorPane root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Listagem de Funcionários");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao abrir a tela de listagem.", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
