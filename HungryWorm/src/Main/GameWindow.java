package Main;

import javax.swing.JFrame;

public class GameWindow {

    private final JFrame frame;

    public GameWindow(GamePanel panel) {
        frame = new JFrame("My Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
