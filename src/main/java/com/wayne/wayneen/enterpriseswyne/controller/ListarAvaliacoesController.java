package com.wayne.wayneen.enterpriseswyne;


import com.wayne.wayneen.enterpriseswyne.DAO.AvaliacaoDAO;
import com.wayne.wayneen.enterpriseswyne.model.Avaliacao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class ListarAvaliacoesController {

    @FXML private TextField filtroFuncionarioField;
    @FXML private DatePicker filtroDataInicial;
    @FXML private DatePicker filtroDataFinal;
    @FXML private TableView<Avaliacao> tabelaAvaliacoes;

    @FXML private TableColumn<Avaliacao, Integer> colId;
    @FXML private TableColumn<Avaliacao, Integer> colFuncionarioId;
    @FXML private TableColumn<Avaliacao, LocalDate> colData;
    @FXML private TableColumn<Avaliacao, Integer> colPontualidade;
    @FXML private TableColumn<Avaliacao, Integer> colProdutividade;
    @FXML private TableColumn<Avaliacao, Integer> colTrabalhoEquipe;
    @FXML private TableColumn<Avaliacao, String> colObservacoes;

    private ObservableList<Avaliacao> listaAvaliacoes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colFuncionarioId.setCellValueFactory(cellData -> cellData.getValue().funcionarioIdProperty().asObject());
        colData.setCellValueFactory(cellData -> cellData.getValue().dataAvaliacaoProperty());
        colPontualidade.setCellValueFactory(cellData -> cellData.getValue().pontualidadeProperty().asObject());
        colProdutividade.setCellValueFactory(cellData -> cellData.getValue().produtividadeProperty().asObject());
        colTrabalhoEquipe.setCellValueFactory(cellData -> cellData.getValue().trabalhoEquipeProperty().asObject());
        colObservacoes.setCellValueFactory(cellData -> cellData.getValue().observacoesProperty());

        carregarAvaliacoes();
    }

    @FXML
    private void carregarAvaliacoes() {
        Integer idFuncionario = null;
        LocalDate dataInicial = filtroDataInicial.getValue();
        LocalDate dataFinal = filtroDataFinal.getValue();

        if (!filtroFuncionarioField.getText().trim().isEmpty()) {
            try {
                idFuncionario = Integer.parseInt(filtroFuncionarioField.getText().trim());
            } catch (NumberFormatException e) {
                mostrarAlerta("ID inválido", "O ID do funcionário deve ser um número inteiro.", Alert.AlertType.WARNING);
                return;
            }
        }

        List<Avaliacao> resultado = AvaliacaoDAO.filtrarAvaliacoes(idFuncionario, dataInicial, dataFinal);
        listaAvaliacoes.setAll(resultado);
        tabelaAvaliacoes.setItems(listaAvaliacoes);
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
