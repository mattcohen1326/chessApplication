package org.ooad.chess.gui.scenes;

import com.jogamp.opengl.GLAutoDrawable;
import org.ooad.chess.gui.component.BackgroundColorComponent;
import org.ooad.chess.gui.component.BlankComponent;
import org.ooad.chess.gui.component.FixedAspectComponent;
import org.ooad.chess.gui.model.Component;
import org.ooad.chess.gui.model.Scene;
import org.ooad.chess.gui.scenes.main.MainMenu;

import java.awt.*;

public abstract class AbstractScene extends Scene {

    @Override
    public final void init(GLAutoDrawable gl) {
        BackgroundColorComponent fullBg = new BackgroundColorComponent(new Color(66, 66, 66));
        FixedAspectComponent letterbox = new FixedAspectComponent(1, makeComponent(gl));
        fullBg.addChild(letterbox);
        addChild(fullBg);

        init1(gl);
    }

    protected void init1(GLAutoDrawable gl) {
    }

    protected void addMainMenuButton() {
        addChild(new BlankComponent() {
            {
                x1 = 0.90;
                x2 = 1;
                y2 = 1;
                y1 = 0.95;

                addChild(new MenuButton("Main Menu") {
                    {
                        setClick(() -> setScene(new MainMenu()));
                    }
                });
            }
        });
    }

    protected abstract Component makeComponent(GLAutoDrawable gl);
}
