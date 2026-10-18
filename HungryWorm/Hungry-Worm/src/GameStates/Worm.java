package GameStates;

import java.awt.Point;
import java.util.ArrayList;

public class Worm {

    private final ArrayList<Point> body = new ArrayList<>();
    private Direction direction = Direction.RIGHT;

    public Worm() {
        reset();
    }

    public void reset() {
        body.clear();
        direction = Direction.RIGHT;
        body.add(new Point(5, 5));
        body.add(new Point(4, 5));
        body.add(new Point(3, 5));
    }

    public void setDirection(Direction nextDir) {
        Direction current = direction;

        if ((current == Direction.UP && nextDir == Direction.DOWN)
                || (current == Direction.DOWN && nextDir == Direction.UP)
                || (current == Direction.LEFT && nextDir == Direction.RIGHT)
                || (current == Direction.RIGHT && nextDir == Direction.LEFT)) {
            return;
        }

        direction = nextDir;
    }

    public void move() {
        Point head = new Point(body.get(0));

        switch (direction) {
            case UP -> head.y--;
            case DOWN -> head.y++;
            case LEFT -> head.x--;
            case RIGHT -> head.x++;
        }

        body.add(0, head);
        body.remove(body.size() - 1);
    }

    public void grow() {
        Point head = new Point(body.get(0));

        switch (direction) {
            case UP -> head.y--;
            case DOWN -> head.y++;
            case LEFT -> head.x--;
            case RIGHT -> head.x++;
        }

        body.add(0, head);
    }

    public boolean contains(Point p) {
        return body.contains(p);
    }

    public Point getHead() {
        return body.get(0);
    }

    public ArrayList<Point> getBody() {
        return body;
    }

    public Direction getDirection() {
        return direction;
    }
}