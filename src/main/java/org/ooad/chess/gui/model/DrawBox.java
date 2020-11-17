package org.ooad.chess.gui.model;

import java.awt.*;

public class DrawBox {

    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;

    private final int superWidth;
    private final int superHeight;

    public DrawBox(int x1, int y1, int x2, int y2, int superWidth, int superHeight) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.superWidth = superWidth;
        this.superHeight = superHeight;
    }

    public int getPixelX(double percentWidthX) {
        return x1 + (int) (percentWidthX * (x2 - x1));
    }

    public int getPixelY(double percentWidthY) {
        return y1 + (int) (percentWidthY * (y2 - y1));
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public boolean contains(Point point) {
        return contains(point.x, point.y);
    }

    public boolean contains(int pixelX, int pixelY) {
        return x1 <= pixelX && pixelX <= x2
                && y1 <= pixelY && pixelY <= y2;
    }

    public double getPercentX(int pixelX) {
        return (pixelX - x1) / (double) (x2 - x1);
    }

    public double getPercentY(int pixelY) {
        return (pixelY - y1) / (double) (y2 - y1);
    }

    public DrawBox computeChild(Component component) {
        return new DrawBox(
                (int) (x1 + component.x1 * (x2 - x1)),
                (int) (y1 + component.y1 * (y2 - y1)),
                (int) (x1 + component.x2 * (x2 - x1)),
                (int) (y1 + component.y2 * (y2 - y1)),
                superWidth,
                superHeight);
    }

    public int getSuperHeight() {
        return superHeight;
    }

    public int getSuperWidth() {
        return superWidth;
    }
}
