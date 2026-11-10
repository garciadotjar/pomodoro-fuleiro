package com.garciadotjar.pomodorofuleiro.service;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class AlarmPlayer {

    private static final String ARQUIVO = "/com/garciadotjar/pomodorofuleiro/app/alarme.wav";

    private Clip clip;

    public void tocar(int volume) {
        parar();
        if (volume <= 0) {
            return;
        }

        URL url = Objects.requireNonNull(AlarmPlayer.class.getResource(ARQUIVO), ARQUIVO);
        try (AudioInputStream entrada = AudioSystem.getAudioInputStream(url)) {
            AudioFormat formato = entrada.getFormat();
            byte[] pcm = escalar(entrada.readAllBytes(), formato, volume);

            clip = AudioSystem.getClip();
            clip.open(formato, pcm, 0, pcm.length);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (IOException | UnsupportedAudioFileException
                 | LineUnavailableException | IllegalArgumentException e) {
            // Sem som (ex.: sem dispositivo de áudio), mas o timer segue funcionando.
            e.printStackTrace();
            parar();
        }
    }

    public void parar() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
        }
    }

    static byte[] escalar(byte[] pcm, AudioFormat formato, int volume) {
        if (formato.getEncoding() != AudioFormat.Encoding.PCM_SIGNED
                || formato.getSampleSizeInBits() != 16
                || formato.isBigEndian()) {
            throw new IllegalArgumentException("alarme.wav deve ser PCM 16 bits little-endian: " + formato);
        }

        double fator = volume / 100.0;
        for (int i = 0; i + 1 < pcm.length; i += 2) {
            short amostra = (short) ((pcm[i + 1] << 8) | (pcm[i] & 0xFF));
            short nova = (short) Math.round(amostra * fator);
            pcm[i] = (byte) nova;
            pcm[i + 1] = (byte) (nova >> 8);
        }
        return pcm;
    }
}
