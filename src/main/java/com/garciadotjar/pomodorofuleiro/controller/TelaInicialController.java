package com.garciadotjar.pomodorofuleiro.controller;

import com.garciadotjar.pomodorofuleiro.Main;
import com.garciadotjar.pomodorofuleiro.app.PomodoroApplication;
import com.garciadotjar.pomodorofuleiro.config.ConfigManager;
import com.garciadotjar.pomodorofuleiro.service.Temporizador;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class TelaInicialController {
    private ConfigManager configManager = Main.getConfigManager();

    @FXML
    private Button descansar;

    @FXML
    private Button estudar;

    @FXML
    private Button comecar;

    @FXML
    private Label labelTempo;

    @FXML
    public void initialize() {
        telaTimer();
    }

    @FXML
    private void comecarAction(){
        Temporizador temporizador = new Temporizador(configManager.getConfig().getTempoEstudo(), labelTempo, this);
        temporizador.iniciar();
    }

    public void telaTimer(){
        descansar.setManaged(false);
        descansar.setVisible(false);
        estudar.setManaged(false);
        estudar.setVisible(false);
    }

    public void telaTempoAcabou(){
        descansar.setManaged(true);
        descansar.setVisible(true);
        estudar.setManaged(true);
        estudar.setVisible(true);
        comecar.setManaged(false);
        comecar.setVisible(false);
    }
}
