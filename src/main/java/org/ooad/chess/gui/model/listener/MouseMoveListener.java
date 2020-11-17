package org.ooad.chess.gui.model.listener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.function.Consumer;

public class MouseMoveListener implements MouseMotionListener {

    private final Consumer<Point> mouseMoveListener;
    private final JFrame jFrame;

    private int lastX;
    private int lastY;

    public MouseMoveListener(JFrame jFrame, Consumer<Point> mouseMoveListener) {
        this.jFrame = jFrame;
        this.mouseMoveListener = mouseMoveListener;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point point = jFrame.getMousePosition();

        if (point != null && (point.x != lastX || point.y != lastY)) {
            point = new Point(point.x, point.y);
            lastX = point.x;
            lastY = point.y;
            mouseMoveListener.accept(point);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }
}
