import java.awt.*;
import java.util.LinkedList;

public class Snake {

    private LinkedList<Point> body;
    private char direction = 'R';
    private final int UNIT = 25;
    private Point lastRemovedTail = null;

    public Snake() {
        body = new LinkedList<>();
        body.add(new Point(100, 100));
    }

    public void move() {
        Point head = body.getFirst();
        Point newHead = new Point(head);

        switch (direction) {
            case 'U': newHead.y -= UNIT; break;
            case 'D': newHead.y += UNIT; break;
            case 'L': newHead.x -= UNIT; break;
            case 'R': newHead.x += UNIT; break;
        }

        body.addFirst(newHead);
        lastRemovedTail = new Point(body.removeLast());  // Save removed tail position
    }

    public void removeTail() {
        if (body.size() > 0) {
            body.removeLast();
        }
    }

    public void grow() {
        if (lastRemovedTail != null) {
            body.addLast(lastRemovedTail);  // Add back the removed tail segment
        }
    }

    public boolean checkSelfCollision() {
        Point head = body.getFirst();
        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i)))
                return true;
        }
        return false;
    }

    public void draw(Graphics g) {
        for (int i = 0; i < body.size(); i++) {
            g.setColor(i == 0 ? Color.GREEN : new Color(45, 180, 0));
            Point p = body.get(i);
            g.fillRoundRect(p.x, p.y, UNIT, UNIT, 10, 10);
        }
    }

    public void changeDirection(int key) {
        switch (key) {
            case 37: if (direction != 'R') direction = 'L'; break;
            case 38: if (direction != 'D') direction = 'U'; break;
            case 39: if (direction != 'L') direction = 'R'; break;
            case 40: if (direction != 'U') direction = 'D'; break;
        }
    }

    public LinkedList<Point> getBody() {
        return body;
    }
}
