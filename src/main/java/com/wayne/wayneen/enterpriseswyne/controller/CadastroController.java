package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.FuncionarioDAOMethods;
import com.wayne.wayneen.enterpriseswyne.model.Funcionario;
import com.wayne.wayneen.enterpriseswyne.model.ValidacaoUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.util.StringConverter;

import java.io.File;
import java.nio.file.Files;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CadastroController {

    @FXML private TextField nomeField;
    @FXML private TextField cpfField;
    @FXML private TextField cargoField;
    @FXML private TextField departamentoField;
    @FXML private TextField emailField;
    @FXML private DatePicker dataAdmissaoPicker;
    @FXML private DatePicker dataNascimentoPicker;
    @FXML private ImageView fotoImageView;

    private File arquivoCurriculoSelecionado;
    private File arquivoContratoSelecionado;
    private File arquivoFotoSelecionado;

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Corrige um bug do JavaFX: o DatePicker padrão usa o formato de data
     * "cru" do sistema operacional para interpretar o texto digitado, e
     * lança uma exceção não tratada (trava o commit do valor) se o texto
     * não bater exatamente com esse formato no momento em que o campo
     * perde o foco - por exemplo, ao clicar em "Salvar" logo depois de
     * digitar a data.
     *
     * Aqui fixamos o formato em dd/MM/yyyy (padrão brasileiro) e,
     * principalmente, tornamos o parser tolerante: se o texto não for
     * uma data válida, ele simplesmente retorna null (campo fica vazio)
     * em vez de lançar exceção e travar o salvamento.
     */
    @FXML
    private void initialize() {
        aplicarConversorDeData(dataAdmissaoPicker);
        aplicarConversorDeData(dataNascimentoPicker);
    }

    private void aplicarConversorDeData(DatePicker datePicker) {
        if (datePicker == null) return;

        datePicker.setPromptText("dd/mm/aaaa");
        datePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate data) {
                return (data != null) ? FORMATO_DATA.format(data) : "";
            }

            @Override
            public LocalDate fromString(String texto) {
                if (texto == null || texto.isBlank()) {
                    return null;
                }
                try {
                    return LocalDate.parse(texto.trim(), FORMATO_DATA);
                } catch (DateTimeParseException e) {
                    // Texto digitado não é uma data válida no formato dd/MM/yyyy.
                    // Em vez de lançar exceção (que trava o commit do JavaFX),
                    // simplesmente ignora o texto e mantém o campo vazio.
                    return null;
                }
            }
        });
    }

    private Stage getCurrentStage() {
        if (nomeField != null && nomeField.getScene() != null) {
            return (Stage) nomeField.getScene().getWindow();
        }
        return null;
    }

    @FXML
    private void salvarFuncionario() {
        try {
            Funcionario funcionario = new Funcionario();

            funcionario.setNomeCompleto(safeText(nomeField));
            funcionario.setCpf(safeText(cpfField));
            funcionario.setCargo(safeText(cargoField));
            funcionario.setDepartamento(safeText(departamentoField));
            funcionario.setEmail(safeText(emailField));

            // Padronizado: usar LocalDate no model; DAO converte para java.sql.Date
            LocalDate adm  = (dataAdmissaoPicker   != null) ? dataAdmissaoPicker.getValue()   : null;
            LocalDate nasc = (dataNascimentoPicker != null) ? dataNascimentoPicker.getValue() : null;
            funcionario.setDataAdmissao(Date.valueOf(adm));
            funcionario.setDataNascimento(nasc);

            funcionario.setCaminhoCurriculo(arquivoCurriculoSelecionado != null ? arquivoCurriculoSelecionado.getAbsolutePath() : null);
            funcionario.setCaminhoContrato(arquivoContratoSelecionado != null ? arquivoContratoSelecionado.getAbsolutePath() : null);

            if (arquivoFotoSelecionado != null) {
                funcionario.setCaminhoFoto(arquivoFotoSelecionado.getAbsolutePath());
            }

            if (!ValidacaoUtil.validarCamposObrigatorios(funcionario)) {
                mostrarAlerta("Atenção", "Preencha os campos obrigatórios corretamente.", Alert.AlertType.WARNING);
                return;
            }

            FuncionarioDAOMethods.salvar(funcionario);
            mostrarAlerta("Sucesso", "Funcionário cadastrado com sucesso.", Alert.AlertType.INFORMATION);
            limparCampos();

        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao salvar funcionário: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void uploadCurriculo() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Selecionar Currículo em PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF (*.pdf)", "*.pdf"));

            Stage stage = getCurrentStage();
            File arquivo = fileChooser.showOpenDialog(stage);
            if (arquivo != null) {
                if (!arquivo.exists() || !Files.isReadable(arquivo.toPath())) {
                    mostrarAlerta("Erro", "Arquivo inválido ou sem permissão de leitura.", Alert.AlertType.ERROR);
                    return;
                }
                this.arquivoCurriculoSelecionado = arquivo;
                mostrarAlerta("Currículo", "Arquivo selecionado com sucesso!", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            mostrarAlerta("Erro", "Falha ao selecionar currículo: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void uploadContrato() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Selecionar Contrato em PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF (*.pdf)", "*.pdf"));

            Stage stage = getCurrentStage();
            File arquivo = fileChooser.showOpenDialog(stage);
            if (arquivo != null) {
                if (!arquivo.exists() || !Files.isReadable(arquivo.toPath())) {
                    mostrarAlerta("Erro", "Arquivo inválido ou sem permissão de leitura.", Alert.AlertType.ERROR);
                    return;
                }
                this.arquivoContratoSelecionado = arquivo;
                mostrarAlerta("Contrato", "Arquivo selecionado com sucesso!", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            mostrarAlerta("Erro", "Falha ao selecionar contrato: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void selecionarFoto(javafx.event.ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Selecionar Foto do Funcionário");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Imagens", "*.jpg", "*.jpeg", "*.png")
            );

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            File arquivo = fileChooser.showOpenDialog(stage);

            if (arquivo != null) {
                if (!arquivo.exists() || !Files.isReadable(arquivo.toPath())) {
                    mostrarAlerta("Erro", "Arquivo de imagem inválido ou sem permissão de leitura.", Alert.AlertType.ERROR);
                    return;
                }
                arquivoFotoSelecionado = arquivo;
                try {
                    Image imagem = new Image(arquivo.toURI().toString(), true);
                    fotoImageView.setImage(imagem);
                } catch (Exception e) {
                    mostrarAlerta("Erro", "Erro ao carregar imagem: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        } catch (Exception e) {
            mostrarAlerta("Erro", "Falha ao selecionar foto: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void fazerLogout(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wayne/wayneen/enterpriseswyne/login.fxml"));
            Scene loginScene = new Scene(loader.load());

            Stage novoStage = new Stage();
            novoStage.setTitle("Login");
            novoStage.setScene(loginScene);
            novoStage.show();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao fazer logout: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void limparCampos() {
        if (nomeField != null) nomeField.clear();
        if (cpfField != null) cpfField.clear();
        if (cargoField != null) cargoField.clear();
        if (departamentoField != null) departamentoField.clear();
        if (emailField != null) emailField.clear();
        if (dataAdmissaoPicker != null) dataAdmissaoPicker.setValue(null);
        if (dataNascimentoPicker != null) dataNascimentoPicker.setValue(null);
        if (fotoImageView != null) fotoImageView.setImage(null);

        arquivoCurriculoSelecionado = null;
        arquivoContratoSelecionado = null;
        arquivoFotoSelecionado = null;
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    private String safeText(TextField tf) {
        return (tf == null || tf.getText() == null) ? "" : tf.getText().trim();
    }
}
