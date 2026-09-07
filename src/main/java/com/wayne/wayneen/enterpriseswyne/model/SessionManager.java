package com.wayne.wayneen.enterpriseswyne.model;

public class SessionManager {
    private static Usuarios usuarioLogado;

    /**
     * Define o usuário logado na sessão.
     */
    public static void setUsuarioLogado(Usuarios usuario) {
        usuarioLogado = usuario;
    }

    /**
     * Retorna o usuário logado atual.
     */
    public static Usuarios getUsuarioLogado() {
        return usuarioLogado;
    }

    /**
     * Verifica se há um usuário logado.
     */
    public static boolean isUsuarioLogado() {
        return usuarioLogado != null;
    }

    /**
     * Encerra a sessão do usuário.
     */
    public static void encerrarSessao() {
        usuarioLogado = null;
    }
}
