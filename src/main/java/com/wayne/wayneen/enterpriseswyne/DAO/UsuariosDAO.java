package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.Usuarios;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UsuariosDAO {

    // CRUD básico
    List<Usuarios> listarTodos() throws SQLException;
    Optional<Usuarios> buscarPorId(long id) throws SQLException;
    List<Usuarios> buscarPorTermo(String termo) throws SQLException;
    Optional<Usuarios> buscarPorEmail(String email) throws SQLException;

    long salvar(Usuarios u) throws SQLException;      // retorna ID gerado
    boolean atualizar(Usuarios u) throws SQLException;
    boolean remover(long id) throws SQLException;

    // utilidades
    boolean setOnline(long id, boolean online) throws SQLException;
    boolean tocarLastSeen(long id) throws SQLException; // atualiza last_seen = CURRENT_TIMESTAMP

    List<Usuarios> listarUsuariosAtivosOuPorNome(String filtro);
}
