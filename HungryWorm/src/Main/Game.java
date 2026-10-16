package Main;

import GameStates.GameState;
import GameStates.Menu;

public class Game implements Runnable {

    public static final float SCALE = 1.5f;
    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;
    private static final int FPS = 60;

    private final GamePanel panel;
    private final Menu menu;
    private Thread gameThread;

    public Game() {
        menu = new Menu(this);
        panel = new GamePanel(this);
        new GameWindow(panel);
        panel.requestFocus();
        startGameLoop();
    }

    private void startGameLoop() {
        gameThread = new Thread(this, "game-loop");
        gameThread.start();
    }

    private void update() {
        switch (GameState.state) {
            case MENU:
                menu.update();
                break;
            case PLAYING:
                // Update game logic for the playing state playing.update();
                break;
            case QUIT:
                System.exit(0);
                break;
            default:
                break;
        }
    }

    @Override
    public void run() {
        final double frameTime = 1_000_000_000.0 / FPS;
        long last = System.nanoTime();
        double delta = 0;

        while (true) {
            long now = System.nanoTime();
            delta += (now - last) / frameTime;
            last = now;

            if (delta >= 1) {
                update();
                panel.repaint();
                delta--;
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    public Menu getMenu() {
        return menu;
    }

    public static void main(String[] args) {
        new Game();
    }
}
