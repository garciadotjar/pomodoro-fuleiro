package com.garciadotjar.pomodorofuleiro.controller;

import com.garciadotjar.pomodorofuleiro.Main;
import com.garciadotjar.pomodorofuleiro.app.PomodoroApplication;
import com.garciadotjar.pomodorofuleiro.config.ConfigManager;
import com.garciadotjar.pomodorofuleiro.config.Tarefa;
import com.garciadotjar.pomodorofuleiro.service.AlarmPlayer;
import com.garciadotjar.pomodorofuleiro.service.Cronometro;
import com.garciadotjar.pomodorofuleiro.service.Temporizador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.stage.Stage;

import java.time.Duration;

public class TelaInicialController {
    private Temporizador temporizador;
    private Cronometro cronometro;
    private Cronometro sessao; // total estudado na sessão
    private final AlarmPlayer alarme = new AlarmPlayer();
    private ObservableList<Tarefa> tarefas;
    private ConfigManager configManager = Main.getConfigManager();
    private long remainingNanos;
    private long totalFaseNanos; // duração total da fase atual, para calcular o progresso do anel
    private boolean estudando;
    private PomodoroApplication application;

    @FXML
    private Button descansar;

    @FXML
    private Button estudar;

    @FXML
    private Button comecar;

    @FXML
    private Label labelTempo;

    @FXML
    private Button pausar;

    @FXML
    private Button continuar;

    @FXML
    private Button voltarEstudar;

    @FXML
    private Label labelEtapa;

    @FXML
    private Arc arcoProgresso;

    @FXML
    private Label labelSessao;

    @FXML
    private Button pararEstudo;

    @FXML
    private VBox painelChecklist;

    @FXML
    private TextField novaTarefa;

    @FXML
    private ListView<Tarefa> listaTarefas;

    public TelaInicialController() {
        this.remainingNanos = 0;
    }

    @FXML
    public void initialize() {
        updateLabelConfig();
        telaTimer();
        estudando = true;
        cronometro = new Cronometro(labelTempo);
        sessao = new Cronometro(labelSessao);
        configurarChecklist();

        // hh:mm:ss tem 8 caracteres: usa fonte menor para continuar cabendo dentro do anel
        labelTempo.textProperty().addListener((obs, antigo, novo) -> ajustarTamanhoTempo(novo));
        ajustarTamanhoTempo(labelTempo.getText());
    }

    private void ajustarTamanhoTempo(String texto) {
        boolean longo = texto != null && texto.length() > 5;
        if (longo && !labelTempo.getStyleClass().contains("tempo-longo")) {
            labelTempo.getStyleClass().add("tempo-longo");
        } else if (!longo) {
            labelTempo.getStyleClass().remove("tempo-longo");
        }
    }

    @FXML
    private void comecarAction(){
        sessao.zerar();
        pararEstudo.setVisible(true);
        pararEstudo.setManaged(true);
        totalFaseNanos = configManager.getConfig().getTempoEstudo().toNanos();
        this.temporizador = new Temporizador(configManager.getConfig().getTempoEstudo(), labelTempo, this);
        temporizador.iniciar();
        estudando = true;
        telaRodando();
    }

    @FXML
    private void pausarAction(){
        temporizador.cancelar(false);
        cronometro.parar();
        telaPausada();
        estudando = false;
    }


    @FXML
    private void continuarAction(){
        Duration remainingDuration = Duration.ofNanos(remainingNanos);
        temporizador.encerrar();
        this.temporizador = new Temporizador(
                remainingDuration,
                labelTempo,
                this
        );
        this.temporizador.iniciar();
        telaRodando();
        estudando = true;
    }

    @FXML
    private void estudarAction(){
        alarme.parar();
        cronometro.zerar();
        cronometro.iniciar();
        telaContinuando();
        estudando = true;
    }

    @FXML
    private void descansarAction(){
        alarme.parar();
        cronometro.parar();
        temporizador.cancelar(false);
        temporizador.encerrar();
        totalFaseNanos = configManager.getConfig().getTempoLivre().toNanos();
        this.temporizador = new Temporizador(configManager.getConfig().getTempoLivre(), labelTempo, this);
        temporizador.iniciar();
        telaDescanso();
        estudando = false;


    }

