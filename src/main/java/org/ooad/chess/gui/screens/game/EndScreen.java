package org.ooad.chess.gui.screens.game;

import com.jogamp.opengl.GLAutoDrawable;
import org.ooad.chess.gui.component.BackgroundColorComponent;
import org.ooad.chess.gui.component.BlankComponent;
import org.ooad.chess.gui.model.Component;
import org.ooad.chess.gui.screens.MenuButton;

import java.awt.*;

public class EndScreen extends Component {

    @Override
    public void init(GLAutoDrawable gl) {
        addChild(new BackgroundColorComponent(new Color(0, 0, 0, 0.8F)));

        addChild(new BlankComponent() {
            {
                x1 = 0.3;
                x2 = 0.7;

                addChild(new MenuButton("GG") {
                    {
                        y1 = 0.45;
                        y2 = 0.55;
                    }
                });
            }
        });
    }
}
