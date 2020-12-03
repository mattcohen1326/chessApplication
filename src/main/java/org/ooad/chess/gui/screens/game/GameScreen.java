package org.ooad.chess.gui.screens.game;

import com.jogamp.opengl.GLAutoDrawable;
import org.ooad.chess.gui.component.FixedAspectComponent;
import org.ooad.chess.gui.component.PaddedComponent;
import org.ooad.chess.gui.model.Component;
import org.ooad.chess.logic.GameController;
import org.ooad.chess.logic.IGameController;
import org.ooad.chess.logic.players.AIPlayer;
import org.ooad.chess.logic.players.HumanPlayer;
import org.ooad.chess.model.player.Player;

import static org.ooad.chess.model.ChessmanColor.BLACK;
import static org.ooad.chess.model.ChessmanColor.WHITE;
import static org.ooad.chess.model.GameDifficulty.EASY;

public class GameScreen extends Component {

    @Override
    public void init(GLAutoDrawable gl) {
        Player player = new HumanPlayer(WHITE);
//        Player player2 = new HumanPlayer(BLACK);
        Player player2 = new AIPlayer(BLACK, EASY);

        IGameController controller = new GameController(player, player2);
        addChild(new PaddedComponent(new FixedAspectComponent(1, new GameBoard(controller)), 0.05));
    }

}
