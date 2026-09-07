package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.CurriculoDAO;
import com.wayne.wayneen.enterpriseswyne.model.Curriculo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;

public class CurriculoFormController {

    @FXML private TextField nomeField;
    @FXML private TextField emailField;
    @FXML private TextField telefoneField;
    @FXML private TextField cargoField;
    @FXML private TextField escolaridadeField;
    @FXML private TextField linkedinField;
    @FXML private ComboBox<String> statusBox;
    @FXML private TextArea skillsArea;
    @FXML private TextArea experienciaArea;
    @FXML private TextField pdfPathField;

    private final CurriculoDAO dao = new CurriculoDAO();

    @FXML
    public void initialize() {
        statusBox.getItems().setAll("NOVO","EM_ANALISE","ENTREVISTA","APROVADO","REPROVADO","RESERVA");
        statusBox.getSelectionModel().select("NOVO");
    }

    @FXML
    public void escolherPdf(ActionEvent e) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Selecionar Currículo (PDF)");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        File f = fc.showOpenDialog(getStage());
        if (f != null) {
            // Copiar para uma pasta local da aplicação (ex.: ./data/curriculos)
            try {
                Path destinoDir = Path.of("data", "curriculos");
                Files.createDirectories(destinoDir);
                Path destino = destinoDir.resolve(System.currentTimeMillis() + "_" + f.getName());
                Files.copy(f.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
                pdfPathField.setText(destino.toAbsolutePath().toString());
            } catch (IOException ex) {
                alertErro("Falha ao copiar arquivo:", ex.getMessage());
            }
        }
    }

    @FXML
    public void abrirPdf(ActionEvent e) {
        String path = pdfPathField.getText();
        if (path == null || path.isBlank()) { alertInfo("Selecione um PDF primeiro."); return; }
        File f = new File(path);
        if (!f.exists()) { alertErro("Arquivo não encontrado:", path); return; }
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(f);
            } else alertInfo("Abertura de arquivo não suportada neste ambiente.");
        } catch (IOException ex) {
            alertErro("Falha ao abrir PDF:", ex.getMessage());
        }
    }

    @FXML
    public void salvar(ActionEvent e) {
        if (!validar()) return;
        Curriculo c = new Curriculo();
        c.setNome(nomeField.getText().trim());
        c.setEmail(emailField.getText().trim());
        c.setTelefone(telefoneField.getText().trim());
        c.setCargoDesejado(cargoField.getText().trim());
        c.setEscolaridade(escolaridadeField.getText().trim());
        c.setLinkedin(linkedinField.getText().trim());
        c.setStatusProcesso(statusBox.getValue());
        c.setSkills(skillsArea.getText());
        c.setExperiencia(experienciaArea.getText());
        c.setCaminhoPdf(pdfPathField.getText());
        c.setDataCadastro(LocalDate.now());

        try {
            dao.inserir(c);
            alertInfo("Currículo salvo com sucesso! ID=" + c.getId());
            limpar(null);
        } catch (SQLException ex) {
            alertErro("Erro ao salvar currículo:", ex.getMessage());
        }
    }

    @FXML
    public void limpar(ActionEvent e) {
        nomeField.clear(); emailField.clear(); telefoneField.clear(); cargoField.clear();
        escolaridadeField.clear(); linkedinField.clear(); pdfPathField.clear();
        skillsArea.clear(); experienciaArea.clear(); statusBox.getSelectionModel().select("NOVO");
    }

    @FXML
    public void fechar(ActionEvent e) {
        getStage().close();
    }

    private boolean validar() {
        if (nomeField.getText().isBlank()) { alertInfo("Nome é obrigatório."); return false; }
        if (emailField.getText().isBlank()) { alertInfo("Email é obrigatório."); return false; }
        if (cargoField.getText().isBlank()) { alertInfo("Cargo desejado é obrigatório."); return false; }
        return true;
    }

    private Stage getStage() { return (Stage) nomeField.getScene().getWindow(); }

    private void alertInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.showAndWait();
    }

    private void alertErro(String header, String content) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(header);
        a.setContentText(content);
        a.showAndWait();
    }
}