package GameStates;

import GUI.LoadSave;
import GUI.MenuButton;
import Main.Game;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Menu extends State implements StateMethods {

    private final MenuButton[] buttons = new MenuButton[3];
    private BufferedImage backgroundImg;
    private int menuX, menuY, menuWidth, menuHeight;

    public Menu(Game game) {
        super(game);
        loadButtons();
        loadBackground();
    }

    private void loadButtons() {
        int centerX = Game.GAME_WIDTH / 2;
        buttons[0] = new MenuButton(centerX, 225, 0, GameState.PLAYING);
        buttons[1] = new MenuButton(centerX, 335, 1, GameState.SETTINGS);
        buttons[2] = new MenuButton(centerX, 445, 2, GameState.QUIT);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
        menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (50 * Game.SCALE);
    }

    @Override
    public void update() {
        for (MenuButton mb : buttons) {
            mb.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);
        for (MenuButton mb : buttons) {
            mb.draw(g);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isInButton(e, mb)) {
                mb.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isInButton(e, mb)) {
                if (mb.isMousePressed()) {
                    mb.applyGameState();
                }
                break;
            }
        }
        resetButtons();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton mb : buttons) {
            mb.setMouseOver(false);
        }
        for (MenuButton mb : buttons) {
            if (isInButton(e, mb)) {
                mb.setMouseOver(true);
                break;
            }
        }
    }

    private void resetButtons() {
        for (MenuButton mb : buttons) {
            mb.resetBools();
        }
    }
}
