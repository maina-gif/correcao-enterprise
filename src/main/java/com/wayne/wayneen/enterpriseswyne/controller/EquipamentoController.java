package com.wayne.wayneen.enterpriseswyne;

import com.wayne.wayneen.enterpriseswyne.DAO.EquipamentoDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EquipamentoController {

    @FXML private TextField campoTipo;
    @FXML private TextField campoNumeroSerie;
    @FXML private TextField campoResponsavel;
    @FXML private TextField campoStatus;
    @FXML private DatePicker campoDataAquisicao;

    @FXML private TableView<Equipamento> tabelaEquipamentos;
    @FXML private TableColumn<Equipamento, Integer> colId;
    @FXML private TableColumn<Equipamento, String> colTipo;
    @FXML private TableColumn<Equipamento, String> colNumeroSerie;
    @FXML private TableColumn<Equipamento, String> colResponsavel;
    @FXML private TableColumn<Equipamento, String> colStatus;
    @FXML private TableColumn<Equipamento, String> colDataAquisicao;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getId()));
        colTipo.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getTipo()));
        colNumeroSerie.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getNumeroSerie()));
        colResponsavel.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getFuncionarioResponsavel()));
        colStatus.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getStatus()));

        colDataAquisicao.setCellValueFactory(equipamento -> {
            LocalDate data = equipamento.getValue().getDataAquisicao();
            return new ReadOnlyStringWrapper(data != null ? data.format(formatter) : "");
        });

        atualizarTabela();
    }

    @FXML
    private void salvarEquipamento() {
        try {
            String tipo = campoTipo.getText();
            String numeroSerie = campoNumeroSerie.getText();
            String responsavel = campoResponsavel.getText();
            String status = campoStatus.getText();
            LocalDate dataAquisicao = campoDataAquisicao.getValue();

            // Validações
            if (tipo.isEmpty() || numeroSerie.isEmpty() || responsavel.isEmpty() || status.isEmpty() || dataAquisicao == null) {
                mostrarAlerta("⚠️ Todos os campos devem ser preenchidos, incluindo a data de aquisição.");
                return;
            }

            Equipamento equipamento = new Equipamento(
                    tipo,
                    numeroSerie,
                    responsavel,
                    status,
                    Date.valueOf(dataAquisicao).toLocalDate()
            );

            EquipamentoDAO.salvar(equipamento);
            mostrarAlerta("✅ Equipamento salvo com sucesso!");
            atualizarTabela();
            limparCampos();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("❌ Erro ao salvar equipamento.");
        }
    }

    @FXML
    private void atualizarEquipamento() {
        Equipamento selecionado = tabelaEquipamentos.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                selecionado.setTipo(campoTipo.getText());
                selecionado.setNumeroSerie(campoNumeroSerie.getText());
                selecionado.setFuncionarioResponsavel(campoResponsavel.getText());
                selecionado.setStatus(campoStatus.getText());
                LocalDate data = campoDataAquisicao.getValue();

                if (data == null) {
                    mostrarAlerta("⚠️ Selecione uma data de aquisição válida.");
                    return;
                }

                selecionado.setDataAquisicao(data);

                EquipamentoDAO.atualizar(selecionado);
                mostrarAlerta("✅ Equipamento atualizado com sucesso!");
                atualizarTabela();
                limparCampos();

            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("❌ Erro ao atualizar equipamento.");
            }

        } else {
            mostrarAlerta("⚠️ Selecione um equipamento para atualizar.");
        }
    }

    private void atualizarTabela() {
        List<Equipamento> equipamentos = EquipamentoDAO.listarTodos();
        tabelaEquipamentos.setItems(FXCollections.observableArrayList(equipamentos));
    }

    private void listarEquipamentos() {
        atualizarTabela(); // mantém compatibilidade com outros métodos que chamam esse
    }

    @FXML
    private void limparCampos() {
        campoTipo.clear();
        campoNumeroSerie.clear();
        campoResponsavel.clear();
        campoStatus.clear();
        campoDataAquisicao.setValue(null);
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
