package ch.fhnw.ip6.praxisruf.speechsynthesis.service;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import lombok.experimental.UtilityClass;

import java.io.InputStream;

import static javazoom.jl.player.FactoryRegistry.systemRegistry;

/**
 * This is a UtilityClass used for testing and verifying audio content locally and manually.
 */
@UtilityClass
public class AudioPlayer {

    public void play(InputStream inputStream) {
        try {
            final AdvancedPlayer player = new AdvancedPlayer(inputStream, systemRegistry().createAudioDevice());
            player.setPlayBackListener(new PlayBackListener());
            player.play();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
    }

    private static class PlayBackListener extends PlaybackListener {
        @Override
        public void playbackStarted(PlaybackEvent evt) {
            System.out.println("Playback started");
        }
        @Override
        public void playbackFinished(PlaybackEvent evt) {
            System.out.println("Playback finished");
        }
    }

}
