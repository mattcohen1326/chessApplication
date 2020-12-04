package org.ooad.chess.gui.scenes.game;

import com.jogamp.opengl.GLAutoDrawable;
import org.ooad.chess.gui.component.FixedAspectComponent;
import org.ooad.chess.gui.component.PaddedComponent;
import org.ooad.chess.gui.model.Component;
import org.ooad.chess.gui.scenes.AbstractScene;
import org.ooad.chess.logic.GameController;
import org.ooad.chess.logic.GameControllerImpl;
import org.ooad.chess.model.player.Player;

public class GameScreen extends AbstractScene {

    private final Player white;
    private final Player black;

    public GameScreen(Player white, Player black) {
        this.white = white;
        this.black = black;
    }

    @Override
    protected void init1(GLAutoDrawable gl) {
        addMainMenuButton();
    }

    @Override
    protected Component makeComponent(GLAutoDrawable gl) {
        GameController controller = new GameControllerImpl(white, black);
        return new PaddedComponent(new FixedAspectComponent(1, new GameBoard(controller)), 0.05);
    }
}
