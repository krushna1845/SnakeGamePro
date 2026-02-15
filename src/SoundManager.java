import java.io.File;
import javax.sound.sampled.*;

public class SoundManager {

    public static void playSound(String soundFileName) {
        try {
            // Path to resources folder
            File soundFile = new File("resources/" + soundFileName);

            if (!soundFile.exists()) {
                System.out.println("Audio file not found: " + soundFile.getAbsolutePath());
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
