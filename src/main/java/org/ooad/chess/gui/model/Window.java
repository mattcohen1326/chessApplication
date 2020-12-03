package org.ooad.chess.gui.model;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import org.ooad.chess.gui.model.listener.MouseClickListener;
import org.ooad.chess.gui.model.listener.MouseMoveListener;
import org.ooad.chess.gui.screens.main.MainMenu;

import javax.swing.*;
import java.awt.*;

import static org.ooad.chess.gui.model.listener.MouseClickListener.MouseClickType;

public class Window extends JFrame implements GLEventListener {

    private final GLCanvas canvas;
    private Component activeComponent = new MainMenu();

    public Window(String title) {
        GLProfile glp = GLProfile.get("GL2");
        GLCapabilities capabilities = new GLCapabilities(glp);

        canvas = new GLCanvas(capabilities);
        canvas.addGLEventListener(this);
        canvas.addMouseListener(new MouseClickListener(this::mouseClick));
        canvas.addMouseMotionListener(new MouseMoveListener(this, this::mouseMove));
        canvas.setSize(900, 500);

        getContentPane().add(canvas);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        pack();
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void open() {
        setVisible(true);
        mainLoop();
    }

    private void mainLoop() {
        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void mouseClick(MouseClickType clickType, Point point) {
        DrawBox drawBox = new DrawBox(0,
                0,
                canvas.getWidth(),
                canvas.getHeight(),
                canvas.getWidth(),
                canvas.getHeight());
        mouseClick(activeComponent, drawBox, clickType, point.x, point.y);
    }

    private void mouseClick(Component component, DrawBox drawBox, MouseClickType clickType, int x, int y) {
        DrawBox childDrawBox = drawBox.computeChild(component);
        if (childDrawBox.contains(x, y)) {
            double percentX = childDrawBox.getPercentX(x);
            double percentY = childDrawBox.getPercentY(y);
            component.mouseClick(clickType, percentX, percentY);
            component.getChildren().forEach(child -> mouseClick(child, childDrawBox, clickType, x, y));
        }
    }

    private void mouseMove(Point point) {
        DrawBox drawBox = new DrawBox(0,
                0,
                getWidth(),
                getHeight(),
                canvas.getWidth(),
                canvas.getHeight());
        mouseMove(activeComponent, drawBox, point);
    }

    private void mouseMove(Component component, DrawBox drawBox, Point point) {
        DrawBox childDrawBox = drawBox.computeChild(component);
        boolean contains = childDrawBox.contains(point);
        if (contains && !component.isContainsMouse()) {
            component.mouseEnter();
            component.setContainsMouse(true);
        } else if (!contains && component.isContainsMouse()) {
            component.mouseLeave();
            component.setContainsMouse(false);
        }

        component.getChildren().forEach(child -> mouseMove(child, childDrawBox, point));
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        init(activeComponent, drawable);
    }

    private void init(Component component, GLAutoDrawable drawable) {
        component.init(drawable);
        component.getChildren().forEach(child -> init(child, drawable));
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glPushMatrix();
        gl.glTranslatef(-1, 1, 0);
        gl.glScalef(2, -2, 1);
        draw(activeComponent, new DrawBox(0,
                0,
                canvas.getWidth(),
                canvas.getHeight(),
                canvas.getWidth(),
                canvas.getHeight()), gl);
        gl.glPopMatrix();
    }

    private void draw(Component component, DrawBox parentDrawBox, GL2 gl) {
        DrawBox childDrawBox = parentDrawBox.computeChild(component);
        gl.glPushMatrix();
        gl.glTranslated(component.x1, component.y1, 0);
        gl.glScaled(component.x2 - component.x1, component.y2 - component.y1, 1);
        component.drawBeforeChildren(gl, childDrawBox);
        component.getChildren().forEach(child -> draw(child, childDrawBox, gl));
        component.drawAfterChildren(gl, childDrawBox);
        gl.glPopMatrix();
    }


    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void setActiveComponent(Component screen) {
        this.activeComponent = screen;
    }
}
