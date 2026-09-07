package com.wayne.wayneen.enterpriseswyne.controller;

import com.wayne.wayneen.enterpriseswyne.DAO.NotificacaoTipoDAO;
import com.wayne.wayneen.enterpriseswyne.model.NotificacaoTipoEntity;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.paint.Color;



import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;

public class NotificacaoTipoController {

    // Tabela
    @FXML private TableView<NotificacaoTipoEntity> tabela;
    @FXML private TableColumn<NotificacaoTipoEntity, String>  colCodigo;
    @FXML private TableColumn<NotificacaoTipoEntity, String>  colNome;
    @FXML private TableColumn<NotificacaoTipoEntity, String>  colCor;
    @FXML private TableColumn<NotificacaoTipoEntity, Number>  colOrdem;
    @FXML private TableColumn<NotificacaoTipoEntity, Boolean> colAtivo;

    // Filtros
    @FXML private TextField buscaField;

    // Form
    @FXML private TextField codigoField;
    @FXML private TextField nomeField;
    @FXML private TextField corHexField;
    @FXML private ColorPicker colorPicker;
    @FXML private Spinner<Integer> ordemSpinner;
    @FXML private CheckBox ativoCheck;

    @FXML private Button btnNovo;
    @FXML private Button btnSalvar;
    @FXML private Button btnExcluir;

    private final NotificacaoTipoDAO dao = new NotificacaoTipoDAO();
    private final ObservableList<NotificacaoTipoEntity> dados = FXCollections.observableArrayList();
    private FilteredList<NotificacaoTipoEntity> filtrados;

    private NotificacaoTipoEntity selecionado;

