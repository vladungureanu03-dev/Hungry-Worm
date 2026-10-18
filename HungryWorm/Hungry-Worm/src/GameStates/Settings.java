package GameStates;

import GUI.LoadSave;
import GUI.SettingsButton;
import Main.Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Settings screen. Holds the global game options (static, read by Playing when a
 * round starts) and draws/handles the panel with its three rows of option buttons.
 */
public class Settings extends State implements StateMethods {

    // ---------- global options ----------
    public enum AppleCount {
        NORMAL("NORMAL", 1), MORE("MORE", 3), LOTS("LOTS", 6);
        public final String label; public final int apples;
        AppleCount(String label, int apples) { this.label = label; this.apples = apples; }
    }

    public enum WormSpeed {
        SLOW("SLOW", 220_000_000L), NORMAL("NORMAL", 150_000_000L), FAST("FAST", 90_000_000L);
        public final String label; public final long moveDelayNs;
        WormSpeed(String label, long moveDelayNs) { this.label = label; this.moveDelayNs = moveDelayNs; }
    }

    public enum FieldSize {
        NORMAL("NORMAL", 30, 26, 20), SMALL("SMALL", 20, 16, 20), BIG("BIG", 44, 35, 15);
        public final String label; public final int cols, rows, tile;
        FieldSize(String label, int cols, int rows, int tile) {
            this.label = label; this.cols = cols; this.rows = rows; this.tile = tile;
        }
    }

    private static AppleCount appleCount = AppleCount.NORMAL;
    private static WormSpeed wormSpeed = WormSpeed.NORMAL;
    private static FieldSize fieldSize = FieldSize.NORMAL;

    public static AppleCount getAppleCount() { return appleCount; }
    public static WormSpeed getWormSpeed() { return wormSpeed; }
    public static FieldSize getFieldSize() { return fieldSize; }

    // ---------- panel ----------
    private static final int PANEL_X = 50, PANEL_Y = 40, PANEL_W = 700, PANEL_H = 520;
    private static final int LABEL_W_DEFAULT = 236, LABEL_H = 56;
    private static final float LABEL_SCALE = 1.2f;
    private static final int BTN_W = 100, BTN_H = 46, BTN_GAP = 8;
    private static final int[] ROW_Y = {190, 300, 410};

    private final BufferedImage[] labels = new BufferedImage[3];
    private final SettingsButton[][] buttons = new SettingsButton[3][3];

    public Settings(Game game) {
        super(game);
        BufferedImage atlas = LoadSave.GetSpriteAtlas(LoadSave.SETTINGS_BUTTONS);
        for (int r = 0; r < 3; r++) {
            labels[r] = atlas.getSubimage(0, r * LABEL_H, LABEL_W_DEFAULT, LABEL_H);
        }
        String[][] names = {
            names(AppleCount.values()), names(WormSpeed.values()), names(FieldSize.values())
        };
        int labelW = (int) (LABEL_W_DEFAULT * LABEL_SCALE);
        int startX = PANEL_X + 34 + labelW + 20;
        for (int r = 0; r < 3; r++) {
            for (int o = 0; o < 3; o++) {
                int bh = BTN_H;
                int by = ROW_Y[r] + ((int) (LABEL_H * LABEL_SCALE) - bh) / 2;
                buttons[r][o] = new SettingsButton(startX + o * (BTN_W + BTN_GAP), by, BTN_W, bh, names[r][o], r, o);
            }
        }
        syncSelection();
    }

    private static String[] names(Enum<?>[] values) {
        String[] s = new String[values.length];
        for (int i = 0; i < s.length; i++) s[i] = values[i].toString();
        return s;
    }

    private void syncSelection() {
        int[] chosen = {appleCount.ordinal(), wormSpeed.ordinal(), fieldSize.ordinal()};
        for (int r = 0; r < 3; r++) {
            for (int o = 0; o < 3; o++) {
                buttons[r][o].setSelected(o == chosen[r]);
            }
        }
    }

