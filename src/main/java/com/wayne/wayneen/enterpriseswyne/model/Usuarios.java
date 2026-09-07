package com.wayne.wayneen.enterpriseswyne.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

public class Usuarios {

    private Long id;
    private String nomeCompleto;
    private String email;
    private boolean online;
    private Instant lastSeen;

    // ====== Construtores ======
    public Usuarios() {}

    public Usuarios(Long id, String nomeCompleto) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
    }

    public Usuarios(Long id, String nomeCompleto, String email, boolean online, Instant lastSeen) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.online = online;
        this.lastSeen = lastSeen;
    }

    // ====== Getters & Setters ======
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isOnline() { return online; }
    public void setOnline(boolean online) { this.online = online; }

    public Instant getLastSeen() { return lastSeen; }
    public void setLastSeen(Instant lastSeen) { this.lastSeen = lastSeen; }

    /** Sobrecarga conveniente: permite setar a partir de Timestamp. */
    public void setLastSeen(Timestamp ts) {
        this.lastSeen = (ts != null) ? ts.toInstant() : null;
    }

    /** Necessário para o DAO quando precisa de Timestamp. */
    public Timestamp getLastSeenTimestamp() {
        return (lastSeen != null) ? Timestamp.from(lastSeen) : null;
    }

    /** Nome amigável para exibição (fallback para email). */
    public String getDisplayName() {
        if (nomeCompleto != null && !nomeCompleto.isBlank()) return nomeCompleto;
        return (email != null && !email.isBlank()) ? email : ("id=" + id);
    }

    // ====== toString (útil p/ debug e ListView) ======
    @Override
    public String toString() {
        return getDisplayName();
    }

    // ====== equals/hashCode por id ======
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuarios)) return false;
        Usuarios that = (Usuarios) o;
        // se id for nulo, usa igualdade por referência
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return (id != null) ? id.hashCode() : System.identityHashCode(this);
    }
}
