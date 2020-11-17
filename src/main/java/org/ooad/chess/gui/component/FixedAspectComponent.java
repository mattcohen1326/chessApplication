package org.ooad.chess.gui.component;

import com.jogamp.opengl.GL2;
import org.ooad.chess.gui.model.Component;
import org.ooad.chess.gui.model.DrawBox;

/**
 * Locks a component to a fixed aspect ratio
 */
public class FixedAspectComponent extends MonoComponent {

    private final double aspectRatio;

    public FixedAspectComponent(double aspectRatio, Component component) {
        super(component);
        this.aspectRatio = aspectRatio;
    }

    @Override
    public void drawBeforeChildren(GL2 gl, DrawBox drawBox) {
        int drawBoxWidth = drawBox.getX2() - drawBox.getX1();
        int drawBoxHeight = drawBox.getY2() - drawBox.getY1();

        int pixelHeight;
        if (drawBoxWidth < drawBoxHeight * aspectRatio) {
            pixelHeight = (int) (drawBoxWidth / aspectRatio / 2);
        } else {
            pixelHeight = drawBoxHeight / 2;

        }
        int pixelWidth = (int) (aspectRatio * pixelHeight);

        int middleX = (drawBox.getX2() + drawBox.getX1()) / 2;
        int middleY = (drawBox.getY2() + drawBox.getY1()) / 2;

        double px1 = drawBox.getPercentX(middleX - pixelWidth);
        double py1 = drawBox.getPercentY(middleY - pixelHeight);
        double px2 = drawBox.getPercentX(middleX + pixelWidth);
        double py2 = drawBox.getPercentY(middleY + pixelHeight);

        child.setX1(px1);
        child.setY1(py1);
        child.setX2(px2);
        child.setY2(py2);
    }
}
