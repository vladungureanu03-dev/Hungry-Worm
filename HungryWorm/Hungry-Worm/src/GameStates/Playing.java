package GameStates;

import Main.Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Playing extends State implements StateMethods {

    private final Worm worm;
    private final List<Food> foods = new ArrayList<>();
    private int score = 0;
    private boolean gameOver = false;
    private boolean started = false;
    private long lastMoveTime = 0;
    private Rectangle restartButtonBounds;

    // these come from the Settings screen and are re-read at the start of every game
    private int tileSize = 20;
    private int gridWidth = 30;
    private int gridHeight = 26;
    private long moveDelayNs = 150_000_000L;

    private int pendingGrowth = 0;
    private long lastGrowTime = 0;
    private static final long GROW_DELAY_NS = 300_000_000L;

    public Playing(Game game) {
        super(game);
        worm = new Worm();
        resetGame();
    }

    
    public void resetGame() {
        applySettings();
        score = 0;
        pendingGrowth = 0;
        lastGrowTime = 0;
        gameOver = false;
        started = false;
        lastMoveTime = System.nanoTime();
        worm.reset();
        worm.setDirection(Direction.RIGHT);
        for (Food f : foods) {
            f.respawn(worm, foods, gridWidth, gridHeight);
        }
        restartButtonBounds = new Rectangle(300, 390, 200, 50);
    }

    private void applySettings() {
        Settings.FieldSize field = Settings.getFieldSize();
        tileSize = field.tile;
        gridWidth = field.cols;
        gridHeight = field.rows;
        moveDelayNs = Settings.getWormSpeed().moveDelayNs;
        foods.clear();
        for (int i = 0; i < Settings.getAppleCount().apples; i++) {
            foods.add(new Food());
        }
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

        if (pendingGrowth > 0 && now - lastGrowTime >= GROW_DELAY_NS) {
             worm.grow();
            pendingGrowth--;
            lastGrowTime = now;
        }

        if (now - lastMoveTime < moveDelayNs) {
            return;
        }

        lastMoveTime = now;
        worm.move();

        for (Food food : foods) {
            if (!worm.getHead().equals(food.getPosition())) {
                continue;
            }
            score += food.getValue();
            if (score < 0) score = 0;
            if (food.getValue() > 1) {
                pendingGrowth += food.getValue();
                lastGrowTime = now;
            }
            // Worm grows 5 tiles when eating a golden apple, with a delay of 300ms between each growth
            if (food.getValue() == 1) {
                worm.grow();
            }
            if (food.getValue() < 0) {
                //implement losing a life when eating a poisoned apple
            }
            food.respawn(worm, foods, gridWidth, gridHeight);
            break;
        }

        if (worm.getHead().x < 0 || worm.getHead().x >= gridWidth
                || worm.getHead().y < 0 || worm.getHead().y >= gridHeight) {
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
        int offsetX = (Game.GAME_WIDTH - gridWidth * tileSize) / 2;
        int offsetY = (Game.GAME_HEIGHT - gridHeight * tileSize) / 2;

        g.setColor(new Color(99, 247, 96));
        for (int x = 0; x <= gridWidth; x++) {
            g.drawLine(offsetX + x * tileSize, offsetY,
                    offsetX + x * tileSize, offsetY + gridHeight * tileSize);
        }
        for (int y = 0; y <= gridHeight; y++) {
            g.drawLine(offsetX, offsetY + y * tileSize,
                    offsetX + gridWidth * tileSize, offsetY + y * tileSize);
        }

        g.setColor(new Color(250, 148, 255));
        for (Point p : worm.getBody()) {
            g.fillRect(offsetX + p.x * tileSize, offsetY + p.y * tileSize,
                    tileSize, tileSize);
        }

        // Draw the food depending on seed value
        for (Food food : foods) {
            switch (food.getSeed()) {
                case 1 -> g.setColor(Color.YELLOW); // Golden apple
                case 2, 3 -> g.setColor(Color.MAGENTA); // Poisoned apple
                default -> g.setColor(Color.RED); // Normal apple
            }
            Point f = food.getPosition();
            g.fillRect(offsetX + f.x * tileSize, offsetY + f.y * tileSize,
                    tileSize, tileSize);
        }

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