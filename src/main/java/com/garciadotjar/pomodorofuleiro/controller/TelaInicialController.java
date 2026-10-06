package com.garciadotjar.pomodorofuleiro.controller;

import com.garciadotjar.pomodorofuleiro.Main;
import com.garciadotjar.pomodorofuleiro.config.ConfigManager;
import com.garciadotjar.pomodorofuleiro.service.Cronometro;
import com.garciadotjar.pomodorofuleiro.service.Temporizador;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.time.Duration;

public class TelaInicialController {
    private Temporizador temporizador;
    private Cronometro cronometro;
    private ConfigManager configManager = Main.getConfigManager();
    private long remainingNanos;
    private boolean estudando;

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

    public TelaInicialController() {
        this.remainingNanos = 0;
    }

    @FXML
    public void initialize() {
        updateLabelConfig();
        telaTimer();
        estudando = true;
        cronometro = new Cronometro(labelTempo);
    }

    @FXML
    private void comecarAction(){
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
        cronometro.iniciar();
        telaContinuando();
        estudando = true;
    }

    @FXML
    private void descansarAction(){
        cronometro.parar();
        temporizador.cancelar(false);
        temporizador.encerrar();
        this.temporizador = new Temporizador(configManager.getConfig().getTempoLivre(), labelTempo, this);
        temporizador.iniciar();
        telaDescanso();
        estudando = false;


    }

    @FXML
    private void voltarEstudarAction(){
        cronometro.parar();
        temporizador.cancelar(false);
        temporizador.encerrar();
        this.temporizador = new Temporizador(configManager.getConfig().getTempoLivre(), labelTempo, this);
        temporizador.iniciar();
        telaRodando();
        estudando = true;


    }


    // TELAS


    public void telaTimer(){
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
    }


    public void telaDescansoAcabou(){
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
    }

    public void telaPausada(){
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

    private void updateLabelConfig(){
        labelTempo.setText(Temporizador.formatar(Math.max(0, (long) Math.ceil(
                configManager.getConfig().getTempoEstudo().toNanos() / 1_000_000_000.0
        ))));
    }

    public void setRemainingNanos(long remainingNanos) {
        this.remainingNanos = remainingNanos;
    }

    public boolean isEstudando() {
        return estudando;
    }
    public void encerrar() {
        if (temporizador != null) {
            temporizador.encerrar();
        }

        if (cronometro != null) {
            cronometro.encerrar();
        }
    }
}
