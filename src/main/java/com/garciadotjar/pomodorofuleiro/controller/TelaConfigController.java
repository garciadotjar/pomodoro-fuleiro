package com.garciadotjar.pomodorofuleiro.controller;

import com.garciadotjar.pomodorofuleiro.Main;
import com.garciadotjar.pomodorofuleiro.app.PomodoroApplication;
import com.garciadotjar.pomodorofuleiro.config.ConfigManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.time.Duration;
import java.util.Locale;

public class TelaConfigController {

    private final ConfigManager configManager = Main.getConfigManager();
    private PomodoroApplication application;

    @FXML
    private Spinner<Integer> horaEstudo;

    @FXML
    private Spinner<Integer> minutoEstudo;

    @FXML
    private Spinner<Integer> horaDescanso;

    @FXML
    private Spinner<Integer> minutoDescanso;

    @FXML
    private Slider volumeAlarme;

    @FXML
    private Label valorVolume;


    @FXML
    public void initialize() {
        configurarSpinners();
        carregarConfiguracoes();
        configurarVolume();
    }


    private void configurarSpinners() {

        horaEstudo.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        0, 23, 0
                )
        );

        minutoEstudo.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        0, 59, 0
                )
        );

        horaDescanso.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        0, 23, 0
                )
        );

        minutoDescanso.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        0, 59, 0
                )
        );
    }

    private void carregarConfiguracoes() {

        Duration tempoEstudo = configManager.getConfig().getTempoEstudo();
        Duration tempoLivre = configManager.getConfig().getTempoLivre();

        // TEMPO DE ESTUDO
        horaEstudo.getValueFactory().setValue(
                (int) tempoEstudo.toHours()
        );

        minutoEstudo.getValueFactory().setValue(
                tempoEstudo.toMinutesPart()
        );


        // TEMPO DE DESCANSO
        horaDescanso.getValueFactory().setValue(
                (int) tempoLivre.toHours()
        );

        minutoDescanso.getValueFactory().setValue(
                tempoLivre.toMinutesPart()
        );


        // VOLUME
        volumeAlarme.setValue(
                configManager.getConfig().getVolume()
        );

        atualizarLabelVolume();
    }

    private void configurarVolume() {

        volumeAlarme.valueProperty().addListener(
                (observable, antigoValor, novoValor) -> {
                    atualizarLabelVolume();
                    atualizarTrilhaVolume();
                }
        );

        // ".track" só existe depois que a skin do slider é criada
        volumeAlarme.skinProperty().addListener((obs, antiga, nova) -> atualizarTrilhaVolume());
        Platform.runLater(this::atualizarTrilhaVolume);
    }

    private void atualizarTrilhaVolume() {
        Node trilha = volumeAlarme.lookup(".track");
        if (trilha == null) {
            return;
        }
        double pct = (volumeAlarme.getValue() - volumeAlarme.getMin())
                / (volumeAlarme.getMax() - volumeAlarme.getMin()) * 100.0;
        trilha.setStyle(String.format(Locale.ROOT,
                "-fx-background-color: linear-gradient(to right, -accent 0%%, -accent %.1f%%, -line %.1f%%, -line 100%%);",
                pct, pct));
    }


    private void atualizarLabelVolume() {

        int volume = (int) Math.round(volumeAlarme.getValue());

        valorVolume.setText(volume + "%");
    }


    @FXML
    private void salvarConfiguracoesAction() {

        int horasEstudo = horaEstudo.getValue();
        int minutosEstudo = minutoEstudo.getValue();

        int horasDescanso = horaDescanso.getValue();
        int minutosDescanso = minutoDescanso.getValue();

        int volume = (int) Math.round(volumeAlarme.getValue());


        Duration novoTempoEstudo = Duration.ofHours(horasEstudo)
                .plusMinutes(minutosEstudo);

        Duration novoTempoLivre = Duration.ofHours(horasDescanso)
                .plusMinutes(minutosDescanso);

        configManager.getConfig().setTempoEstudo(novoTempoEstudo);
        configManager.getConfig().setTempoLivre(novoTempoLivre);
        configManager.getConfig().setVolume(volume);


        configManager.exportConfig();

        application.voltarTelaInicial();
    }

    @FXML
    private void voltarAction(){
        application.voltarTelaInicial();
    }
    public void setApplication(PomodoroApplication application) {
        this.application = application;
    }
}