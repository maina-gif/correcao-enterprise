package com.wayne.wayneen.enterpriseswyne;

import com.wayne.wayneen.enterpriseswyne.DAO.AvaliacaoDAO;
import com.wayne.wayneen.enterpriseswyne.model.Avaliacao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PainelAvaliacaoController {

    @FXML private TextField funcionarioIdField;
    @FXML private DatePicker dataInicioPicker;
    @FXML private DatePicker dataFimPicker;
    @FXML private TableView<Avaliacao> tabelaAvaliacoes;
    @FXML private TableColumn<Avaliacao, Integer> colId;
    @FXML private TableColumn<Avaliacao, Integer> colFuncionario;
    @FXML private TableColumn<Avaliacao, LocalDate> colData;
    @FXML private TableColumn<Avaliacao, Integer> colPontualidade;
    @FXML private TableColumn<Avaliacao, Integer> colProdutividade;
    @FXML private TableColumn<Avaliacao, Integer> colTrabalhoEquipe;
    @FXML private TableColumn<Avaliacao, String> colObservacoes;

    private ObservableList<Avaliacao> dados = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFuncionario.setCellValueFactory(new PropertyValueFactory<>("funcionarioId"));
        colData.setCellValueFactory(new PropertyValueFactory<>("dataAvaliacao"));
        colPontualidade.setCellValueFactory(new PropertyValueFactory<>("pontualidade"));
        colProdutividade.setCellValueFactory(new PropertyValueFactory<>("produtividade"));
        colTrabalhoEquipe.setCellValueFactory(new PropertyValueFactory<>("trabalhoEquipe"));
        colObservacoes.setCellValueFactory(new PropertyValueFactory<>("observacoes"));

        carregarAvaliacoes();
    }

    private void carregarAvaliacoes() {
        try {
            List<Avaliacao> lista = AvaliacaoDAO.listarTodas();
            dados.setAll(lista);
            tabelaAvaliacoes.setItems(dados);
        } catch (Exception e) {
            mostrarErro("Erro ao carregar avaliações: " + e.getMessage());
        }
    }

    @FXML
    private void filtrarAvaliacoes() {
        try {
            List<Avaliacao> filtradas = AvaliacaoDAO.listarTodas();

            String idTexto = funcionarioIdField.getText();
            if (!idTexto.isEmpty()) {
                int id = Integer.parseInt(idTexto);
                filtradas = filtradas.stream()
                        .filter(a -> a.getFuncionarioId() == id)
                        .collect(Collectors.toList());
            }

            LocalDate inicio = dataInicioPicker.getValue();
            LocalDate fim = dataFimPicker.getValue();

            if (inicio != null && fim != null) {
                filtradas = filtradas.stream()
                        .filter(a -> !a.getDataAvaliacao().isBefore(inicio) && !a.getDataAvaliacao().isAfter(fim))
                        .collect(Collectors.toList());
            }

            dados.setAll(filtradas);
        } catch (Exception e) {
            mostrarErro("Erro ao filtrar avaliações: " + e.getMessage());
        }
    }

    @FXML
    private void abrirNovaAvaliacao() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/avaliacao.fxml"));
            Scene cena = new Scene(loader.load());
            Stage novaJanela = new Stage();
            novaJanela.setTitle("Nova Avaliação");
            novaJanela.setScene(cena);
            novaJanela.show();
        } catch (Exception e) {
            mostrarErro("Erro ao abrir tela de nova avaliação.");
        }
    }

    private void mostrarErro(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
