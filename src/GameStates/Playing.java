package GameStates;

import Main.Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Playing extends State implements StateMethods {

    private final Worm worm;
    private final Food food;
    private int score = 0;
    private boolean gameOver = false;
    private boolean started = false;
    private long lastMoveTime = 0;
    private Rectangle restartButtonBounds;

    private final int TILE_SIZE = 20;
    private final int GRID_WIDTH = 30;
    private final int GRID_HEIGHT = 26;
    private static final long MOVE_DELAY_NS = 150_000_000L;

    public Playing(Game game) {
        super(game);
        worm = new Worm();
        food = new Food();
        resetGame();
    }

    public void resetGame() {
        score = 0;
        gameOver = false;
        started = false;
        lastMoveTime = System.nanoTime();
        worm.reset();
        worm.setDirection(Direction.RIGHT);
        food.respawn(worm);
        restartButtonBounds = new Rectangle(300, 390, 200, 50);
    }

    public boolean handleRestartClick(int x, int y) {
        if (restartButtonBounds != null && restartButtonBounds.contains(x, y)) {
            game.getPlaying().resetGame();
            GameState.state = GameState.PLAYING;
            return true;
        }
        return false;
    }

    @Override
    public void update() {
        if (gameOver || !started) {
            return;
        }

        long now = System.nanoTime();
        if (now - lastMoveTime < MOVE_DELAY_NS) {
            return;
        }

        lastMoveTime = now;
        worm.move();

        if (worm.getHead().equals(food.getPosition())) {
            score++;
            worm.grow();
            food.respawn(worm);
        }

        if (worm.getHead().x < 0 || worm.getHead().x >= GRID_WIDTH
                || worm.getHead().y < 0 || worm.getHead().y >= GRID_HEIGHT) {
            gameOver = true;
            GameState.state = GameState.GAME_OVER;
            return;
        }

        for (int i = 1; i < worm.getBody().size(); i++) {
            if (worm.getHead().equals(worm.getBody().get(i))) {
                gameOver = true;
                GameState.state = GameState.GAME_OVER;
                return;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        int offsetX = 120;
        int offsetY = 40;

        g.setColor(new Color(99, 247, 96));
        for (int x = 0; x <= GRID_WIDTH; x++) {
            g.drawLine(offsetX + x * TILE_SIZE, offsetY,
                    offsetX + x * TILE_SIZE, offsetY + GRID_HEIGHT * TILE_SIZE);
        }
        for (int y = 0; y <= GRID_HEIGHT; y++) {
            g.drawLine(offsetX, offsetY + y * TILE_SIZE,
                    offsetX + GRID_WIDTH * TILE_SIZE, offsetY + y * TILE_SIZE);
        }

        g.setColor(new Color(250, 148, 255));
        for (Point p : worm.getBody()) {
            g.fillRect(offsetX + p.x * TILE_SIZE, offsetY + p.y * TILE_SIZE,
                    TILE_SIZE, TILE_SIZE);
        }

        g.setColor(Color.RED);
        Point f = food.getPosition();
        g.fillRect(offsetX + f.x * TILE_SIZE, offsetY + f.y * TILE_SIZE,
                TILE_SIZE, TILE_SIZE);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("Score: " + score, 20, 30);

        if (gameOver) {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over", 260, 270);

            g.setColor(new Color(70, 150, 255));
            g.fillRoundRect(300, 390, 200, 50, 20, 20);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 22));
            g.drawString("Restart", 365, 423);

            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString("Press ESC to return to menu", 225, 330);
            g.drawString("Press SPACE to start again", 240, 360);
        }
    }

    public void handleKeyPress(int keyCode) {
        if (gameOver && keyCode == KeyEvent.VK_SPACE) {
            resetGame();
            GameState.state = GameState.PLAYING;
            return;
        }

        if (keyCode == KeyEvent.VK_SPACE && !gameOver) {
            started = true;
            lastMoveTime = System.nanoTime();
            return;
        }

        if (keyCode == KeyEvent.VK_UP) {
            worm.setDirection(Direction.UP);
            started = true;
        } else if (keyCode == KeyEvent.VK_DOWN) {
            worm.setDirection(Direction.DOWN);
            started = true;
        } else if (keyCode == KeyEvent.VK_LEFT) {
            worm.setDirection(Direction.LEFT);
            started = true;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            worm.setDirection(Direction.RIGHT);
            started = true;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}