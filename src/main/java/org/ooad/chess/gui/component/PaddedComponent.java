package org.ooad.chess.gui.component;

import com.jogamp.opengl.GL2;
import org.ooad.chess.gui.model.Component;
import org.ooad.chess.gui.model.DrawBox;

public class PaddedComponent extends MonoComponent {

    public PaddedComponent(Component component, double x1, double y1, double x2, double y2) {
        super(component);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public PaddedComponent(Component component, double percent) {
        this(component, percent, percent, 1 - percent, 1 - percent);
    }

    @Override
    public void drawBeforeChildren(GL2 gl, DrawBox drawBox) {
    }
}
