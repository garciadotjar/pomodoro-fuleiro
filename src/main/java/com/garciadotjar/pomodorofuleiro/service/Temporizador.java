package com.garciadotjar.pomodorofuleiro.service;

import com.garciadotjar.pomodorofuleiro.controller.TelaInicialController;
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
    private final TelaInicialController telaInicialController;

    private ScheduledFuture<?> tarefa;

    // Guardam as informações necessárias para calcular
    // quanto tempo ainda falta quando o timer for cancelado.
    private long inicio;
    private long duracaoNanos;

    public Temporizador(
            Duration duration,
            Label label,
            TelaInicialController telaInicialController
    ) {
        this.duration = duration;
        this.label = label;
        this.telaInicialController = telaInicialController;
    }

    public void iniciar() {

        // Guarda o momento exato em que o timer começou
        inicio = System.nanoTime();

        // Converte a duração para nanossegundos
        duracaoNanos = duration.toNanos();

        tarefa = scheduler.scheduleAtFixedRate(() -> {

            // Quanto tempo já passou
            long decorrido = System.nanoTime() - inicio + 1_000_000_000;

            // Quanto tempo ainda falta
            long restanteNanos =
                    Math.max(0, duracaoNanos - decorrido);

            // Converte nanos para segundos
            long segundosRestantes =
                    (long) Math.ceil(
                            restanteNanos / 1_000_000_000.0
                    );

            // Atualiza a interface JavaFX
            Platform.runLater(() -> {
                label.setText(formatar(segundosRestantes));
            });

            // Timer acabou
            if (restanteNanos <= 0) {

                tarefa.cancel(true);
                if(telaInicialController.isEstudando()){
                    Platform.runLater(() -> {
                        telaInicialController.telaTempoAcabou();
                    });
                }else{
                    Platform.runLater(() -> {
                        telaInicialController.telaDescansoAcabou();
                    });
                }
            }

        }, 0, 1, TimeUnit.SECONDS);
    }

    public static String formatar(long segundos) {

        if (segundos < 3600) {

            long minutos = segundos / 60;
            long segundosRestantes = segundos % 60;

            return String.format(
                    "%02d:%02d",
                    minutos,
                    segundosRestantes
            );
        }

        long horas = segundos / 3600;
        long minutos = (segundos % 3600) / 60;
        long segundosRestantes = segundos % 60;

        return String.format(
                "%02d:%02d:%02d",
                horas,
                minutos,
                segundosRestantes
        );
    }

    public void cancelar(boolean trocarTela) {

        // Cancela a tarefa do timer
        if (tarefa != null) {
            tarefa.cancel(false);
        }

        // Calcula exatamente quanto tempo já passou
        long decorrido = System.nanoTime() - inicio;

        // Calcula quanto tempo ainda falta
        long restanteNanos =
                Math.max(0, duracaoNanos - decorrido);


        if (trocarTela) {

            telaInicialController.telaTempoAcabou();

        } else {

            // Envia o tempo restante para o controller
            telaInicialController.setRemainingNanos(restanteNanos);
        }

    }
    public void encerrar() {
        if (tarefa != null) {
            tarefa.cancel(true);
        }

        scheduler.shutdownNow();
    }


}
