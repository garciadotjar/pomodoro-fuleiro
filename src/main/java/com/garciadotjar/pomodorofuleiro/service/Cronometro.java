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

    private long inicio;
    private long acumuladoNanos;

    public Cronometro(Label label) {
        this.label = label;
    }

    public void iniciar() {

        if (tarefa != null) {
            return; // já está rodando
        }

        final long inicioTarefa = System.nanoTime() - acumuladoNanos;
        inicio = inicioTarefa;

        tarefa = scheduler.scheduleAtFixedRate(() -> {

            long segundos = arredondar(System.nanoTime() - inicioTarefa);

            Platform.runLater(() -> {
                label.setText(formatar(segundos));
            });

        }, 0, 1, TimeUnit.SECONDS);
    }

    private static long arredondar(long nanos) {
        return Math.round(nanos / 1_000_000_000.0);
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
        if (tarefa == null) {
            return;
        }
        acumuladoNanos = System.nanoTime() - inicio;
        tarefa.cancel(true);
        tarefa = null;
        // o último tick pode ter sido há quase 1s
        label.setText(formatar(arredondar(acumuladoNanos)));
    }

    public void zerar() {
        parar();
        acumuladoNanos = 0;
        label.setText(formatar(0));
    }

    public void encerrar() {
        if (tarefa != null) {
            tarefa.cancel(true);
        }

        scheduler.shutdownNow();
    }
}
