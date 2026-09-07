package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.FuncionarioDAOMethods;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

import java.util.Map;

public class RelatorioGraficoController {

    @FXML
    private PieChart graficoDepartamentos;

    @FXML
    public void initialize() {
        gerarGrafico();
    }

    private void gerarGrafico() {
        // Pega do DAO já agregado por departamento
        Map<String, Integer> contagem = FuncionarioDAOMethods.contarDepartamentos();

        ObservableList<PieChart.Data> dados = FXCollections.observableArrayList();
        contagem.forEach((depto, qtd) ->
                dados.add(new PieChart.Data(
                        (depto == null || depto.isBlank()) ? "Não informado" : depto,
                        qtd
                ))
        );

        graficoDepartamentos.setData(dados);
        if (graficoDepartamentos.getTitle() == null || graficoDepartamentos.getTitle().isBlank()) {
            graficoDepartamentos.setTitle("Funcionários por departamento");
        }
    }
}
