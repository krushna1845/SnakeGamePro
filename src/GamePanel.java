import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 700;
    static final int SCREEN_HEIGHT = 700;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;

    int DELAY = 120;

    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];

    int bodyParts = 8;
    int applesEaten;
    int highScore;

    int appleX;
    int appleY;

    char direction = 'R';
    boolean running = false;
    boolean difficultySelected = false;

    Timer timer;
    Random random;

    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(15,15,25));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        loadHighScore();
    }

    public void startGame() {
        bodyParts = 8;
        applesEaten = 0;
        direction = 'R';
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
        SoundManager.playSound("GameStart.wav");
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        if (!difficultySelected) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("SNAKE GAME PRO", 180, 250);

            g.setFont(new Font("Arial", Font.PLAIN, 25));
            g.drawString("Press 1 - Easy Mode", 240, 330);
            g.drawString("Press 2 - Hard Mode", 240, 370);
            return;
        }

        if (running) {

            // Grid background
            g.setColor(new Color(30,30,45));
            for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++){
                g.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE,SCREEN_HEIGHT);
                g.drawLine(0,i*UNIT_SIZE,SCREEN_WIDTH,i*UNIT_SIZE);
            }

            // Animated apple
            g2d.setColor(Color.RED);
            g2d.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // Snake body with gradient
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g2d.setColor(new Color(0,255,120));
                } else {
                    g2d.setColor(new Color(0, 180 - i*2, 80));
                }
                g2d.fillRoundRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE, 10,10);
            }

            // Score UI
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + applesEaten, 20, 30);
            g.drawString("HighScore: " + highScore, 550, 30);

        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U': y[0] -= UNIT_SIZE; break;
            case 'D': y[0] += UNIT_SIZE; break;
            case 'L': x[0] -= UNIT_SIZE; break;
            case 'R': x[0] += UNIT_SIZE; break;
        }
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            newApple();
            SoundManager.playSound("FoodSound.wav");

            // Speed increase slightly
            if (DELAY > 60) {
                DELAY -= 2;
                timer.setDelay(DELAY);
            }
        }
    }

    public void checkCollisions() {

        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i])
                running = false;
        }

        if (x[0] < 0 || x[0] >= SCREEN_WIDTH ||
            y[0] < 0 || y[0] >= SCREEN_HEIGHT)
            running = false;

        if (!running) {
            timer.stop();
            SoundManager.playSound("GameOver.wav");
            saveHighScore();
        }
    }

    public void gameOver(Graphics g) {

        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("GAME OVER", 220, 300);

        g.setFont(new Font("Arial", Font.PLAIN, 25));
        g.drawString("Final Score: " + applesEaten, 270, 350);
        g.drawString("Press R to Restart", 250, 390);
    }

    public void loadHighScore() {
        try {
            File file = new File("highscore.txt");
            if (!file.exists()) return;

            BufferedReader br = new BufferedReader(new FileReader(file));
            highScore = Integer.parseInt(br.readLine());
            br.close();
        } catch (Exception e) {
            highScore = 0;
        }
    }

    public void saveHighScore() {
        if (applesEaten > highScore) {
            highScore = applesEaten;
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("highscore.txt"));
                bw.write("" + highScore);
                bw.close();
            } catch (Exception e) {}
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {

            if (!difficultySelected) {
                if (e.getKeyChar() == '1') {
                    DELAY = 120;
                    difficultySelected = true;
                    startGame();
                }
                if (e.getKeyChar() == '2') {
                    DELAY = 80;
                    difficultySelected = true;
                    startGame();
                }
            }

            if (running) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (direction != 'R') direction = 'L';
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'L') direction = 'R';
                        break;
                    case KeyEvent.VK_UP:
                        if (direction != 'D') direction = 'U';
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != 'U') direction = 'D';
                        break;
                }
            }

            if (!running && e.getKeyCode() == KeyEvent.VK_R) {
                difficultySelected = false;
                repaint();
            }
        }
    }
}
