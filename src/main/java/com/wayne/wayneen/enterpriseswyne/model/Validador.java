package com.wayne.wayneen.enterpriseswyne.model;

import java.util.regex.Pattern;

public class Validador {

    private static final Pattern REGEX_EMAIL =
            Pattern.compile("^[\\w\\.-]+@[\\w\\.-]+\\.[a-z]{2,}$", Pattern.CASE_INSENSITIVE);

    // Valida e-mail e retorna true/false
    public static boolean emailValido(String email) {
        return email != null && REGEX_EMAIL.matcher(email).matches();
    }

    // Valida CPF e retorna true/false
    public static boolean cpfValido(String cpf) {
        if (cpf == null || cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int soma = 0, peso = 10;

            for (int i = 0; i < 9; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * peso--;
            }

            int primeiroDigito = 11 - (soma % 11);
            if (primeiroDigito > 9) primeiroDigito = 0;
            if (primeiroDigito != Character.getNumericValue(cpf.charAt(9))) return false;

            soma = 0;
            peso = 11;

            for (int i = 0; i < 10; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * peso--;
            }

            int segundoDigito = 11 - (soma % 11);
            if (segundoDigito > 9) segundoDigito = 0;
            return segundoDigito == Character.getNumericValue(cpf.charAt(10));

        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Gera mensagem de erro caso o CPF seja inválido
    public static String validarCpfComMensagem(String cpf) {
        if (cpfValido(cpf)) return null;
        return "CPF inválido. Certifique-se de digitar 11 dígitos válidos.";
    }

    // Gera mensagem de erro caso o e-mail seja inválido
    public static String validarEmailComMensagem(String email) {
        if (emailValido(email)) return null;
        return "E-mail inválido. Digite um endereço de e-mail válido.";
    }
}

