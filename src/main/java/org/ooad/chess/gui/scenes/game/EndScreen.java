package org.ooad.chess.gui.scenes.game;

import com.jogamp.opengl.GLAutoDrawable;
import org.ooad.chess.gui.component.BackgroundColorComponent;
import org.ooad.chess.gui.component.BlankComponent;
import org.ooad.chess.gui.model.Component;
import org.ooad.chess.gui.scenes.MenuButton;
import org.ooad.chess.gui.scenes.main.MainMenu;
import org.ooad.chess.model.ChessmanColor;

import java.awt.*;

public class EndScreen extends Component {

    private final ChessmanColor winner;

    public EndScreen(ChessmanColor winner) {
        this.winner = winner;
    }

    @Override
    public void init(GLAutoDrawable gl) {
        addChild(new BackgroundColorComponent(new Color(0, 0, 0, 0.8F)));

        addChild(new BlankComponent() {
            {
                x1 = 0.3;
                x2 = 0.7;

                addChild(new MenuButton(String.format("%s wins, return to main menu", winner.name().toLowerCase())) {
                    {
                        y1 = 0.45;
                        y2 = 0.55;

                        setClick(() -> setScene(new MainMenu()));
                    }
                });
            }
        });
    }
}
