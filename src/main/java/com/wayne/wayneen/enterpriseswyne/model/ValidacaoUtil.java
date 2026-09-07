package com.wayne.wayneen.enterpriseswyne.model;


import com.wayne.wayneen.enterpriseswyne.model.Funcionario;
import javafx.scene.control.Alert;

public class ValidacaoUtil {

    public static boolean emailValido(String email) {
        return Validador.emailValido(email);
    }

    public static boolean cpfValido(String cpf) {
        return Validador.cpfValido(cpf);
    }

    public static void mostrarAlerta(String titulo) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION); // ou WARNING, ERROR, etc.
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        String mensagem = null;
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }


    public static boolean validarCamposObrigatorios(Funcionario f) {
        StringBuilder erros = new StringBuilder();

        if (f.getNomeCompleto() == null || f.getNomeCompleto().isBlank())
            erros.append("• Nome completo é obrigatório.\n");

        if (f.getCpf() == null || f.getCpf().isBlank())
            erros.append("• CPF é obrigatório.\n");
        else if (!cpfValido(f.getCpf()))
            erros.append("• CPF inválido.\n");

        if (f.getEmail() == null || f.getEmail().isBlank())
            erros.append("• E-mail é obrigatório.\n");
        else if (!emailValido(f.getEmail()))
            erros.append("• E-mail inválido.\n");

        if (f.getCargo() == null || f.getCargo().isBlank())
            erros.append("• Cargo é obrigatório.\n");

        if (f.getDepartamento() == null || f.getDepartamento().isBlank())
            erros.append("• Departamento é obrigatório.\n");

        if (f.getDataAdmissao() == null)
            erros.append("• Data de admissão é obrigatória.\n");

        if (f.getDataNascimento() == null)
            erros.append("• Data de nascimento é obrigatória.\n");

        if (erros.length() > 0) {
            mostrarAlerta("Campos inválidos");
            return false;
        }

        return true;
    }
}
