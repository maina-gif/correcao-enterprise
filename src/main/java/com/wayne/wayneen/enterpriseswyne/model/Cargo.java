package com.wayne.wayneen.enterpriseswyne.model;

public class Cargo {
    private int id;
    private String nome;
    private double salarioBase;
    private String nivel;
    private String criteriosPromocao; // Campo faltante

    public Cargo() {}

    public Cargo(int id, String nome, double salarioBase, String nivel, String criteriosPromocao) {
        this.id = id;
        this.nome = nome;
        this.salarioBase = salarioBase;
        this.nivel = nivel;
        this.criteriosPromocao = criteriosPromocao;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getSalarioBase() {
        return salarioBase;
    }
    public void setSalarioBase(double salarioBase) {
        this.salarioBase = salarioBase;
    }

    public String getNivel() {
        return nivel;
    }
    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getCriteriosPromocao() {
        return criteriosPromocao;
    }
    public void setCriteriosPromocao(String criteriosPromocao) {
        this.criteriosPromocao = criteriosPromocao;
    }

    // Para compatibilidade com o controller (padrão alternativo de nome)
    public String getNomeCargo() {
        return nome;
    }
    public void setNomeCargo(String nome) {
        this.nome = nome;
    }
}
