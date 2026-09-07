package com.wayne.wayneen.enterpriseswyne;

import com.wayne.wayneen.enterpriseswyne.DAO.FuncionarioDAOMethods;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import java.util.Map;

import javafx.scene.Node;
import javafx.stage.Stage;

public class KpiController {

    @FXML
    private Label lblMediaTempo;

    @FXML
    private BarChart<String, Number> chartCargos;

    @FXML
    private BarChart<String, Number> chartDepartamentos;

    @FXML
    public void initialize() {
        preencherGraficoCargos();
        preencherGraficoDepartamentos();
        preencherTempoMedio();
    }

    private void preencherGraficoCargos() {
        Map<String, Integer> cargos = FuncionarioDAOMethods.contarCargos();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Cargos");

        cargos.forEach((cargo, total) -> {
            series.getData().add(new XYChart.Data<>(cargo, total));
        });

        chartCargos.getData().add(series);
    }

    private void preencherGraficoDepartamentos() {
        Map<String, Integer> departamentos = FuncionarioDAOMethods.contarDepartamentos();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Departamentos");

        departamentos.forEach((departamento, total) -> {
            series.getData().add(new XYChart.Data<>(departamento, total));
        });

        chartDepartamentos.getData().add(series);
    }

    private void preencherTempoMedio() {
        double media = FuncionarioDAOMethods.calcularTempoMedioEmpresa();
        lblMediaTempo.setText(String.format("%.2f anos", media));
    }
    public void fecharJanela(ActionEvent actionEvent) {
        // Obtém o Stage a partir do botão que acionou o evento
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
