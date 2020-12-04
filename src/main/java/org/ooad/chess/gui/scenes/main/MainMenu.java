package org.ooad.chess.gui.scenes.main;

import com.jogamp.opengl.GLAutoDrawable;
import org.ooad.chess.gui.component.BlankComponent;
import org.ooad.chess.gui.component.FixedAspectComponent;
import org.ooad.chess.gui.component.ImageComponent;
import org.ooad.chess.gui.component.PaddedComponent;
import org.ooad.chess.gui.model.Component;
import org.ooad.chess.gui.scenes.AbstractScene;
import org.ooad.chess.gui.scenes.MenuButton;
import org.ooad.chess.gui.scenes.game.GameScreen;
import org.ooad.chess.logic.players.AIPlayer;
import org.ooad.chess.logic.players.HumanPlayer;

import static org.ooad.chess.model.ChessmanColor.BLACK;
import static org.ooad.chess.model.ChessmanColor.WHITE;
import static org.ooad.chess.model.GameDifficulty.EASY;
import static org.ooad.chess.model.GameDifficulty.MEDIUM;

public class MainMenu extends AbstractScene {

    @Override
    protected Component makeComponent(GLAutoDrawable gl) {
        BlankComponent root = new BlankComponent();
        root.addChild(new PaddedComponent(new FixedAspectComponent(1, new ImageComponent("board.png")), 0.05));

        root.addChild(new BlankComponent() {
            {
                x1 = 0.2;
                x2 = 0.8;

                addChild(new MainMenuHeader() {
                    {
                        y1 = 0.1;
                        y2 = 0.4;
                    }
                });

                float buttonStart = 0.5F;

                addChild(new BlankComponent() {
                    {
                        y1 = buttonStart;
                        y2 = buttonStart + 0.1;
                        addChild(new MenuButton("New Player vs Player Game") {
                            {
                                setClick(() -> {
                                    setScene(new GameScreen(new HumanPlayer(WHITE), new HumanPlayer(BLACK)));
                                });
                            }
                        });
                    }
                });

                addChild(new BlankComponent() {
                    {
                        y1 = buttonStart + 0.11;
                        y2 = buttonStart + 0.21;
                        addChild(new MenuButton("New Game vs Easy AI") {
                            {
                                setClick(() -> {
                                    setScene(new GameScreen(new HumanPlayer(WHITE), new AIPlayer(BLACK, EASY)));
                                });
                            }
                        });
                    }
                });

                addChild(new BlankComponent() {
                    {
                        y1 = buttonStart + 0.22;
                        y2 = buttonStart + 0.32;
                        addChild(new MenuButton("New Game vs Medium AI") {
                            {
                                setClick(() -> {
                                    setScene(new GameScreen(new HumanPlayer(WHITE), new AIPlayer(BLACK, MEDIUM)));
                                });
                            }
                        });
                    }
                });
            }
        });
        return root;
    }
}
