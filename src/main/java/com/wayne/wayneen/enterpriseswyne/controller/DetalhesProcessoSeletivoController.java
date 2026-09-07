package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.model.ProcessoSeletivo;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

public class DetalhesProcessoSeletivoController {

    @FXML
    private Label lblTitulo;

    @FXML
    private Label lblDescricao;

    @FXML
    private Label lblDataInicio;

    @FXML
    private Label lblDataFim;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Recebe o processo seletivo e preenche os campos da tela.
     */
    public void setProcessoSeletivo(ProcessoSeletivo processo) {
        if (processo != null) {
            lblTitulo.setText(processo.getTitulo());
            lblDescricao.setText(processo.getDescricao());
            lblDataInicio.setText(processo.getDataInicio() != null ? DATE_FMT.format(processo.getDataInicio()) : "-");
            lblDataFim.setText(processo.getDataFim() != null ? DATE_FMT.format(processo.getDataFim()) : "-");
        }
    }

    /**
     * Fecha a janela de detalhes.
     */
    @FXML
    private void fechar() {
        Stage stage = (Stage) lblTitulo.getScene().getWindow();
        stage.close();
    }
}
