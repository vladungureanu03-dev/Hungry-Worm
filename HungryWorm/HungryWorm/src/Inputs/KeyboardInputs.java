package Inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import GameStates.GameState;
import Main.GamePanel;

public class KeyboardInputs implements KeyListener {

    private final GamePanel gamePanel;

    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            GameState.state = GameState.MENU;
        }
    }

    @Override public void keyReleased(KeyEvent e) { }
    @Override public void keyTyped(KeyEvent e) { }
}
