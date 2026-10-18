package Inputs;

import GameStates.GameState;
import Main.GamePanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputs implements KeyListener {

    private final GamePanel gamePanel;

    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_ESCAPE) {
            gamePanel.getGame().getSettings().resetButtons();
            gamePanel.getGame().getMenu().resetButtons();
            GameState.state = GameState.MENU;
            return;
        }

        if (key == KeyEvent.VK_SPACE) {
            if (GameState.state == GameState.MENU) {
                gamePanel.getGame().startGame();
                return;
            }
            if (GameState.state == GameState.GAME_OVER) {
                gamePanel.getGame().restartGame();
                return;
            }
        }

        if (GameState.state == GameState.PLAYING) {
            gamePanel.getGame().getPlaying().handleKeyPress(key);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}