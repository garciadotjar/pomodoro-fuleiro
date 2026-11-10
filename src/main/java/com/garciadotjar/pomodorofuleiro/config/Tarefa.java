package com.garciadotjar.pomodorofuleiro.config;

public class Tarefa {
    private String texto;
    private boolean concluida;

    // necessário para o Jackson
    public Tarefa() {
    }

    public Tarefa(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }
}
