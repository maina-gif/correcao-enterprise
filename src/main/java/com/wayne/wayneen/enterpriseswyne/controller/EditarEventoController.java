package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.Evento;
import com.wayne.wayneen.enterpriseswyne.DAO.EventoDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EditarEventoController {

    @FXML private TextField campoTitulo;
    @FXML private TextArea campoDescricao;
    @FXML private DatePicker campoData;
    @FXML private TextField campoLocal;
    @FXML private ComboBox<String> campoTipo;

    private Evento evento;

    public void setEvento(Evento evento) {
        this.evento = evento;
        campoTitulo.setText(evento.getTitulo());
        campoDescricao.setText(evento.getDescricao());
        campoData.setValue(evento.getData());
        campoLocal.setText(evento.getLocal());
        campoTipo.setValue(evento.getTipo());
    }

    @FXML
    private void salvarAlteracoes() {
        if (evento == null) return;

        evento.setTitulo(campoTitulo.getText().trim());
        evento.setDescricao(campoDescricao.getText().trim());
        evento.setData(campoData.getValue());
        evento.setLocal(campoLocal.getText().trim());
        evento.setTipo(campoTipo.getValue());

        if (EventoDAO.atualizar(evento)) {
            mostrarAlerta("✅ Evento atualizado com sucesso.");
            fecharJanela();
        } else {
            mostrarAlerta("❌ Falha ao atualizar evento.");
        }
    }

    @FXML
    private void cancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) campoTitulo.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edição de Evento");
        alert.setContentText(msg);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
