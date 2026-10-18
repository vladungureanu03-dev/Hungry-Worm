package GUI;

import static GUI.Constants.UI.Buttons.*;
import GameStates.GameState;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class MenuButton {

    private final int x, y, row;
    private final int xCenter = B_WIDTH / 2;
    private final GameState state;
    private BufferedImage[] imgs;
    private int index;
    private boolean mouseOver, mousePressed;
    private Rectangle bounds;

    /** x is the horizontal CENTER of the button, y is its top edge. */
    public MenuButton(int x, int y, int row, GameState state) {
        this.x = x;
        this.y = y;
        this.row = row;
        this.state = state;
        loadImages();
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(x - xCenter, y, B_WIDTH, B_HEIGHT);
    }

    private void loadImages() {
        imgs = new BufferedImage[3];
        BufferedImage atlas = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = atlas.getSubimage(
                    i * B_WIDTH_DEFAULT, row * B_HEIGHT_DEFAULT,
                    B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
        }
    }

    public void draw(Graphics g) {
        g.drawImage(imgs[index], x - xCenter, y, B_WIDTH, B_HEIGHT, null);
    }

    public void update() {
        index = 0;
        if (mouseOver) {
            index = 1;
        }
        if (mousePressed) {
            index = 2;
        }
    }

    public boolean isMouseOver() { return mouseOver; }
    public void setMouseOver(boolean mouseOver) { this.mouseOver = mouseOver; }
    public boolean isMousePressed() { return mousePressed; }
    public void setMousePressed(boolean mousePressed) { this.mousePressed = mousePressed; }
    public Rectangle getBounds() { return bounds; }

    public void applyGameState() {
        GameState.state = state;
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }
}
