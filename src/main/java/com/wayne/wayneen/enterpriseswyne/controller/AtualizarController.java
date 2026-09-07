package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.FuncionarioDAOMethods;
import com.wayne.wayneen.enterpriseswyne.model.Funcionario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class AtualizarController {

    private Funcionario funcionarioSelecionado;

    @FXML private TextField nomeField;
    @FXML private TextField cpfField;
    @FXML private TextField cargoField;
    @FXML private TextField departamentoField;
    @FXML private TextField emailField;
    @FXML private DatePicker dataAdmissaoPicker;

    /** Chamado pela listagem para preencher os dados do funcionário */
    public void carregarFuncionario(Funcionario funcionario) {
        this.funcionarioSelecionado = funcionario;
        if (funcionario == null) {
            mostrarAlerta("Aviso", "Funcionário não informado.", Alert.AlertType.WARNING);
            return;
        }

        nomeField.setText(nz(funcionario.getNomeCompleto()));
        cpfField.setText(nz(funcionario.getCpf()));
        cargoField.setText(nz(funcionario.getCargo()));
        departamentoField.setText(nz(funcionario.getDepartamento()));
        emailField.setText(nz(funcionario.getEmail()));

        // MODEL: java.sql.Date -> DatePicker (LocalDate)
        java.sql.Date d = funcionario.getDataAdmissao();
        dataAdmissaoPicker.setValue(d != null ? d.toLocalDate() : null);
    }

    @FXML
    private void atualizarFuncionario() {
        if (funcionarioSelecionado == null) {
            mostrarAlerta("Aviso", "Nenhum funcionário selecionado.", Alert.AlertType.WARNING);
            return;
        }

        try {
            funcionarioSelecionado.setNomeCompleto(nz(nomeField.getText()));
            funcionarioSelecionado.setCpf(nz(cpfField.getText()));
            funcionarioSelecionado.setCargo(nz(cargoField.getText()));
            funcionarioSelecionado.setDepartamento(nz(departamentoField.getText()));
            funcionarioSelecionado.setEmail(nz(emailField.getText()));

            // DatePicker (LocalDate) -> MODEL: java.sql.Date
            java.time.LocalDate ld = dataAdmissaoPicker.getValue();
            funcionarioSelecionado.setDataAdmissao(ld != null ? java.sql.Date.valueOf(ld) : null);

            FuncionarioDAOMethods.atualizar(funcionarioSelecionado);
            mostrarAlerta("Atualizado", "Funcionário atualizado com sucesso.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao atualizar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /* -------- utilidades -------- */

    private String nz(String s) { return (s == null) ? "" : s.trim(); }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
