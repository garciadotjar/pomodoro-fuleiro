package com.garciadotjar.pomodorofuleiro.service;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Cronometro {

    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    private final Label label;

    private ScheduledFuture<?> tarefa;

    public Cronometro(Label label) {
        this.label = label;
    }

    public void iniciar() {

        final long inicio = System.nanoTime();

        tarefa = scheduler.scheduleAtFixedRate(() -> {

            long decorrido = System.nanoTime() - inicio;

            long segundosRestantes =
                    Math.max(0, (long) Math.ceil(
                            decorrido / 1_000_000_000.0
                    ));

            Platform.runLater(() -> {
                label.setText(formatar(segundosRestantes));
            });



        }, 0, 1, TimeUnit.SECONDS);
    }

    private String formatar(long segundos) {
        if(segundos<3600){
            long minutos = segundos / 60;
            long segundosRestantes = segundos % 60;
            return String.format("%02d:%02d", minutos, segundosRestantes);
        }
        long horas = segundos/3600;
        long minutos = segundos%3600/60;
        long segundosRestantes = segundos%60;
        return String.format("%02d:%02d:%02d", horas, minutos, segundosRestantes);
    }

    public void parar() {
        if (tarefa != null) {
            tarefa.cancel(true);
        }
    }
    public void encerrar() {
        if (tarefa != null) {
            tarefa.cancel(true);
        }

        scheduler.shutdownNow();
    }
}
