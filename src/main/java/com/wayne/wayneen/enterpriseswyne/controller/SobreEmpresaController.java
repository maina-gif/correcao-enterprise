package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.EmpresaInfoDAO;
import com.wayne.wayneen.enterpriseswyne.model.EmpresaInfo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.HTMLEditor;   // requer javafx-web
import javafx.scene.web.WebView;      // requer javafx-web
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

public class SobreEmpresaController {

    @FXML private TextField nomeField;
    @FXML private TextField cnpjField;
    @FXML private TextField telefoneField;
    @FXML private TextField emailField;
    @FXML private TextField siteField;
    @FXML private TextArea enderecoArea;
    @FXML private TextArea redesArea;

    @FXML private TextArea missaoArea;
    @FXML private TextArea visaoArea;
    @FXML private TextArea valoresArea;

    @FXML private ImageView logoView;
    @FXML private HTMLEditor htmlEditor;   // precisa de requires javafx.web no module-info
    @FXML private WebView webPreview;      // precisa de requires javafx.web no module-info
    @FXML private Label atualizadoLabel;

    private final EmpresaInfoDAO dao = new EmpresaInfoDAO();
    private EmpresaInfo atual;

    @FXML
    public void initialize() {
        try {
            dao.inserirPadraoSeVazio();
            carregar();
        } catch (SQLException e) {
            alertErro("Banco de dados", e.getMessage());
        }

        if (htmlEditor == null || webPreview == null) {
            System.err.println("❌ FXML não injetou htmlEditor ou webPreview. Verifique os fx:id no FXML.");
            return;
        }

        // Garante que o WebEngine já está pronto
        Platform.runLater(() -> {
            String inicial = htmlEditor.getHtmlText();
            webPreview.getEngine().loadContent(inicial == null ? "" : inicial);

            // ✅ Listener correto: htmlTextProperty (não styleProperty)
            htmlEditor.styleProperty().addListener((obs, oldV, newV) -> {
                webPreview.getEngine().loadContent(newV == null ? "" : newV);
            });
        });
    }

    private void carregar() throws SQLException {
        atual = dao.buscar();
        if (atual == null) return;

        nomeField.setText(nv(atual.getNome()));
        cnpjField.setText(nv(atual.getCnpj()));
        telefoneField.setText(nv(atual.getTelefone()));
        emailField.setText(nv(atual.getEmail()));
        siteField.setText(nv(atual.getSite()));
        enderecoArea.setText(nv(atual.getEndereco()));
        redesArea.setText(nv(atual.getRedes()));
        missaoArea.setText(nv(atual.getMissao()));
        visaoArea.setText(nv(atual.getVisao()));
        valoresArea.setText(nv(atual.getValores()));

        if (htmlEditor != null) {
            htmlEditor.setHtmlText(nv(atual.getDescricaoHtml()));
        }

        atualizadoLabel.setText(atual.getDataAtualizacao() != null ? "Atualizado: " + atual.getDataAtualizacao() : "");

        carregarLogo(atual.getLogoPath());
    }

    private void carregarLogo(String path) {
        try {
            if (path != null && !path.isBlank() && new File(path).exists()) {
                try (FileInputStream fis = new FileInputStream(path)) {
                    logoView.setImage(new Image(fis));
                }
            } else {
                logoView.setImage(null);
            }
        } catch (IOException e) {
            alertErro("Logo", e.getMessage());
        }
    }

    @FXML
    public void salvar() {
        if (atual == null) atual = new EmpresaInfo();
        atual.setId(1);
        atual.setNome(nv(nomeField.getText()));
        atual.setCnpj(nv(cnpjField.getText()));
        atual.setTelefone(nv(telefoneField.getText()));
        atual.setEmail(nv(emailField.getText()));
        atual.setSite(nv(siteField.getText()));
        atual.setEndereco(nv(enderecoArea.getText()));
        atual.setRedes(nv(redesArea.getText()));
        atual.setMissao(nv(missaoArea.getText()));
        atual.setVisao(nv(visaoArea.getText()));
        atual.setValores(nv(valoresArea.getText()));
        atual.setDescricaoHtml(htmlEditor != null ? htmlEditor.getHtmlText() : "");

        try {
            dao.salvar(atual);
            alertInfo("Informações salvas com sucesso.");
            carregar();
            if (webPreview != null && htmlEditor != null) {
                webPreview.getEngine().loadContent(nv(htmlEditor.getHtmlText()));
            }
        } catch (SQLException e) {
            alertErro("Salvar", e.getMessage());
        }
    }

    @FXML
    public void recarregar() {
        try {
            carregar();
            if (webPreview != null && htmlEditor != null) {
                webPreview.getEngine().loadContent(nv(htmlEditor.getHtmlText()));
            }
        } catch (SQLException e) {
            alertErro("Recarregar", e.getMessage());
        }
    }

    @FXML
    public void fechar() {
        Stage st = getStage();
        if (st != null) st.close();
    }

    @FXML
    public void escolherLogo() {
        Stage st = getStage();
        if (st == null) {
            alertErro("Logo", "Janela ainda não está disponível.");
            return;
        }

        FileChooser fc = new FileChooser();
        fc.setTitle("Selecionar Logo (PNG/JPG)");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg"));
        File f = fc.showOpenDialog(st);
        if (f == null) return;

        try {
            Path dir = Path.of("data", "empresa");
            Files.createDirectories(dir);
            Path destino = dir.resolve("logo_" + System.currentTimeMillis() + getExt(f.getName()));
            Files.copy(f.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);

            if (atual == null) atual = new EmpresaInfo();
            atual.setLogoPath(destino.toAbsolutePath().toString());
            carregarLogo(atual.getLogoPath());
        } catch (IOException e) {
            alertErro("Logo", e.getMessage());
        }
    }

    @FXML
    public void abrirLogo() {
        if (atual == null || atual.getLogoPath() == null || atual.getLogoPath().isBlank()) {
            alertInfo("Sem logo cadastrada.");
            return;
        }
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(atual.getLogoPath()));
            } else {
                alertErro("Abrir logo", "Desktop não suportado neste ambiente.");
            }
        } catch (IOException e) {
            alertErro("Abrir logo", e.getMessage());
        }
    }

    @FXML
    public void removerLogo() {
        if (atual == null) return;
        atual.setLogoPath(null);
        logoView.setImage(null);
    }

    private String nv(String s) { return s == null ? "" : s; }

    private String getExt(String name) {
        int i = name.lastIndexOf('.');
        return i >= 0 ? name.substring(i) : "";
    }

    private Stage getStage() {
        return (nomeField != null && nomeField.getScene() != null)
                ? (Stage) nomeField.getScene().getWindow()
                : null;
    }

    private void alertInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }

    private void alertErro(String h, String c) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(h);
        a.setContentText(c);
        a.showAndWait();
    }
}
