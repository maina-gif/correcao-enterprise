package com.wayne.wayneen.enterpriseswyne.model;

import com.wayne.wayneen.enterpriseswyne.DAO.LogDAO;

/**
 * Serviço central para registrar auditoria no sistema.
 * Deve ser usado sempre que ocorrer uma ação importante:
 * login, logout, cadastro, atualização, exclusão, exportação de relatórios, etc.
 */
public class AuditoriaService {

    /**
     * Registra ação vinculada ao usuário logado.
     *
     * @param acao Descrição da ação realizada
     */
    public static void registrarAcao(String acao) {
        Usuarios usuario = SessionManager.getUsuarioLogado();

        if (usuario != null) {
            String descricao = "[Usuário: " + nomeParaLog(usuario) + "] " + acao;
            LogDAO.registrar(descricao);
        } else {
            // fallback para ações de sistema ou erros
            LogDAO.registrar("[Sistema] " + acao);
        }
    }

    /**
     * Atalho específico para logins.
     */
    public static void registrarLogin(Usuarios usuario) {
        if (usuario != null) {
            SessionManager.setUsuarioLogado(usuario);
            LogDAO.registrar("[Login] Usuário " + nomeParaLog(usuario) + " autenticado");
        } else {
            LogDAO.registrar("[Login] Tentativa de login sem usuário (null)");
        }
    }

    /**
     * Atalho específico para logouts.
     */
    public static void registrarLogout() {
        Usuarios usuario = SessionManager.getUsuarioLogado();
        if (usuario != null) {
            LogDAO.registrar("[Logout] Usuário " + nomeParaLog(usuario) + " encerrou a sessão");
            SessionManager.encerrarSessao();
        } else {
            LogDAO.registrar("[Logout] Sessão já estava encerrada");
        }
    }

    /**
     * Atalho para registrar operações de CRUD.
     *
     * @param entidade Nome da entidade (Funcionario, Documento, etc)
     * @param operacao Tipo da operação (CADASTRO, ATUALIZAÇÃO, EXCLUSÃO)
     * @param detalhes Informações adicionais (ex: ID=10, Nome="João")
     */
    public static void registrarCrud(String entidade, String operacao, String detalhes) {
        String acao = "[CRUD][" + safe(entidade) + "][" + safe(operacao) + "] " + safe(detalhes);
        registrarAcao(acao);
    }

    // ===== Helpers =====

    /** Resolve um nome amigável para logs: nomeCompleto -> email -> id. */
    private static String nomeParaLog(Usuarios u) {
        if (u == null) return "desconhecido";
        String nome = u.getNomeCompleto();
        if (nome == null || nome.isBlank()) nome = u.getEmail();
        if (nome == null || nome.isBlank()) nome = "id=" + u.getId();
        return nome;
    }

    private static String safe(String s) {
        return (s == null) ? "" : s;
    }
}
