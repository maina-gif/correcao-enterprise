package com.wayne.wayneen.enterpriseswyne;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Carrega a tela inicial (ajuste o nome se o arquivo tiver outra grafia)
            URL fxml = Objects.requireNonNull(
                    getClass().getResource("/com/wayne/wayneen/enterpriseswyne/Home.fxml"),
                    "FXML inicial não encontrado em /com/wayne/wayneen/enterpriseswyne/Home.fxml"
            );
            Parent root = FXMLLoader.load(fxml);
            Scene scene = new Scene(root);

            // CSS é opcional: só aplica se existir (evita NullPointerException)
            URL css = getClass().getResource("/com/wayne/wayneen/enterpriseswyne/app.css");
            if (css != null) {
                scene.getStylesheets().add(css.toExternalForm());
            }

            primaryStage.setTitle("Login - Wayne Enterprises");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Erro ao iniciar a aplicação: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
