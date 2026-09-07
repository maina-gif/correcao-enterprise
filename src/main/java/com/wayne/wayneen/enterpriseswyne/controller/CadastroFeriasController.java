package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.FeriasDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import com.wayne.wayneen.enterpriseswyne.model.Ferias;

public class CadastroFeriasController {

    @FXML private TextField funcionarioIdField;
    @FXML private DatePicker dataInicioPicker;
    @FXML private DatePicker dataFimPicker;
    @FXML private TextArea observacaoArea;

    @FXML
    private void salvarFerias() {
        try {
            int funcionarioId = Integer.parseInt(funcionarioIdField.getText());
            LocalDate inicio = dataInicioPicker.getValue();
            LocalDate fim = dataFimPicker.getValue();
            String observacao = observacaoArea.getText();

            com.wayne.wayneen.enterpriseswyne.model.Ferias f = new com.wayne.wayneen.enterpriseswyne.model.Ferias();
            f.setFuncionarioId(funcionarioId);
            f.setDataInicio(inicio);
            f.setDataFim(fim);
            f.setObservacao(observacao);

            FeriasDAO.salvar(f);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setContentText("Férias cadastradas com sucesso.");
            alert.showAndWait();

            funcionarioIdField.clear();
            dataInicioPicker.setValue(null);
            dataFimPicker.setValue(null);
            observacaoArea.clear();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setContentText("Erro ao salvar férias: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
