// Classe DashboardController.java
package com.wayne.wayneen.enterpriseswyne;

import com.wayne.wayneen.enterpriseswyne.DAO.FuncionarioDAOMethods;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.util.Map;

public class DashboardController {

    @FXML private Label totalFuncionariosLabel;
    @FXML private Label mediaTempoLabel;
    @FXML private Label setorMaisLabel;
    @FXML private BarChart<String, Number> graficoSetores;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;

    @FXML
    public void initialize() {
        carregarIndicadores();
    }

    @FXML

    private void carregarIndicadores() {
        int total = FuncionarioDAOMethods.contarFuncionarios();
        double media = FuncionarioDAOMethods.calcularMediaTempoPermanencia();
        String setorMais = FuncionarioDAOMethods.setorComMaisFuncionarios();

        totalFuncionariosLabel.setText(String.valueOf(total));
        mediaTempoLabel.setText(String.format("%.2f anos", media));
        setorMaisLabel.setText(setorMais);

        carregarGrafico();
    }

    @FXML
    private void carregarGrafico() {
        Map<String, Integer> dados = FuncionarioDAOMethods.contarPorSetor();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Funcionários por Setor");

        for (Map.Entry<String, Integer> entry : dados.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        graficoSetores.getData().add(series);
    }
}
