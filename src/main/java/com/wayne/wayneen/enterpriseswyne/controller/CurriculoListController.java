package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.CurriculoDAO;
import com.wayne.wayneen.enterpriseswyne.model.Curriculo;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CurriculoListController {

    @FXML private TextField buscaField;
    @FXML private TextField cargoField;
    @FXML private ComboBox<String> statusBox;
    @FXML private DatePicker dePicker;
    @FXML private DatePicker atePicker;

    @FXML private TableView<Curriculo> tabela;
    @FXML private TableColumn<Curriculo, Number> colId;
    @FXML private TableColumn<Curriculo, String> colNome;
    @FXML private TableColumn<Curriculo, String> colCargo;
    @FXML private TableColumn<Curriculo, String> colStatus;
    @FXML private TableColumn<Curriculo, String> colEmail;
    @FXML private TableColumn<Curriculo, String> colData;
    @FXML private TableColumn<Curriculo, Curriculo> colAcoes;

    private final CurriculoDAO dao = new CurriculoDAO();
    private final ObservableList<Curriculo> dados = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        statusBox.getItems().setAll("TODOS","NOVO","EM_ANALISE","ENTREVISTA","APROVADO","REPROVADO","RESERVA");
        statusBox.getSelectionModel().select("TODOS");

        colId.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getId()));
        colNome.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getNome()));
        colCargo.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getCargoDesejado()));
        colStatus.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getStatusProcesso()));
        colEmail.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getEmail()));
        colData.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getDataCadastro() != null ? c.getValue().getDataCadastro().toString() : ""));

        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button btnAbrir = new Button("Abrir PDF");
            private final Button btnEditar = new Button("Editar");
            private final Button btnExcluir = new Button("Excluir");
            private final HBox box = new HBox(6, btnAbrir, btnEditar, btnExcluir);
            {
                btnAbrir.setOnAction(e -> abrirPdf(getItem()));
                btnEditar.setOnAction(e -> editar(getItem()));
                btnExcluir.setOnAction(e -> excluir(getItem()));
            }
            @Override protected void updateItem(Curriculo c, boolean empty) {
                super.updateItem(c, empty);
                if (empty || c == null) setGraphic(null); else setGraphic(box);
            }
        });

        tabela.setItems(dados);
        recarregar();
    }

    private void recarregar() {
        try {
            dados.setAll(dao.listarTodos());
        } catch (SQLException ex) {
            alertErro("Erro ao listar currículos:", ex.getMessage());
        }
    }

    @FXML
    public void aplicarFiltro() {
        String cargo = cargoField.getText();
        String status = statusBox.getValue();
        LocalDate de = dePicker.getValue();
        LocalDate ate = atePicker.getValue();
        String busca = buscaField.getText();
        try {
            List<Curriculo> lista = dao.filtrar(cargo, status, de, ate, busca);
            dados.setAll(lista);
        } catch (SQLException ex) {
            alertErro("Falha no filtro:", ex.getMessage());
        }
    }

    @FXML
    public void limparFiltro() {
        buscaField.clear(); cargoField.clear();
        statusBox.getSelectionModel().select("TODOS");
        dePicker.setValue(null); atePicker.setValue(null);
        recarregar();
    }

    @FXML
    public void exportarCsv() {
        if (dados.isEmpty()) { alertInfo("Nada para exportar."); return; }
        File out = new File("data/export_curriculos.csv");
        out.getParentFile().mkdirs();
        try (FileWriter fw = new FileWriter(out)) {
            fw.write("id;nome;email;telefone;cargo;status;data;linkedin;skills;experiencia\n");
            for (Curriculo c : dados) {
                fw.write(s(c.getId())+";"+s(c.getNome())+";"+s(c.getEmail())+";"+s(c.getTelefone())+";"+
                        s(c.getCargoDesejado())+";"+s(c.getStatusProcesso())+";"+s(c.getDataCadastro())+";"+
                        s(c.getLinkedin())+";"+s(c.getSkills())+";"+s(c.getExperiencia())+"\n");
            }
            alertInfo("Exportado: " + out.getAbsolutePath());
        } catch (IOException ex) {
            alertErro("Falha ao exportar:", ex.getMessage());
        }
    }

    private String s(Object o) { return o == null ? "" : o.toString().replace(";", ","); }

    private void abrirPdf(Curriculo c) {
        if (c == null || c.getCaminhoPdf() == null || c.getCaminhoPdf().isBlank()) { alertInfo("Sem PDF anexado."); return; }
        File f = new File(c.getCaminhoPdf());
        if (!f.exists()) { alertInfo("Arquivo não encontrado: " + f.getAbsolutePath()); return; }
        try {
            if (Desktop.isDesktopSupported()) Desktop.getDesktop().open(f);
        } catch (IOException ex) { alertErro("Falha ao abrir PDF:", ex.getMessage()); }
    }

    private void editar(Curriculo c) {
        if (c == null) return;
        // Abordagem simples: reabrir o formulário em modo de edição (pode ser evoluído)
        Alert a = new Alert(Alert.AlertType.INFORMATION, "Edição direta não implementada neste exemplo. Podemos habilitar em seguida.", ButtonType.OK);
        a.showAndWait();
    }

    private void excluir(Curriculo c) {
        if (c == null) return;
        Alert conf = new Alert(Alert.AlertType.CONFIRMATION, "Excluir currículo de " + c.getNome() + "?", ButtonType.YES, ButtonType.NO);
        conf.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                try {
                    dao.deletar(c.getId());
                    dados.remove(c);
                } catch (SQLException ex) {
                    alertErro("Falha ao excluir:", ex.getMessage());
                }
            }
        });
    }

    private void alertInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.showAndWait();
    }

    private void alertErro(String header, String content) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(header);
        a.setContentText(content);
        a.showAndWait();
    }
}