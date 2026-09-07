package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.EmpresaInfoDAO;
import com.wayne.wayneen.enterpriseswyne.model.EmpresaInfo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;

public class SobreDialogController {
    @FXML private ImageView logoView;
    @FXML private Label appLabel;
    @FXML private Label empresaLabel;
    @FXML private Label contatoLabel;
    @FXML private Label siteLabel;
    @FXML private Label versaoLabel;
    @FXML private Button fecharButton;

    private final EmpresaInfoDAO dao = new EmpresaInfoDAO();

    @FXML
    public void initialize() {
        try {
            EmpresaInfo e = dao.buscar();
            if (e != null) {
                appLabel.setText("Wayne Enterprises — Sistema de Gestão");
                empresaLabel.setText(nv(e.getNome()) + (e.getCnpj()!=null? " • CNPJ: "+e.getCnpj():""));
                contatoLabel.setText("Contato: " + nv(e.getTelefone()) + (e.getEmail()!=null? " • "+e.getEmail():""));
                siteLabel.setText("Site: " + nv(e.getSite()));
                versaoLabel.setText("Versão: 1.0.0"); // ajuste conforme seu build
                if (e.getLogoPath()!=null && new File(e.getLogoPath()).exists()) {
                    try (FileInputStream fis = new FileInputStream(e.getLogoPath())) {
                        logoView.setImage(new Image(fis));
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    @FXML public void fechar() { ((Stage) fecharButton.getScene().getWindow()).close(); }
    private String nv(String s) { return s==null?"":s; }
}