package org.ooad.chess.gui.screens.main;

import com.jogamp.opengl.GLAutoDrawable;
import org.ooad.chess.gui.component.BlankComponent;
import org.ooad.chess.gui.component.FixedAspectComponent;
import org.ooad.chess.gui.component.ImageComponent;
import org.ooad.chess.gui.component.PaddedComponent;
import org.ooad.chess.gui.model.Component;
import org.ooad.chess.gui.screens.MenuButton;

public class MainMenu extends Component {

    @Override
    public void init(GLAutoDrawable gl) {
        addChild(new PaddedComponent(new FixedAspectComponent(1, new ImageComponent("board.png")), 0.05));

        addChild(new BlankComponent() {
            {
                x1 = 0.2;
                x2 = 0.8;

                addChild(new MainMenuHeader() {
                    {
                        y1 = 0.1;
                        y2 = 0.4;
                    }
                });

                addChild(new BlankComponent() {
                    {
                        y1 = 0.5;
                        y2 = 0.6;
                        addChild(new MenuButton("New Game") {
                            {
                                setClick(() -> {
                                    // TODO move to new game
                                });
                            }
                        });
                    }
                });
            }
        });
    }
}
