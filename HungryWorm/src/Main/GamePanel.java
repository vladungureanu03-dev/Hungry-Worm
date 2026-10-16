package Main;

import GameStates.GameState;
import Inputs.KeyboardInputs;
import Inputs.MouseInputs;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

    private final Game game;

    public GamePanel(Game game) {
        this.game = game;

        MouseInputs mouseInputs = new MouseInputs(this);
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
        addKeyListener(new KeyboardInputs(this));

        setBackground(new Color(40, 40, 50));
        setPreferredSize(new Dimension(Game.GAME_WIDTH, Game.GAME_HEIGHT));
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        switch (GameState.state) {
            case MENU:
                game.getMenu().draw(g);
                break;
            case PLAYING:
                g.setColor(Color.WHITE);
                g.drawString("PLAYING - press ESC to return to menu", 20, 30);
                break;
            case SETTINGS:
                g.setColor(Color.WHITE);
                g.drawString("SETTINGS - press ESC to return to menu", 20, 30);
                break;
            default:
                break;
        }
    }

    public Game getGame() {
        return game;
    }
}
