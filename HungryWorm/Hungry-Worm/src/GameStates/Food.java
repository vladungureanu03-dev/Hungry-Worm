package GameStates;

import java.awt.Point;
import java.util.List;
import java.util.Random;

public class Food {

    private final Random random = new Random();
    private Point position;
    private int seed;
    private int value;

    public Food() {
        seed=4;
        position = new Point(10, 10);
    }

    public void respawn(Worm worm, List<Food> others, int cols, int rows) {
        seed = random.nextInt(10);
        switch (seed){
            case 1 -> value = 5; //golden apple
            case 2,3 -> value = -1; //poisoned apple
            default -> value = 1; //normal apple
        }
        do {
            position = new Point(random.nextInt(cols), random.nextInt(rows));
        } while (worm.contains(position) || takenByOther(others));
    }

    private boolean takenByOther(List<Food> others) {
        for (Food f : others) {
            if (f != this && position.equals(f.position)) return true;
        }
        return false;
    }

    public Point getPosition() {
        return position;
    }

    public int getSeed() {
        return seed;
    }

    public int getValue() {
        return value;
    }
}