    private void apply(SettingsButton b) {
        switch (b.getRow()) {
            case 0 -> appleCount = AppleCount.values()[b.getOption()];
            case 1 -> wormSpeed = WormSpeed.values()[b.getOption()];
            default -> fieldSize = FieldSize.values()[b.getOption()];
        }
        syncSelection();
    }

    private SettingsButton buttonAt(MouseEvent e) {
        for (SettingsButton[] row : buttons) {
            for (SettingsButton b : row) {
                if (b.getBounds().contains(e.getX(), e.getY())) return b;
            }
        }
        return null;
    }

    @Override
    public void update() { }

    @Override
    public void draw(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawPanel(g);

        int lw = (int) (LABEL_W_DEFAULT * LABEL_SCALE), lh = (int) (LABEL_H * LABEL_SCALE);
        for (int r = 0; r < 3; r++) {
            g.drawImage(labels[r], PANEL_X + 34, ROW_Y[r], lw, lh, null);
            for (SettingsButton b : buttons[r]) b.draw(g);
        }

        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        g.setColor(new Color(0xf4ecd0));
        g.drawString("Changes apply to the next game  -  press ESC to go back", PANEL_X + 120, PANEL_Y + PANEL_H - 34);
    }

    /** Wooden panel with a green frame, in the style of menu_background.png. */
    private void drawPanel(Graphics2D g) {
        Color outline = new Color(0x33323d);
        g.setColor(new Color(0, 0, 0, 110));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.setColor(outline);
        g.fillRect(PANEL_X, PANEL_Y, PANEL_W, PANEL_H);
        g.setColor(new Color(0x639d6d));
        g.fillRect(PANEL_X + 4, PANEL_Y + 4, PANEL_W - 8, PANEL_H - 8);
        g.setColor(new Color(0x407769));
        g.fillRect(PANEL_X + 4, PANEL_Y + PANEL_H - 12, PANEL_W - 8, 8);
        g.setColor(outline);
        g.fillRect(PANEL_X + 22, PANEL_Y + 22, PANEL_W - 44, PANEL_H - 44);

        // wooden planks
        int ix = PANEL_X + 26, iy = PANEL_Y + 26, iw = PANEL_W - 52, ih = PANEL_H - 52;
        g.setColor(new Color(0xae7764));
        g.fillRect(ix, iy, iw, ih);
        g.setColor(new Color(0x985252));
        for (int y = iy + 110; y < iy + ih; y += 110) {
            g.fillRect(ix, y - 2, iw, 4);
        }
        g.setColor(new Color(0x833f4c));
        g.fillRect(ix, iy + ih - 6, iw, 6);

        // title banner
        g.setColor(outline);
        g.fillRect(PANEL_X + 200, PANEL_Y + 26, PANEL_W - 400, 74);
        g.setColor(new Color(0xb77357));
        g.fillRect(PANEL_X + 204, PANEL_Y + 30, PANEL_W - 408, 66);
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 44));
        FontMetrics fm = g.getFontMetrics();
        String title = "OPTIONS";
        int tx = PANEL_X + (PANEL_W - fm.stringWidth(title)) / 2;
        int ty = PANEL_Y + 82;
        g.setColor(new Color(0xa04f45));
        g.drawString(title, tx + 3, ty + 4);
        g.setColor(new Color(0xdd8f61));
        g.drawString(title, tx, ty);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        SettingsButton b = buttonAt(e);
        if (b != null) b.setMousePressed(true);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        SettingsButton b = buttonAt(e);
        if (b != null && b.isMousePressed()) apply(b);
        for (SettingsButton[] row : buttons) {
            for (SettingsButton x : row) x.resetBools();
        }
        mouseMoved(e); // keep hover highlight after release
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        SettingsButton hit = buttonAt(e);
        for (SettingsButton[] row : buttons) {
            for (SettingsButton b : row) b.setMouseOver(b == hit);
        }
    }

    /** Clears hover/pressed state, e.g. when leaving the screen. */
    public void resetButtons() {
        for (SettingsButton[] row : buttons) {
            for (SettingsButton b : row) b.resetBools();
        }
    }
}