    @FXML
    private void voltarEstudarAction(){
        alarme.parar();
        cronometro.parar();
        temporizador.cancelar(false);
        temporizador.encerrar();
        totalFaseNanos = configManager.getConfig().getTempoEstudo().toNanos();
        this.temporizador = new Temporizador(configManager.getConfig().getTempoEstudo(), labelTempo, this);
        temporizador.iniciar();
        telaRodando();
        estudando = true;


    }


    @FXML
    private void pararEstudoAction(){
        alarme.parar();
        cronometro.parar();
        sessao.parar(); // o total estudado continua na tela até a próxima sessão
        temporizador.encerrar();
        updateLabelConfig();
        telaTimer();
    }

    @FXML
    private void configuracoesAction() {
        if (painelChecklist.isVisible()) {
            checklistAction();
        }
        application.abrirConfiguracoes();
    }

    // CHECKLIST

    private void configurarChecklist() {
        tarefas = FXCollections.observableList(configManager.getConfig().getTarefas());
        listaTarefas.setItems(tarefas);
        listaTarefas.setPlaceholder(new Label("Nenhuma tarefa ainda"));
        listaTarefas.setCellFactory(lista -> new ListCell<>() {
            private final CheckBox check = new CheckBox();

            {
                check.getStyleClass().add("tarefa");
                check.setWrapText(true);
                check.prefWidthProperty().bind(lista.widthProperty().subtract(30));
                check.setOnAction(e -> {
                    getItem().setConcluida(check.isSelected());
                    configManager.exportConfig();
                });
            }

            @Override
            protected void updateItem(Tarefa tarefa, boolean vazio) {
                super.updateItem(tarefa, vazio);
                if (vazio || tarefa == null) {
                    setGraphic(null);
                    return;
                }
                check.setText(tarefa.getTexto());
                check.setSelected(tarefa.isConcluida());
                setGraphic(check);
            }
        });
    }

    @FXML
    private void checklistAction() {
        boolean abrir = !painelChecklist.isVisible();
        painelChecklist.setVisible(abrir);
        painelChecklist.setManaged(abrir);

        Stage stage = (Stage) painelChecklist.getScene().getWindow();
        stage.setResizable(true);
        stage.setWidth(stage.getWidth() + (abrir ? 1 : -1) * painelChecklist.getPrefWidth());
        stage.setResizable(false);
    }

    @FXML
    private void adicionarTarefaAction() {
        String texto = novaTarefa.getText().strip();
        if (texto.isEmpty()) {
            return;
        }
        tarefas.add(new Tarefa(texto));
        novaTarefa.clear();
        configManager.exportConfig();
    }

    @FXML
    private void limparConcluidasAction() {
        tarefas.removeIf(Tarefa::isConcluida);
        configManager.exportConfig();
    }

    private static final String[] MODOS = {
            "modo-pronto", "modo-estudo", "modo-descanso", "modo-extra", "modo-pausa", "modo-fim"
    };

    private void aplicarModo(String modo, String texto) {
        labelEtapa.setText(texto);
        for (Node no : new Node[]{arcoProgresso, labelEtapa}) {
            no.getStyleClass().removeAll(MODOS);
            no.getStyleClass().add(modo);
        }
    }

    /* Chamado a cada segundo pelo Temporizador com o tempo que ainda falta na fase. */
    public void atualizarProgresso(long restanteNanos) {
        double fracao = totalFaseNanos <= 0 ? 0 : (double) restanteNanos / totalFaseNanos;
        fracao = Math.max(0, Math.min(1, fracao));
        arcoProgresso.setLength(-360.0 * fracao); // negativo = sentido horário, a partir do topo
    }

    private void anelCheio() {
        arcoProgresso.setLength(-360.0);
    }

    public void telaTimer(){
        aplicarModo("modo-pronto", "Pronto para estudar");
        anelCheio();
        pararEstudo.setVisible(false);
        pararEstudo.setManaged(false);
        descansar.setManaged(false);
        descansar.setVisible(false);
        estudar.setManaged(false);
        estudar.setVisible(false);
        continuar.setManaged(false);
        continuar.setVisible(false);
        comecar.setVisible(true);
        comecar.setManaged(true);
        pausar.setVisible(false);
        pausar.setManaged(false);
        voltarEstudar.setVisible(false);
        voltarEstudar.setManaged(false);

    }


