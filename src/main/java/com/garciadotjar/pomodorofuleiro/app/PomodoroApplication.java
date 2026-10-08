package com.garciadotjar.pomodorofuleiro.app;

import com.garciadotjar.pomodorofuleiro.config.ConfigManager;
import com.garciadotjar.pomodorofuleiro.controller.TelaConfigController;
import com.garciadotjar.pomodorofuleiro.controller.TelaInicialController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class PomodoroApplication extends Application {
    private TelaInicialController telaInicialController;
    private TelaConfigController telaConfigController;

    private Scene sceneInicial;
    private Scene sceneConfig;
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Pomodoro Fuleiro");
        stage.setResizable(false);
        sceneInicial = new Scene(telaInicial(), 322, 559);
        sceneConfig = new Scene(telaConfig(), 322, 559);
        telaInicialController.setApplication(this);
        telaConfigController.setApplication(this);
        sceneInicial.getStylesheets().add(Objects.requireNonNull(getClass().getResource("main.css")).toExternalForm());
        sceneConfig.getStylesheets().add(Objects.requireNonNull(getClass().getResource("main.css")).toExternalForm());


        Image icon = new Image(
                getClass().getResource("icon.png").toExternalForm()
        );
        stage.getIcons().add(icon);

        stage.setScene(sceneInicial);
        stage.show();
    }


    // ------------- telas

    private Parent telaInicial() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("telaInicial.fxml")
        );
        Parent root = loader.load();
        telaInicialController = loader.getController();
        return root;
    }

    private Parent telaConfig() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("telaConfig.fxml")
        );

        Parent root = loader.load();
        telaConfigController = loader.getController();

        return root;
    }

    //trocar de telas
    public void abrirConfiguracoes() {
        Stage stage = (Stage) sceneInicial.getWindow();
        stage.setScene(sceneConfig);
    }

    public void voltarTelaInicial() {
        Stage stage = (Stage) sceneConfig.getWindow();
        stage.setScene(sceneInicial);
        telaInicialController.updateLabelConfig();
    }

    //encerrar tudo ao fechar a janela
    public void stop() {
        if (telaInicialController != null) {
            telaInicialController.encerrar();
        }
    }
}


