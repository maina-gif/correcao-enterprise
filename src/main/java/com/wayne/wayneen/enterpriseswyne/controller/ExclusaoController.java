package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.FuncionarioDAOMethods;
import com.wayne.wayneen.enterpriseswyne.model.Funcionario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ExclusaoController {

    private Funcionario funcionarioSelecionado;

    @FXML private Button cancelarBtn;
    @FXML private Button confirmarBtn;

    /** Define o funcionário que será excluído (recebido pela tela de listagem) */
    public void setFuncionario(Funcionario funcionario) {
        this.funcionarioSelecionado = funcionario;
    }

    /** Ação do botão "Cancelar" */
    @FXML
    private void cancelar() {
        fecharJanela(cancelarBtn);
    }

    /** Ação do botão "Confirmar" */
    @FXML
    private void confirmarExclusao() {
        if (funcionarioSelecionado == null) {
            mostrarAlerta("Aviso", "Nenhum funcionário selecionado para exclusão.", Alert.AlertType.WARNING);
            fecharJanela(confirmarBtn);
            return;
        }

        boolean sucesso = FuncionarioDAOMethods.excluir(funcionarioSelecionado.getId());

        if (sucesso) {
            mostrarAlerta("Sucesso", "Funcionário excluído com sucesso!", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Erro", "Erro ao excluir o funcionário.", Alert.AlertType.ERROR);
        }

        fecharJanela(confirmarBtn);
    }

    /* ---------------- utilitários ---------------- */

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void fecharJanela(Button btn) {
        if (btn != null && btn.getScene() != null && btn.getScene().getWindow() instanceof Stage stage) {
            stage.close();
        }
    }
}
