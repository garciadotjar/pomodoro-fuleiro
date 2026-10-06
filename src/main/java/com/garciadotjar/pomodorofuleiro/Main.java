package com.garciadotjar.pomodorofuleiro;

import com.garciadotjar.pomodorofuleiro.app.PomodoroApplication;
import com.garciadotjar.pomodorofuleiro.config.Config;
import com.garciadotjar.pomodorofuleiro.config.ConfigManager;
import com.garciadotjar.pomodorofuleiro.service.Cronometro;
import com.garciadotjar.pomodorofuleiro.service.Temporizador;
import javafx.application.Application;

import java.time.Duration;

public class Main {
    private static ConfigManager configManager = new ConfigManager(new Config());
    public static void main(String[] args) {

        configManager.importConfig();
        //configManager.getConfig().setTempoEstudo(Duration.ofSeconds(10));
        //configManager.getConfig().setTempoLivre(Duration.ofSeconds(7));
        //configManager.exportConfig();
        Application.launch(PomodoroApplication.class, args);
        /*
        FAZER DE FORMA SIMPLES A TELA DE CONFIG FUNCIONAR
        LABEL PRA MARCAR EM QUE ETAPA DO ESTUDO ESTAMOS (estudando, descansando...)
        FAZER O CONTADOR DE TEMPO TOTAL DE ESTUDO
        DEPOIS, FAZER O DESIGN DA INTERFACE
        */
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }
}
