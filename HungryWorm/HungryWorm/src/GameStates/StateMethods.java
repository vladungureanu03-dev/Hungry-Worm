package GameStates;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

public interface StateMethods {
    void update();
    void draw(Graphics g);
    void mousePressed(MouseEvent e);
    void mouseReleased(MouseEvent e);
    void mouseMoved(MouseEvent e);
}
