package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.Evento;
import com.wayne.wayneen.enterpriseswyne.DAO.EventoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraficoEventosController {

    @FXML private PieChart graficoPizza;

    @FXML
    public void initialize() {
        List<Evento> eventos = EventoDAO.listarTodos();

        Map<String, Integer> contagemPorTipo = new HashMap<>();
        for (Evento e : eventos) {
            String tipo = e.getTipo() != null ? e.getTipo() : "Outro";
            contagemPorTipo.put(tipo, contagemPorTipo.getOrDefault(tipo, 0) + 1);
        }

        ObservableList<PieChart.Data> dadosGrafico = FXCollections.observableArrayList();
        contagemPorTipo.forEach((tipo, qtd) ->
                dadosGrafico.add(new PieChart.Data(tipo + " (" + qtd + ")", qtd))
        );

        graficoPizza.setData(dadosGrafico);
        graficoPizza.setLegendVisible(true);
        graficoPizza.setLabelsVisible(true);
    }
}
