package org.ooad.chess.gui.graphics;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public final class Fonts {

    public static Font ROBOTO_REGULAR = getFont("Roboto-Regular.ttf");

    private Fonts() {
    }

    private static Font getFont(String name) {
        try {
            InputStream resource = Fonts.class.getResourceAsStream(String.format("/assets/%s", name));
            Font font = Font.createFont(Font.TRUETYPE_FONT, resource);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);

            return font.deriveFont(Font.PLAIN, 22F);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
