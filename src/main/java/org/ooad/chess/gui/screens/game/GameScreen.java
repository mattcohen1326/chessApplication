package org.ooad.chess.gui.screens.game;

import com.jogamp.opengl.GLAutoDrawable;
import org.ooad.chess.gui.component.FixedAspectComponent;
import org.ooad.chess.gui.component.PaddedComponent;
import org.ooad.chess.gui.model.Component;
import org.ooad.chess.model.Board;

public class GameScreen extends Component {

    @Override
    public void init(GLAutoDrawable gl) {
        addChild(new PaddedComponent(new FixedAspectComponent(1, new GameBoard(Board.filledBoard())), 0.05));
    }

}
