package com.wayne.wayneen.enterpriseswyne;

import com.wayne.wayneen.enterpriseswyne.model.BackupRestauracao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class RestauracaoController {

    @FXML
    private Button btnBackup;

    @FXML
    private Button btnRestaurar;

    @FXML
    private PasswordField campoSenha;

    @FXML
    void fazerBackup(ActionEvent event) {
        Stage stage = (Stage) btnBackup.getScene().getWindow();
        boolean sucesso = BackupRestauracao.realizarBackup(stage);

        if (sucesso) {
            mostrarAlerta("Backup", "Backup realizado com sucesso!", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Erro", "Falha ao realizar o backup!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void restaurarBackup(ActionEvent event) {
        String senha = campoSenha.getText();
        if (senha == null || senha.trim().isEmpty()) {
            mostrarAlerta("Erro", "A senha é obrigatória!", Alert.AlertType.ERROR);
            return;
        }

        Stage stage = (Stage) btnRestaurar.getScene().getWindow();
        boolean sucesso = BackupRestauracao.restaurarBackup(stage, senha);

        if (sucesso) {
            mostrarAlerta("Restauração", "Restauração concluída com sucesso!", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Erro", "Erro ao restaurar o banco de dados!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) campoSenha.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
