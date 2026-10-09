package Main;

import GameStates.GameState;
import Inputs.KeyboardInputs;
import Inputs.MouseInputs;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

    private final Game game;
    private BufferedImage backgroundImage = null;

    public GamePanel(Game game) {
        this.game = game;

        MouseInputs mouseInputs = new MouseInputs(this);
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
        addKeyListener(new KeyboardInputs(this));

        setBackground(new Color(40, 40, 50));
        setPreferredSize(new Dimension(Game.GAME_WIDTH, Game.GAME_HEIGHT));
        setFocusable(true);
        setBackgroundImagePath("src/grass.jpg");
    }

    public void setBackgroundImagePath(String imagePath) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            backgroundImage = null;
            return;
        }

        try {
            backgroundImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            backgroundImage = null;
            System.out.println("Background image not loaded: " + imagePath);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }

        switch (GameState.state) {
            case MENU:
                game.getMenu().draw(g);
                break;
            case PLAYING:
                game.getPlaying().draw(g);
                break;
            case SETTINGS:
                g.setColor(Color.WHITE);
                g.drawString("SETTINGS - press ESC to return to menu", 20, 30);
                break;
            case GAME_OVER:
                game.getPlaying().draw(g);
                break;
            default:
                break;
        }
    }

    public Game getGame() {
        return game;
    }
}