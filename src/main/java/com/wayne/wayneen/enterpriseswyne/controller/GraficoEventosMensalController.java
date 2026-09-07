package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.Evento;
import com.wayne.wayneen.enterpriseswyne.DAO.EventoDAO;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.time.format.TextStyle;
import java.util.*;

public class GraficoEventosMensalController {

    @FXML private BarChart<String, Number> graficoBarra;
    @FXML private CategoryAxis eixoX;
    @FXML private NumberAxis eixoY;

    @FXML
    public void initialize() {
        Map<String, Integer> eventosPorMes = new LinkedHashMap<>();
        Locale localeBR = new Locale("pt", "BR");

        for (int i = 1; i <= 12; i++) {
            String nomeMes = java.time.Month.of(i).getDisplayName(TextStyle.FULL, localeBR);
            eventosPorMes.put(nomeMes, 0);
        }

        for (Evento evento : EventoDAO.listarTodos()) {
            String mes = evento.getData().getMonth().getDisplayName(TextStyle.FULL, localeBR);
            eventosPorMes.put(mes, eventosPorMes.get(mes) + 1);
        }

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Eventos por Mês");

        eventosPorMes.forEach((mes, total) ->
                serie.getData().add(new XYChart.Data<>(mes, total))
        );

        graficoBarra.getData().add(serie);
    }
}
