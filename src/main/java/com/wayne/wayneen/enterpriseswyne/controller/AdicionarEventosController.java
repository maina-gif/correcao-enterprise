package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.Evento;
import com.wayne.wayneen.enterpriseswyne.DAO.EventoDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AdicionarEventosController {

    @FXML private TextField campoTitulo;
    @FXML private TextArea campoDescricao;
    @FXML private DatePicker campoData;
    @FXML private TextField campoLocal;
    @FXML private ComboBox<String> campoTipo;

    @FXML
    private void salvarEvento() {
        String titulo = campoTitulo.getText() != null ? campoTitulo.getText().trim() : "";
        String descricao = campoDescricao.getText() != null ? campoDescricao.getText().trim() : "";
        LocalDate data = campoData.getValue();
        String local = campoLocal.getText() != null ? campoLocal.getText().trim() : "";
        String tipo = campoTipo.getValue() != null ? campoTipo.getValue().trim() : "";

        if (titulo.isEmpty() || data == null || local.isEmpty() || tipo.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Preencha todos os campos obrigatórios (Título, Data, Local, Tipo).");
            return;
        }

        Evento evento = new Evento(titulo, descricao, data, local, tipo);

        try {
            boolean sucesso = EventoDAO.salvar(evento);
            if (sucesso) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "✅ Evento cadastrado com sucesso!");
                fecharJanela();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "❌ Não foi possível salvar o evento.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Erro inesperado", "❌ Ocorreu um erro ao salvar o evento:\n" + e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) campoTitulo.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
