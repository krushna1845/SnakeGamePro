import java.awt.*;
import java.util.Random;

public class Food {

    private Point location;
    private final int UNIT = 25;
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private Random random = new Random();

    public Food() {
        randomLocation();
    }

    public void randomLocation() {
        int x = random.nextInt(WIDTH / UNIT) * UNIT;
        int y = random.nextInt(HEIGHT / UNIT) * UNIT;
        location = new Point(x, y);
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(location.x, location.y, UNIT, UNIT);
    }

    public Point getLocation() {
        return location;
    }
}
