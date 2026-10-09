package GameStates;

import java.awt.Point;
import java.util.Random;

public class Food {

    private final Random random = new Random();
    private Point position;

    public Food() {
        position = new Point(10, 10);
    }

    public void respawn(Worm worm) {
        do {
            position = new Point(random.nextInt(30), random.nextInt(22));
        } while (worm.contains(position));
    }

    public Point getPosition() {
        return position;
    }
}