package com.garciadotjar.pomodorofuleiro.app;

import com.garciadotjar.pomodorofuleiro.config.ConfigManager;
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

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Pomodoro Fuleiro");
        stage.setResizable(false);
        Scene scene = new Scene(telaInicial(), 322, 559);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("main.css")).toExternalForm());

        Image icon = new Image(
                getClass().getResource("icon.png").toExternalForm()
        );
        stage.getIcons().add(icon);

        stage.setScene(scene);
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

}
