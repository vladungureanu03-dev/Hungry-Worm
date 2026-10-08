package Inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import GameStates.GameState;
import Main.GamePanel;

public class MouseInputs implements MouseListener, MouseMotionListener {

    private final GamePanel gamePanel;

    public MouseInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (GameState.state == GameState.MENU) {
            gamePanel.getGame().getMenu().mouseMoved(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (GameState.state == GameState.MENU) {
            gamePanel.getGame().getMenu().mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (GameState.state == GameState.MENU) {
            gamePanel.getGame().getMenu().mouseReleased(e);
        }
    }

    @Override public void mouseClicked(MouseEvent e) { }
    @Override public void mouseDragged(MouseEvent e) { }
    @Override public void mouseEntered(MouseEvent e) { }
    @Override public void mouseExited(MouseEvent e) { }
}
