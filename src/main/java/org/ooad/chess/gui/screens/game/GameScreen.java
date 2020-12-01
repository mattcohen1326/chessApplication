package org.ooad.chess.gui.screens.game;

import com.jogamp.opengl.GLAutoDrawable;
import org.ooad.chess.gui.component.FixedAspectComponent;
import org.ooad.chess.gui.component.PaddedComponent;
import org.ooad.chess.gui.model.Component;
import org.ooad.chess.model.Board;
import org.ooad.chess.logic.MoveEngine;

public class GameScreen extends Component {

    @Override
    public void init(GLAutoDrawable gl) {
        Board board = Board.filledBoard();
        MoveEngine engine = new MoveEngine(board);
        addChild(new PaddedComponent(new FixedAspectComponent(1, new GameBoard(engine, board)), 0.05));
    }

}
