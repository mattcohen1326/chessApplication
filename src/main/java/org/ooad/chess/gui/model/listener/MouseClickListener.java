package org.ooad.chess.gui.model.listener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.BiConsumer;

/**
 * Hide away some of the Swing junk
 * Mouse clicked does not always pick up on clicks, so I have to do it this way
 */
public class MouseClickListener implements MouseListener {

    private final BiConsumer<MouseClickType, Point> mouseAction;
    private boolean mouseDown = false;

    public MouseClickListener(BiConsumer<MouseClickType, Point> mouseAction) {
        this.mouseAction = mouseAction;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseDown = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (mouseDown) {
            MouseClickType clickType;
            if (SwingUtilities.isLeftMouseButton(e)) {
                clickType = MouseClickType.LEFT;
            } else if (SwingUtilities.isRightMouseButton(e)) {
                clickType = MouseClickType.RIGHT;
            } else {
                return;
            }
            mouseAction.accept(clickType, new Point(e.getX(), e.getY()));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public enum MouseClickType {
        LEFT,
        RIGHT
    }
}
