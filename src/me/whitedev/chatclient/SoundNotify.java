package me.whitedev.chatclient;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class SoundNotify {
    public void getMessage() {
        try {
            URL url = new URL("https://cdn.discordapp.com/attachments/903341551411920987/1066168995390181406/sound.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float gain = 0.15F;
            System.out.println(gain);
            float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
