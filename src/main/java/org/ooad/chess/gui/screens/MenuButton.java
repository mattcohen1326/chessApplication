package org.ooad.chess.gui.screens;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.awt.TextRenderer;
import org.ooad.chess.gui.graphics.Fonts;
import org.ooad.chess.gui.graphics.GraphicsUtils;
import org.ooad.chess.gui.model.Component;
import org.ooad.chess.gui.model.DrawBox;
import org.ooad.chess.gui.model.listener.MouseClickListener;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MenuButton extends Component {

    private static final Color ACTIVE = new Color(92, 92, 92, 250);
    private static final Color IDLE = new Color(92, 92, 92, 200);

    private final String text;
    private Runnable click = () -> {
    };

    private TextRenderer textRenderer;

    public MenuButton(String text) {
        this.text = text;
    }

    @Override
    public void init(GLAutoDrawable gl) {
        textRenderer = new TextRenderer(Fonts.ROBOTO_REGULAR, true, true);
    }

    @Override
    public void drawBeforeChildren(GL2 gl, DrawBox drawBox) {
        GraphicsUtils.setColor(gl, containsMouse ? ACTIVE : IDLE);
        GraphicsUtils.drawBox(gl);

        textRenderer.beginRendering(drawBox.getSuperWidth(), drawBox.getSuperHeight());
        textRenderer.setColor(Color.BLACK);
        textRenderer.setSmoothing(true);

        Rectangle2D bounds = textRenderer.getBounds(text);


        textRenderer.draw(text,
                (int) (drawBox.getPixelX(0.5) - (bounds.getWidth() / 2)),
                drawBox.getSuperHeight() - (int) (drawBox.getPixelY(0.5) + (bounds.getHeight() / 2)));
        textRenderer.endRendering();
    }

    @Override
    public void mouseClick(MouseClickListener.MouseClickType clickType, double x, double y) {
        if (clickType == MouseClickListener.MouseClickType.LEFT) {
            click.run();
        }
    }

    public void setClick(Runnable click) {
        this.click = click;
    }
}
