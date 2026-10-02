package com.garciadotjar.pomodorofuleiro.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Cronometro {

    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    //private final Label label;

    private ScheduledFuture<?> tarefa;

    public Cronometro(){//, Label label) {
        //this.label = label;
    }

    public void iniciar() {

        final long inicio = System.nanoTime();

        final ScheduledFuture<?>[] tarefa = new ScheduledFuture<?>[1];

        tarefa[0] = scheduler.scheduleAtFixedRate(() -> {

            long decorrido = System.nanoTime() - inicio - 1_000_000_000;

            long segundosRestantes =
                    Math.max(0, (long) Math.ceil(
                            decorrido / 1_000_000_000.0
                    ));

            //Platform.runLater(() -> { USAR COM O JAVAFX
            System.out.println(formatar(segundosRestantes));
            //});



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
            tarefa.cancel(false);
        }

        scheduler.shutdownNow();
    }
}
