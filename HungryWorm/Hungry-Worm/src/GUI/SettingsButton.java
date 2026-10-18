package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

/**
 * A small option button (e.g. "NORMAL", "MORE", "LOTS") drawn in the same
 * pixel-art wood palette as the menu buttons. States: idle, hover, pressed,
 * and selected (the currently active option of its row).
 */
public class SettingsButton {

    private static final Color OUTLINE = new Color(0x33323d);
    private static final Color FACE = new Color(0xdaab76);
    private static final Color FACE_HOVER = new Color(0xe7cd8d);
    private static final Color FACE_PRESSED = new Color(0x9e7751);
    private static final Color FACE_SELECTED = new Color(0x639d6d);
    private static final Color FACE_SELECTED_HOVER = new Color(0x7bb985);
    private static final Color SHADE = new Color(0xb77357);
    private static final Color SHADE_SELECTED = new Color(0x407769);
    private static final Color TEXT = new Color(0xa04f45);
    private static final Color TEXT_LIGHT = new Color(0xf4ecd0);

    private final Rectangle bounds;
    private final String label;
    private final int row;
    private final int option;
    private boolean mouseOver, mousePressed, selected;

    public SettingsButton(int x, int y, int width, int height, String label, int row, int option) {
        this.bounds = new Rectangle(x, y, width, height);
        this.label = label;
        this.row = row;
        this.option = option;
    }

    public void draw(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int x = bounds.x, w = bounds.width, h = bounds.height;
        int y = bounds.y + (mousePressed ? 3 : 0);   // pressed buttons sink down
        int hh = mousePressed ? h - 3 : h;

        Color face, shade;
        if (mousePressed) {
            face = FACE_PRESSED;
            shade = SHADE;
        } else if (selected) {
            face = mouseOver ? FACE_SELECTED_HOVER : FACE_SELECTED;
            shade = SHADE_SELECTED;
        } else {
            face = mouseOver ? FACE_HOVER : FACE;
            shade = SHADE;
        }

        // drop shadow (hidden when pressed)
        if (!mousePressed) {
            g.setColor(OUTLINE);
            g.fillRect(x + 2, y + 4, w, hh);
        }
        // outline, face, bottom shade, top highlight
        g.setColor(OUTLINE);
        g.fillRect(x, y, w, hh);
        g.setColor(face);
        g.fillRect(x + 3, y + 3, w - 6, hh - 6);
        g.setColor(shade);
        g.fillRect(x + 3, y + hh - 9, w - 6, 6);
        if (!mousePressed) {
            g.setColor(new Color(255, 255, 255, 90));
            g.fillRect(x + 3, y + 3, w - 6, 3);
        }
        // clipped corners for the pixel look
        g.setColor(OUTLINE);
        g.fillRect(x, y, 3, 3);
        g.fillRect(x + w - 3, y, 3, 3);

        // text
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        FontMetrics fm = g.getFontMetrics();
        int tx = x + (w - fm.stringWidth(label)) / 2;
        int ty = y + (hh - 6 - fm.getHeight()) / 2 + fm.getAscent() + 3;
        g.setColor(selected && !mousePressed ? SHADE_SELECTED : new Color(0, 0, 0, 60));
        g.drawString(label, tx + 1, ty + 2);
        g.setColor(selected && !mousePressed ? TEXT_LIGHT : TEXT);
        g.drawString(label, tx, ty);
    }

    public Rectangle getBounds() { return bounds; }
    public int getRow() { return row; }
    public int getOption() { return option; }
    public boolean isMousePressed() { return mousePressed; }
    public void setMousePressed(boolean mousePressed) { this.mousePressed = mousePressed; }
    public void setMouseOver(boolean mouseOver) { this.mouseOver = mouseOver; }
    public void setSelected(boolean selected) { this.selected = selected; }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }
}
