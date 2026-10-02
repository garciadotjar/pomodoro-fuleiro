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
        Application.launch(PomodoroApplication.class, args);

        /*
        FAZER TODA A INTERFACE GRAFICA FUNCIONAR COM O CÓDIGO DO JEITO MAIS SIMPLES POSSÍVEL
        DEPOIS, FAZER O DESIGN DA INTERFACE
        */






        /*Cronometro cronometro = new Cronometro();
        cronometro.iniciar();
        cronometro.parar();
        */

        //TESTE 01. CONTADOR JA ESTA FUNCIONANDO
//        System.out.println("asd");
//        configManager.getConfig().setTempoEstudo(Duration.ofMinutes(7));
//        configManager.exportConfig();
//

    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }
}
