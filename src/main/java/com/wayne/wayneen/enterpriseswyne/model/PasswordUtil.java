package com.wayne.wayneen.enterpriseswyne.model;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilitario de hash de senha usando bcrypt (jBCrypt).
 *
 * Nunca compare senha em texto puro com o que esta no banco - sempre
 * use hash() ao cadastrar/trocar senha, e verifica() ao fazer login.
 */
public class PasswordUtil {

    private PasswordUtil() {
        // classe utilitaria, nao instanciar
    }

    /**
     * Gera o hash de uma senha em texto puro, pronto para salvar na
     * coluna `senha` da tabela `usuarios`.
     */
    public static String hash(String senhaTextoPuro) {
        if (senhaTextoPuro == null || senhaTextoPuro.isBlank()) {
            throw new IllegalArgumentException("Senha nao pode ser vazia.");
        }
        return BCrypt.hashpw(senhaTextoPuro, BCrypt.gensalt());
    }

    /**
     * Verifica se a senha digitada bate com o hash armazenado no banco.
     * Retorna false (nunca lanca excecao) se o hash estiver ausente,
     * vazio ou em formato invalido - assim um valor corrompido no banco
     * nunca autentica ninguem por engano.
     */
    public static boolean verifica(String senhaDigitada, String hashArmazenado) {
        if (senhaDigitada == null || hashArmazenado == null || hashArmazenado.isBlank()) {
            return false;
        }
        try {
            return BCrypt.checkpw(senhaDigitada, hashArmazenado);
        } catch (IllegalArgumentException formatoInvalido) {
            // hashArmazenado nao esta em formato bcrypt (ex: ainda esta em texto puro
            // porque o usuario nao foi migrado) - trata como senha incorreta.
            return false;
        }
    }
}
