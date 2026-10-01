package com.garciadotjar.pomodorofuleiro.app;

import com.garciadotjar.pomodorofuleiro.config.ConfigManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class PomodoroApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Pomodoro Fuleiro");
        Scene scene = new Scene(telaInicial());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("main.css")).toExternalForm());

        //COLOCAR LOGO
        //Image icon = new Image(
        //        new File("LOGO PATH").toURI().toString()
        //);
        //stage.getIcons().add(icon);

        
        stage.setScene(scene);
        stage.show();
    }

    private Parent telaInicial() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("telaInicial.fxml")
        );
        return loader.load();
    }
}
