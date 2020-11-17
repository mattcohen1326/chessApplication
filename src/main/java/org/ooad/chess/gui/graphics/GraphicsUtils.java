package org.ooad.chess.gui.graphics;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2GL3;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.awt.*;

import static com.jogamp.opengl.GL2.*;

public final class GraphicsUtils {

    private GraphicsUtils() {
    }

    public static void setColor(GL2 g, Color color) {
        g.glColor4f(color.getRed() / 255F,
                color.getGreen() / 255F,
                color.getBlue() / 255F,
                color.getAlpha() / 255F);
    }

    public static void drawBox(GL2 g) {
        g.glBegin(GL2GL3.GL_QUADS);
        g.glTexCoord2f(0, 0);
        g.glVertex2f(0, 0);

        g.glTexCoord2f(1, 0);
        g.glVertex2f(1, 0);

        g.glTexCoord2f(1, 1);
        g.glVertex2f(1, 1);

        g.glTexCoord2f(0, 1);
        g.glVertex2f(0, 1);
        g.glEnd();
    }

    public static int loadTexture(String assetName) {
        try {
            Texture texture = TextureIO.newTexture(GraphicsUtils.class.getResource(String.format("/assets/%s", assetName)), false, ".png");
            return texture.getTextureObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void drawTexture(GL2 gl, int texture) {
        gl.glEnable(GL_TEXTURE_2D);
        gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);
        drawBox(gl);
        gl.glDisable(GL_TEXTURE_2D);
    }
}
