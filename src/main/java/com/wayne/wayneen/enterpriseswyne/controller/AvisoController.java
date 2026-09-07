package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.model.Aviso;
import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDate;

public class AvisoController {

    @FXML private TextField tituloField;
    @FXML private TextArea descricaoArea;
    @FXML private DatePicker dataAvisoPicker;
    @FXML private ComboBox<String> tipoComboBox;

    @FXML private TableView<Aviso> tabelaAvisos;
    @FXML private TableColumn<Aviso, Integer> colId;
    @FXML private TableColumn<Aviso, String> colTitulo;
    @FXML private TableColumn<Aviso, String> colDescricao;
    @FXML private TableColumn<Aviso, LocalDate> colData;
    @FXML private TableColumn<Aviso, String> colTipo;

    private final ObservableList<Aviso> listaAvisos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        tipoComboBox.setItems(FXCollections.observableArrayList("Aniversário", "Férias", "Comunicado Interno"));

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        tabelaAvisos.setItems(listaAvisos);
        carregarAvisos();
    }


    @FXML
    private void adicionarAviso() {
        String titulo = tituloField.getText();
        String descricao = descricaoArea.getText();
        LocalDate data = dataAvisoPicker.getValue();
        String tipo = tipoComboBox.getValue();

        if (titulo.isEmpty() || descricao.isEmpty() || data == null || tipo == null) {
            mostrarAlerta("⚠️ Todos os campos devem ser preenchidos.");
            return;
        }

        String sql = "INSERT INTO avisos (titulo, descricao, data, tipo) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, titulo);
            stmt.setString(2, descricao);
            stmt.setDate(3, Date.valueOf(data));
            stmt.setString(4, tipo);
            stmt.executeUpdate();

            mostrarAlerta("✅ Aviso adicionado com sucesso!");
            limparCampos();
            carregarAvisos();

        } catch (SQLException e) {
            mostrarAlerta("❌ Erro ao adicionar aviso:\n" + e.getMessage());
        }
    }

    @FXML
    private void carregarAvisos() {
        listaAvisos.clear();
        String sql = "SELECT * FROM avisos ORDER BY data DESC";

        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Aviso aviso = new Aviso(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("descricao"),
                        rs.getDate("data").toLocalDate(),
                        rs.getString("tipo")
                );
                listaAvisos.add(aviso);
            }

        } catch (SQLException e) {
            mostrarAlerta("❌ Erro ao carregar avisos:\n" + e.getMessage());
        }
    }

    private void limparCampos() {
        tituloField.clear();
        descricaoArea.clear();
        dataAvisoPicker.setValue(null);
        tipoComboBox.setValue(null);
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
