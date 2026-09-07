package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.EstoqueItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * Controller simples para gerenciar itens de estoque.
 */
public class EstoqueController {

    @FXML
    private TableView<EstoqueItem> tabelaEstoque;

    @FXML
    private TableColumn<EstoqueItem, String> colNome;

    @FXML
    private TableColumn<EstoqueItem, Number> colQuantidade;

    @FXML
    private TableColumn<EstoqueItem, String> colDescricao;

    @FXML
    private TextField campoNome;

    @FXML
    private TextField campoQuantidade;

    @FXML
    private TextField campoDescricao;

    private final ObservableList<EstoqueItem> itens = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        tabelaEstoque.setItems(itens);
    }

    @FXML
    private void adicionarItem(ActionEvent event) {
        String nome = campoNome.getText();
        int quantidade = Integer.parseInt(campoQuantidade.getText());
        String descricao = campoDescricao.getText();
        EstoqueItem item = new EstoqueItem(System.currentTimeMillis(), nome, quantidade, descricao);
        itens.add(item);
        limparCampos();
    }

    @FXML
    private void removerItem(ActionEvent event) {
        EstoqueItem selecionado = tabelaEstoque.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            itens.remove(selecionado);
        }
    }

    private void limparCampos() {
        campoNome.clear();
        campoQuantidade.clear();
        campoDescricao.clear();
    }
}