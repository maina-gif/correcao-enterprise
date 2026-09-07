package com.wayne.wayneen.enterpriseswyne.model;



import java.time.LocalDate;

public class Curriculo {
    private Integer id;
    private String nome;
    private String email;
    private String telefone;
    private String cargoDesejado;
    private String skills;
    private String experiencia;
    private String escolaridade;
    private String linkedin;
    private String statusProcesso; // NOVO, EM_ANALISE, ENTREVISTA, APROVADO, REPROVADO, RESERVA
    private String caminhoPdf;     // caminho no disco
    private LocalDate dataCadastro;

    public Curriculo() {}

    public Curriculo(Integer id, String nome, String email, String telefone, String cargoDesejado,
                     String skills, String experiencia, String escolaridade, String linkedin,
                     String statusProcesso, String caminhoPdf, LocalDate dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cargoDesejado = cargoDesejado;
        this.skills = skills;
        this.experiencia = experiencia;
        this.escolaridade = escolaridade;
        this.linkedin = linkedin;
        this.statusProcesso = statusProcesso;
        this.caminhoPdf = caminhoPdf;
        this.dataCadastro = dataCadastro;
    }

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getCargoDesejado() { return cargoDesejado; }
    public void setCargoDesejado(String cargoDesejado) { this.cargoDesejado = cargoDesejado; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getExperiencia() { return experiencia; }
    public void setExperiencia(String experiencia) { this.experiencia = experiencia; }

    public String getEscolaridade() { return escolaridade; }
    public void setEscolaridade(String escolaridade) { this.escolaridade = escolaridade; }

    public String getLinkedin() { return linkedin; }
    public void setLinkedin(String linkedin) { this.linkedin = linkedin; }

    public String getStatusProcesso() { return statusProcesso; }
    public void setStatusProcesso(String statusProcesso) { this.statusProcesso = statusProcesso; }

    public String getCaminhoPdf() { return caminhoPdf; }
    public void setCaminhoPdf(String caminhoPdf) { this.caminhoPdf = caminhoPdf; }

    public LocalDate getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDate dataCadastro) { this.dataCadastro = dataCadastro; }
}