package org.ooad.chess.gui.scenes.game;

import com.jogamp.opengl.GL2;
import org.ooad.chess.gui.graphics.GraphicsUtils;
import org.ooad.chess.gui.model.Component;
import org.ooad.chess.gui.model.DrawBox;

import java.awt.*;

public class GameTile extends Component {

    private static final Color HIGHLIGHT = new Color(26, 255, 37, 87);

    @Override
    public void drawBeforeChildren(GL2 gl, DrawBox drawBox) {
        if (containsMouse) {
            GraphicsUtils.setColor(gl, HIGHLIGHT);
            GraphicsUtils.drawBox(gl);
        }
    }
}
