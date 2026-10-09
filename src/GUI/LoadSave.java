package GUI;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class LoadSave {

    public static final String MENU_BUTTONS = "menu_buttons.png";
    public static final String MENU_BACKGROUND = "menu_background.png";

    public static BufferedImage GetSpriteAtlas(String fileName) {
        try (InputStream is = LoadSave.class.getResourceAsStream("/" + fileName)) {
            if (is == null) {
                throw new RuntimeException("Resource not found on classpath: /" + fileName
                        + " (put it directly in src/ or a source folder)");
            }
            return ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException("Could not read " + fileName, e);
        }
    }

    //to be revised
}
