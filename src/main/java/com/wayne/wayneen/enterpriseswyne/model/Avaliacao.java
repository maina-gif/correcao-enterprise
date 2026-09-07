package com.wayne.wayneen.enterpriseswyne.model;



import javafx.beans.property.*;

import java.time.LocalDate;

public class Avaliacao {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty funcionarioId = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> dataAvaliacao = new SimpleObjectProperty<>();
    private final IntegerProperty pontualidade = new SimpleIntegerProperty();
    private final IntegerProperty produtividade = new SimpleIntegerProperty();
    private final IntegerProperty trabalhoEquipe = new SimpleIntegerProperty();
    private final StringProperty observacoes = new SimpleStringProperty();

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public int getFuncionarioId() { return funcionarioId.get(); }
    public void setFuncionarioId(int funcionarioId) { this.funcionarioId.set(funcionarioId); }
    public IntegerProperty funcionarioIdProperty() { return funcionarioId; }

    public LocalDate getDataAvaliacao() { return dataAvaliacao.get(); }
    public void setDataAvaliacao(LocalDate dataAvaliacao) { this.dataAvaliacao.set(dataAvaliacao); }
    public ObjectProperty<LocalDate> dataAvaliacaoProperty() { return dataAvaliacao; }

    public int getPontualidade() { return pontualidade.get(); }
    public void setPontualidade(int pontualidade) { this.pontualidade.set(pontualidade); }
    public IntegerProperty pontualidadeProperty() { return pontualidade; }

    public int getProdutividade() { return produtividade.get(); }
    public void setProdutividade(int produtividade) { this.produtividade.set(produtividade); }
    public IntegerProperty produtividadeProperty() { return produtividade; }

    public int getTrabalhoEquipe() { return trabalhoEquipe.get(); }
    public void setTrabalhoEquipe(int trabalhoEquipe) { this.trabalhoEquipe.set(trabalhoEquipe); }
    public IntegerProperty trabalhoEquipeProperty() { return trabalhoEquipe; }

    public String getObservacoes() { return observacoes.get(); }
    public void setObservacoes(String observacoes) { this.observacoes.set(observacoes); }
    public StringProperty observacoesProperty() { return observacoes; }
}
