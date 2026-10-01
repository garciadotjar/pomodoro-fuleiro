package com.garciadotjar.pomodorofuleiro.config;

import java.time.Duration;

public class Config {
    private Duration tempoEstudo;
    private Duration tempoLivre;
    private int volume;

    public Config() {
        this.tempoEstudo = Duration.ofMinutes(25);
        this.tempoLivre = Duration.ofMinutes(5);
        this.volume = 80;
    }

    public Config(Duration tempoEstudo, Duration tempoLivre, int volume) {
        this.tempoEstudo = tempoEstudo;
        this.tempoLivre = tempoLivre;
        this.volume = volume;
    }

    public Duration getTempoEstudo() {
        return tempoEstudo;
    }

    public void setTempoEstudo(Duration tempoEstudo) {
        this.tempoEstudo = tempoEstudo;
    }

    public Duration getTempoLivre() {
        return tempoLivre;
    }

    public void setTempoLivre(Duration tempoLivre) {
        this.tempoLivre = tempoLivre;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        if (volume < 0) {
            this.volume = 0;
        } else if (volume > 100) {
            this.volume = 100;
        } else {
            this.volume = volume;
        }
    }
}
