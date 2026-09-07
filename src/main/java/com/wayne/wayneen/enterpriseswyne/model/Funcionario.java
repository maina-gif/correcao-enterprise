package com.wayne.wayneen.enterpriseswyne.model;

import javafx.fxml.FXML;

import java.awt.event.ActionEvent;
import java.sql.Date;
import java.time.LocalDate;

public class Funcionario {

    private int id;
    private String nomeCompleto;
    private String cpf;
    private String cargo;
    private String departamento;
    private String email;

    private Date dataAdmissao;                 // JDBC
    private LocalDate dataNascimento;          // Lógica interna
    private LocalDate dataFerias;              // Lógica interna

    private String caminhoCurriculo;
    private String caminhoContrato;

    // Construtor vazio
    public Funcionario() {}

    // Construtor completo
    public Funcionario(int id, String nomeCompleto, String cpf, String cargo, String departamento,
                       String email, Date dataAdmissao, LocalDate dataNascimento,
                       LocalDate dataFerias, String caminhoCurriculo, String caminhoContrato) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.cargo = cargo;
        this.departamento = departamento;
        this.email = email;
        this.dataAdmissao = dataAdmissao;
        this.dataNascimento = dataNascimento;
        this.dataFerias = dataFerias;
        this.caminhoCurriculo = caminhoCurriculo;
        this.caminhoContrato = caminhoContrato;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(Date dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public LocalDate getDataFerias() {
        return dataFerias;
    }

    public void setDataFerias(LocalDate dataFerias) {
        this.dataFerias = dataFerias;
    }

    public String getCaminhoCurriculo() {
        return caminhoCurriculo;
    }

    public void setCaminhoCurriculo(String caminhoCurriculo) {
        this.caminhoCurriculo = caminhoCurriculo;
    }

    public String getCaminhoContrato() {
        return caminhoContrato;
    }

    public void setCaminhoContrato(String caminhoContrato) {
        this.caminhoContrato = caminhoContrato;
    }

    private String caminhoFoto;

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    @FXML
    private void selecionarFoto(ActionEvent event) {
        // ...
    }


}
