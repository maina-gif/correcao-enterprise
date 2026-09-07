package com.wayne.wayneen.enterpriseswyne;

import com.wayne.wayneen.enterpriseswyne.DAO.DocumentoDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;

public class DocumentoController {

    @FXML private TextField tituloField;
    @FXML private DatePicker validadePicker;
    private File arquivoSelecionado;

    @FXML
    private void selecionarArquivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Documento");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("Imagens", "*.jpg", "*.png", "*.jpeg")
        );
        File arquivo = fileChooser.showOpenDialog(null);

        if (arquivo != null) {
            arquivoSelecionado = arquivo;
            mostrarAlerta("Arquivo", "Arquivo selecionado com sucesso:\n" + arquivo.getName(), Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void salvarDocumento() {
        String titulo = tituloField.getText();
        LocalDate validade = validadePicker.getValue();

        if (titulo.isEmpty() || validade == null || arquivoSelecionado == null) {
            mostrarAlerta("Erro", "Preencha todos os campos e selecione um arquivo.", Alert.AlertType.ERROR);
            return;
        }

        Documento documento = new Documento();
        documento.setTitulo(titulo);
        documento.setValidade(validade);
        documento.setCaminhoArquivo(arquivoSelecionado.getAbsolutePath());

        boolean sucesso = DocumentoDAO.salvar(documento);
        if (sucesso) {
            mostrarAlerta("Sucesso", "Documento salvo com sucesso!", Alert.AlertType.INFORMATION);
            limparCampos();
        } else {
            mostrarAlerta("Erro", "Erro ao salvar o documento.", Alert.AlertType.ERROR);
        }
    }

    private void limparCampos() {
        tituloField.clear();
        validadePicker.setValue(null);
        arquivoSelecionado = null;
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) tituloField.getScene().getWindow();
        stage.close();
    }
}
