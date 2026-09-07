package com.wayne.wayneen.enterpriseswyne.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.net.URL;

public class HomeController {

    @FXML
    public void abrirLogin(ActionEvent event) {
        try {
            URL fxmlPath = getClass().getResource("/com/wayne/wayneen/enterpriseswyne/login.fxml");
            if (fxmlPath == null) {
                System.err.println("Erro: login.fxml não encontrado. Verifique o caminho em resources.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlPath);
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage loginStage = new Stage();
            loginStage.setTitle("Tela de Login");
            loginStage.setScene(scene);
            loginStage.show();

            // Fecha a janela atual
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            System.err.println("Erro ao abrir a tela de login:");
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirSobreEmpresa(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sobre a Empresa");
        alert.setHeaderText("Wayne Enterprises");
        alert.setContentText("A Wayne Enterprises é líder global em soluções inovadoras, " +
                "atuando em setores como tecnologia, segurança e pesquisa avançada.");
        alert.showAndWait();
    }

    @FXML
    private void abrirSobreDialog(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sobre");
        alert.setHeaderText("Informações do Sistema");
        alert.setContentText("Sistema de Gerenciamento Corporativo\nVersão: 1.0\n" +
                "Desenvolvido por: Equipe de TI - Wayne Enterprises");
        alert.showAndWait();
    }
}
