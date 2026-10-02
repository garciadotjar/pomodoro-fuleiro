package com.garciadotjar.pomodorofuleiro.service;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Temporizador {

    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    private final Duration duration;
    private final Label label;

    private ScheduledFuture<?> task;

    public Temporizador(Duration duration, Label label) {
        this.duration = duration;
        this.label = label;
    }

    public void iniciar() {

        final long inicio = System.nanoTime();
        final long duracaoNanos = duration.toNanos();

        final ScheduledFuture<?>[] tarefa = new ScheduledFuture<?>[1];

        tarefa[0] = scheduler.scheduleAtFixedRate(() -> {

            long decorrido = System.nanoTime() - inicio;
            long restanteNanos = duracaoNanos - decorrido;

            long segundosRestantes =
                    Math.max(0, (long) Math.ceil(
                            restanteNanos / 1_000_000_000.0
                    ));

            Platform.runLater(() -> {
                label.setText(formatar(segundosRestantes));
            });

            if (restanteNanos <= 0) {

                tarefa[0].cancel(false);
                scheduler.shutdownNow();

            //    Platform.runLater(() -> { USAR COM O JAVAFX
                    System.out.println("tempo acabou");
                //    });
            }

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

    public void cancelar() {
        if (task != null) {
            task.cancel(false);
        }

        scheduler.shutdownNow();
    }
}
