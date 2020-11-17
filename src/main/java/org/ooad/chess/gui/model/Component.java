package org.ooad.chess.gui.model;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.ooad.chess.gui.model.listener.MouseClickListener.MouseClickType;

public abstract class Component {

    private final List<Component> children = new LinkedList<>();
    protected boolean containsMouse;

    protected double x1 = 0;
    protected double y1 = 0;
    protected double x2 = 1;
    protected double y2 = 1;

    public Component() {
    }

    public Component(Runnable init) {
        init.run();
    }

    public void init(GLAutoDrawable gl) {
    }

    public void drawBeforeChildren(GL2 gl, DrawBox drawBox) {
    }

    public void drawAfterChildren(GL2 gl, DrawBox drawBox) {
    }

    public void mouseEnter() {
    }

    public void mouseLeave() {
    }

    public void mouseClick(MouseClickType clickType, double x, double y) {
    }

    public List<Component> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public boolean addChild(Component component) {
        return children.add(component);
    }

    public boolean removeChild(Component component) {
        return children.remove(component);
    }

    public void clearChildren() {
        children.clear();
    }

    public void setBounds(double x1, double y1, double x2, double y2) {
        setX1(x1);
        setY1(y1);
        setX2(x2);
        setY2(y2);
    }

    public double getX1() {
        return x1;
    }

    public double getY1() {
        return y1;
    }

    public double getX2() {
        return x2;
    }

    public double getY2() {
        return y2;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }

    final boolean isContainsMouse() {
        return containsMouse;
    }

    final void setContainsMouse(boolean containsMouse) {
        this.containsMouse = containsMouse;
    }
}