    @FXML
    private void initialize() {
        // colunas
        colCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigo()));
        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));
        colCor.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCorHex()));
        colOrdem.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getOrdem() == null ? 999 : c.getValue().getOrdem()));
        colAtivo.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isAtivo()));
        colAtivo.setCellFactory(CheckBoxTableCell.forTableColumn(colAtivo));

        // célula de cor visual (pinta o fundo com o HEX)
        colCor.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String hex, boolean empty) {
                super.updateItem(hex, empty);
                setText(empty ? null : hex);
                if (empty || hex == null || !isValidHex(hex)) {
                    setStyle("");
                } else {
                    setStyle("-fx-background-color: " + hex + "; -fx-text-fill: -fx-text-base-color;");
                }
            }
        });

        // spinner
        if (ordemSpinner != null) {
            ordemSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1));
        }

        // seleção na tabela -> preenche formulário
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            selecionado = val;
            preencherFormulario(val);
            limparErros();
        });

        // dados + filtro
        recarregar();
        filtrados = new FilteredList<>(dados, t -> true);
        tabela.setItems(filtrados);

        buscaField.textProperty().addListener((obs, old, val) -> {
            final String q = (val == null ? "" : val.trim().toLowerCase(Locale.ROOT));
            filtrados.setPredicate(t -> {
                if (q.isEmpty()) return true;
                return (t.getCodigo() != null && t.getCodigo().toLowerCase(Locale.ROOT).contains(q))
                        || (t.getNome() != null && t.getNome().toLowerCase(Locale.ROOT).contains(q));
            });
        });

        // sync ColorPicker -> Hex
        colorPicker.valueProperty().addListener((obs, old, cor) -> {
            if (cor != null) {
                String hex = toHex(cor);
                corHexField.setText(hex);
            }
        });

        // sync Hex -> ColorPicker (quando válido)
        corHexField.textProperty().addListener((obs, old, txt) -> {
            if (isValidHex(txt)) {
                try {
                    colorPicker.setValue(Color.web(txt));
                } catch (Exception ignored) {}
            }
        });

        // botões reativos
        btnExcluir.disableProperty().bind(Bindings.isNull(tabela.getSelectionModel().selectedItemProperty()));
    }

    private void recarregar() {
        try {
            dados.setAll(dao.listarTodos(false));
        } catch (SQLException e) {
            erro("Erro ao listar tipos", e.getMessage());
        }
    }

    private void preencherFormulario(NotificacaoTipoEntity t) {
        if (t == null) {
            codigoField.clear();
            nomeField.clear();
            corHexField.clear();
            if (ordemSpinner != null) ordemSpinner.getValueFactory().setValue(1);
            ativoCheck.setSelected(true);
            colorPicker.setValue(Color.web("#2196F3"));
            return;
        }
        codigoField.setText(t.getCodigo());
        nomeField.setText(t.getNome());
        corHexField.setText(t.getCorHex() == null ? "" : t.getCorHex());
        if (ordemSpinner != null) ordemSpinner.getValueFactory().setValue(t.getOrdem() == null ? 1 : t.getOrdem());
        ativoCheck.setSelected(t.isAtivo());

        if (isValidHex(t.getCorHex())) {
            try { colorPicker.setValue(Color.web(t.getCorHex())); } catch (Exception ignored) {}
        }
    }

    @FXML
    private void novo() {
        tabela.getSelectionModel().clearSelection();
        limparErros();
        preencherFormulario(null);
        selecionado = null;
    }

    @FXML
    private void salvar() {
        limparErros();
        boolean ok = validar();
        if (!ok) return;

        String codigo = codigoField.getText().trim().toUpperCase(Locale.ROOT);
        String nome   = nomeField.getText().trim();
        String cor    = corHexField.getText().trim();
        Integer ordem = ordemSpinner.getValue();
        boolean ativo = ativoCheck.isSelected();

        try {
            if (selecionado == null) {
                NotificacaoTipoEntity t = new NotificacaoTipoEntity(null, codigo, nome, cor, ordem, ativo);
                dao.inserir(t);
                dados.add(0, t);
                tabela.getSelectionModel().select(t);
            } else {
                selecionado.setCodigo(codigo);
                selecionado.setNome(nome);
                selecionado.setCorHex(cor);
                selecionado.setOrdem(ordem);
                selecionado.setAtivo(ativo);
                dao.atualizar(selecionado);
                tabela.refresh();
            }
            info("Sucesso", "Tipo salvo com sucesso.");
        } catch (SQLException e) {
            erro("Erro ao salvar", e.getMessage());
        }
    }

    @FXML
    private void excluir() {
        NotificacaoTipoEntity t = tabela.getSelectionModel().getSelectedItem();
        if (t == null) {
            info("Seleção necessária", "Escolha um tipo para excluir.");
            return;
        }
        Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
        conf.setTitle("Excluir tipo");
        conf.setHeaderText("Excluir \"" + (t.getNome() != null ? t.getNome() : t.getCodigo()) + "\"?");
        conf.setContentText("Essa ação não pode ser desfeita.");
        Optional<ButtonType> resp = conf.showAndWait();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            try {
                dao.deletar(t.getId());
                dados.remove(t);
                novo();
            } catch (SQLException e) {
                erro("Erro ao excluir", e.getMessage());
            }
        }
    }

    @FXML
    private void alternarAtivo() {
        NotificacaoTipoEntity t = tabela.getSelectionModel().getSelectedItem();
        if (t == null) {
            info("Seleção necessária", "Escolha um tipo para ativar/desativar.");
            return;
        }
        try {
            boolean novoAtivo = !t.isAtivo();
            dao.ativar(t.getId(), novoAtivo);
            t.setAtivo(novoAtivo);
            tabela.refresh();
        } catch (SQLException e) {
            erro("Erro ao atualizar status", e.getMessage());
        }
    }

    /* ---------- Validação Visual ---------- */

    private boolean validar() {
        boolean ok = true;
        if (vazio(codigoField.getText())) {
            marcarErro(codigoField); ok = false;
        } else if (codigoField.getText().trim().length() > 20) {
            marcarErro(codigoField); ok = false;
        }
        if (vazio(nomeField.getText())) {
            marcarErro(nomeField); ok = false;
        }
        String hex = corHexField.getText();
        if (!vazio(hex) && !isValidHex(hex)) {
            marcarErro(corHexField); ok = false;
        }
        if (!ok) {
            erro("Campos inválidos", "Verifique os campos destacados em vermelho.");
        }
        return ok;
    }

    private void marcarErro(Control c) {
        c.getStyleClass().add("field-error");
    }

    private void limparErros() {
        codigoField.getStyleClass().remove("field-error");
        nomeField.getStyleClass().remove("field-error");
        corHexField.getStyleClass().remove("field-error");
    }

    /* ---------- Util ---------- */

    private boolean vazio(String s) { return s == null || s.trim().isEmpty(); }

    private boolean isValidHex(String s) {
        if (s == null) return false;
        String v = s.trim();
        return v.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
    }

    private String toHex(Color c) {
        int r = (int)Math.round(c.getRed() * 255);
        int g = (int)Math.round(c.getGreen() * 255);
        int b = (int)Math.round(c.getBlue() * 255);
        return String.format("#%02X%02X%02X", r, g, b);
    }

    private void info(String titulo, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void erro(String titulo, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(titulo);
        a.setHeaderText(titulo);
        a.setContentText(msg);
        a.showAndWait();
    }


}
