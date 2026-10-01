package com.garciadotjar.pomodorofuleiro;

import com.garciadotjar.pomodorofuleiro.app.PomodoroApplication;
import com.garciadotjar.pomodorofuleiro.config.Config;
import com.garciadotjar.pomodorofuleiro.config.ConfigManager;
import com.garciadotjar.pomodorofuleiro.service.Temporizador;
import javafx.application.Application;

import java.time.Duration;

public class Main {

    public static void main(String[] args) {
        ConfigManager configManager = new ConfigManager(new Config());
        configManager.importConfig();
        Application.launch(PomodoroApplication.class, args);

        /*
        TODA A INTERFACE GRAFICA
           TO-DO LIST
        */

        //TESTE 01. CONTADOR JA ESTA FUNCIONANDO
//        System.out.println("asd");
//        configManager.getConfig().setTempoEstudo(Duration.ofMinutes(7));
//        configManager.exportConfig();
//        Temporizador temporizador = new Temporizador(configManager.getConfig().getTempoEstudo());
//        temporizador.iniciar();

    }
}