    public void telaRodando(){
        aplicarModo("modo-estudo", "Estudando");
        sessao.iniciar();
        descansar.setManaged(false);
        descansar.setVisible(false);
        estudar.setManaged(false);
        estudar.setVisible(false);
        continuar.setManaged(false);
        continuar.setVisible(false);
        comecar.setVisible(false);
        comecar.setManaged(false);
        pausar.setVisible(true);
        pausar.setManaged(true);
        voltarEstudar.setVisible(false);
        voltarEstudar.setManaged(false);

    }

    public void telaDescanso(){
        aplicarModo("modo-descanso", "Descansando");
        sessao.parar();
        descansar.setManaged(false);
        descansar.setVisible(false);
        estudar.setManaged(false);
        estudar.setVisible(false);
        continuar.setManaged(false);
        continuar.setVisible(false);
        comecar.setVisible(false);
        comecar.setManaged(false);
        pausar.setVisible(false);
        pausar.setManaged(false);
        voltarEstudar.setVisible(true);
        voltarEstudar.setManaged(true);

    }

    public void telaContinuando(){
        aplicarModo("modo-extra", "Estudando (tempo extra)");
        anelCheio();
        sessao.iniciar();
        descansar.setManaged(true);
        descansar.setVisible(true);
        estudar.setManaged(false);
        estudar.setVisible(false);
        continuar.setManaged(false);
        continuar.setVisible(false);
        comecar.setVisible(false);
        comecar.setManaged(false);
        pausar.setVisible(false);
        pausar.setManaged(false);
        voltarEstudar.setVisible(false);
        voltarEstudar.setManaged(false);

    }

    public void telaTempoAcabou(){
        aplicarModo("modo-fim", "Tempo de estudo acabou");
        anelCheio();
        sessao.parar();
        descansar.setManaged(true);
        descansar.setVisible(true);
        estudar.setManaged(true);
        estudar.setVisible(true);
        comecar.setManaged(false);
        comecar.setVisible(false);
        pausar.setVisible(false);
        pausar.setManaged(false);
        continuar.setManaged(false);
        continuar.setVisible(false);
        voltarEstudar.setVisible(false);
        voltarEstudar.setManaged(false);
        alarme.tocar(configManager.getConfig().getVolume());
    }


    public void telaDescansoAcabou(){
        aplicarModo("modo-fim", "Descanso acabou");
        anelCheio();
        sessao.parar();
        descansar.setManaged(false);
        descansar.setVisible(false);
        estudar.setManaged(false);
        estudar.setVisible(false);
        comecar.setManaged(false);
        comecar.setVisible(false);
        pausar.setVisible(false);
        pausar.setManaged(false);
        continuar.setManaged(false);
        continuar.setVisible(false);
        voltarEstudar.setVisible(true);
        voltarEstudar.setManaged(true);
        alarme.tocar(configManager.getConfig().getVolume());
    }

    public void telaPausada(){
        aplicarModo("modo-pausa", "Pausado");
        sessao.parar();
        continuar.setManaged(true);
        continuar.setVisible(true);
        pausar.setVisible(false);
        pausar.setManaged(false);
        descansar.setManaged(false);
        descansar.setVisible(false);
        estudar.setManaged(false);
        estudar.setVisible(false);
        comecar.setManaged(false);
        comecar.setVisible(false);
        voltarEstudar.setVisible(false);
        voltarEstudar.setManaged(false);

    }
    //------------------- set label para config atual

    public void updateLabelConfig(){
        labelTempo.setText(Temporizador.formatar(Math.max(0, (long) Math.ceil(
                configManager.getConfig().getTempoEstudo().toNanos() / 1_000_000_000.0
        ))));
    }

    public void setRemainingNanos(long remainingNanos) {
        this.remainingNanos = remainingNanos;
    }

    public void setApplication(PomodoroApplication application) {
        this.application = application;
    }


    public boolean isEstudando() {
        return estudando;
    }
    public void encerrar() {
        alarme.parar();

        if (temporizador != null) {
            temporizador.encerrar();
        }

        if (cronometro != null) {
            cronometro.encerrar();
        }

        if (sessao != null) {
            sessao.encerrar();
        }
    }
}
