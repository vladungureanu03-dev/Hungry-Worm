package GameStates;

import java.awt.event.MouseEvent;

import GUI.MenuButton;
import Main.Game;

public class State {

    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public boolean isInButton(MouseEvent e, MenuButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }

    public Game getGame() {
        return game;
    }
}
