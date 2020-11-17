package org.ooad.chess.gui.component;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import org.ooad.chess.gui.graphics.GraphicsUtils;
import org.ooad.chess.gui.model.Component;
import org.ooad.chess.gui.model.DrawBox;

public class ImageComponent extends Component {

    private final String assetName;
    private int texture;

    public ImageComponent(String assetName) {
        this.assetName = assetName;
    }

    @Override
    public void init(GLAutoDrawable gl) {
        this.texture = GraphicsUtils.loadTexture(assetName);
    }

    @Override
    public void drawBeforeChildren(GL2 gl, DrawBox drawBox) {
        GraphicsUtils.drawTexture(gl, texture);
    }
}
