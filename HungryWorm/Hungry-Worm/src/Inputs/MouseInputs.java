package Inputs;

import GameStates.GameState;
import Main.GamePanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInputs implements MouseListener, MouseMotionListener {

    private final GamePanel gamePanel;

    public MouseInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (GameState.state == GameState.MENU) {
            gamePanel.getGame().getMenu().mouseMoved(e);
        } else if (GameState.state == GameState.SETTINGS) {
            gamePanel.getGame().getSettings().mouseMoved(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (GameState.state == GameState.MENU) {
            gamePanel.getGame().getMenu().mousePressed(e);
            return;
        }

        if (GameState.state == GameState.SETTINGS) {
            gamePanel.getGame().getSettings().mousePressed(e);
            return;
        }

        if (GameState.state == GameState.GAME_OVER) {
            gamePanel.getGame().getPlaying().handleRestartClick(e.getX(), e.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (GameState.state == GameState.MENU) {
            gamePanel.getGame().getMenu().mouseReleased(e);
        } else if (GameState.state == GameState.SETTINGS) {
            gamePanel.getGame().getSettings().mouseReleased(e);
        }
    }

    @Override public void mouseClicked(MouseEvent e) { }
    @Override
    public void mouseDragged(MouseEvent e) {
        if (GameState.state == GameState.SETTINGS) {
            gamePanel.getGame().getSettings().mouseMoved(e);
        }
    }
    @Override public void mouseEntered(MouseEvent e) { }
    @Override public void mouseExited(MouseEvent e) { }
}
