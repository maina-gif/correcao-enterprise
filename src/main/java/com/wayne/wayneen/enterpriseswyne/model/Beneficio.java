package com.wayne.wayneen.enterpriseswyne.model;

public class Beneficio {
    private int id;
    private String tipo;
    private double valor;
    private String status;

    public Beneficio() {}

    public Beneficio(int id, String tipo, double valor, String status) {
        this.id = id;
        this.tipo = tipo;
        this.valor = valor;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
