public class Main {
    public static void main(String[] args) {
        SoundManager sm = new SoundManager();
        SoundManager.playSound("GameStart.wav");
        new GameFrame();
    }
}

