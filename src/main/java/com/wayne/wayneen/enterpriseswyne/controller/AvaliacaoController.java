package com.wayne.wayneen.enterpriseswyne;


import com.wayne.wayneen.enterpriseswyne.DAO.AvaliacaoDAO;
import com.wayne.wayneen.enterpriseswyne.model.Avaliacao;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AvaliacaoController {

    @FXML private TextField idFuncionarioField;
    @FXML private DatePicker dataAvaliacaoPicker;
    @FXML private TextField pontualidadeField;
    @FXML private TextField produtividadeField;
    @FXML private TextField trabalhoEquipeField;
    @FXML private TextArea observacoesArea;

    @FXML
    private void salvarAvaliacao() {
        try {
            // Valida campos obrigatórios
            if (idFuncionarioField.getText().isEmpty() ||
                    pontualidadeField.getText().isEmpty() ||
                    produtividadeField.getText().isEmpty() ||
                    trabalhoEquipeField.getText().isEmpty() ||
                    dataAvaliacaoPicker.getValue() == null) {

                mostrarAlerta("Atenção", "Todos os campos obrigatórios devem ser preenchidos!", Alert.AlertType.WARNING);
                return;
            }

            // Tenta converter valores numéricos
            int idFuncionario = Integer.parseInt(idFuncionarioField.getText());
            int pontualidade = Integer.parseInt(pontualidadeField.getText());
            int produtividade = Integer.parseInt(produtividadeField.getText());
            int trabalhoEquipe = Integer.parseInt(trabalhoEquipeField.getText());

            Avaliacao a = new Avaliacao();
            a.setFuncionarioId(idFuncionario);
            a.setDataAvaliacao(dataAvaliacaoPicker.getValue());
            a.setPontualidade(pontualidade);
            a.setProdutividade(produtividade);
            a.setTrabalhoEquipe(trabalhoEquipe);
            a.setObservacoes(observacoesArea.getText());

            AvaliacaoDAO.salvar(a);
            mostrarAlerta("Sucesso", "Avaliação salva com sucesso!", Alert.AlertType.INFORMATION);
            limparCampos();

        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Preencha os campos numéricos corretamente.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao salvar avaliação: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void limparCampos() {
        idFuncionarioField.clear();
        dataAvaliacaoPicker.setValue(null);
        pontualidadeField.clear();
        produtividadeField.clear();
        trabalhoEquipeField.clear();
        observacoesArea.clear();
    }
}
