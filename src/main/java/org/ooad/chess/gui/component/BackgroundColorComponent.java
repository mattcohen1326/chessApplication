package org.ooad.chess.gui.component;

import com.jogamp.opengl.GL2;
import org.ooad.chess.gui.graphics.GraphicsUtils;
import org.ooad.chess.gui.model.Component;
import org.ooad.chess.gui.model.DrawBox;

import java.awt.*;

/**
 * Provides a background color for a component
 */
public class BackgroundColorComponent extends Component {

    private final Color backgroundColor;

    public BackgroundColorComponent(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void drawBeforeChildren(GL2 gl, DrawBox drawBox) {
        GraphicsUtils.setColor(gl, backgroundColor);
        GraphicsUtils.drawBox(gl);
    }
}
