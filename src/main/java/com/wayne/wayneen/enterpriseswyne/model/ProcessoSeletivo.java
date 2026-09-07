package com.wayne.wayneen.enterpriseswyne.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class ProcessoSeletivo {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty titulo = new SimpleStringProperty();
    private final StringProperty descricao = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> dataInicio = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> dataFim = new SimpleObjectProperty<>();

    public int getId() { return id.get(); }
    public void setId(int v) { id.set(v); }
    public IntegerProperty idProperty() { return id; }

    public String getTitulo() { return titulo.get(); }
    public void setTitulo(String v) { titulo.set(v); }
    public StringProperty tituloProperty() { return titulo; }

    public String getDescricao() { return descricao.get(); }
    public void setDescricao(String v) { descricao.set(v); }
    public StringProperty descricaoProperty() { return descricao; }

    public LocalDate getDataInicio() { return dataInicio.get(); }
    public void setDataInicio(LocalDate v) { dataInicio.set(v); }
    public ObjectProperty<LocalDate> dataInicioProperty() { return dataInicio; }

    public LocalDate getDataFim() { return dataFim.get(); }
    public void setDataFim(LocalDate v) { dataFim.set(v); }
    public ObjectProperty<LocalDate> dataFimProperty() { return dataFim; }
}
