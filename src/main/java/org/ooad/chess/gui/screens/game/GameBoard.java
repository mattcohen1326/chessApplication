package org.ooad.chess.gui.screens.game;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import org.ooad.chess.gui.component.ImageComponent;
import org.ooad.chess.gui.graphics.GraphicsUtils;
import org.ooad.chess.gui.model.Component;
import org.ooad.chess.gui.model.DrawBox;
import org.ooad.chess.gui.model.listener.MouseClickListener;
import org.ooad.chess.logic.MoveEngine;
import org.ooad.chess.model.Board;
import org.ooad.chess.model.BoardPiece;
import org.ooad.chess.model.BoardPosition;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jogamp.opengl.GL.GL_FRONT_AND_BACK;
import static com.jogamp.opengl.GL2GL3.GL_FILL;
import static com.jogamp.opengl.GL2GL3.GL_LINE;
import static org.ooad.chess.gui.model.listener.MouseClickListener.MouseClickType.LEFT;

class GameBoard extends Component {

    private final Map<GameChessman, BoardPiece> pieceGuiMapping = new HashMap<>();

    // TODO
    private final MoveEngine engine;
    private final Board board;

    private BoardPosition selected;

    GameBoard(MoveEngine engine, Board board) {
        this.engine = engine;
        this.board = board;
    }

    @Override
    public void init(GLAutoDrawable gl) {
        addChild(new ImageComponent("board.png"));
        rebuildGuiMapping();
        buildTiles();
    }

    private void rebuildGuiMapping() {
        pieceGuiMapping.clear();

        double x = 1;
        double y = 1;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                BoardPiece boardPiece = board.getPiece(new BoardPosition(row, col));
                if (boardPiece != null) {
                    GameChessman gameChessman = new GameChessman(boardPiece.getColor(), boardPiece.getType());
                    gameChessman.setBounds(x - 1 / 8.0, y - 1 / 8.0, x, y);

                    addChild(gameChessman);
                    pieceGuiMapping.put(gameChessman, boardPiece);
                }
                x -= 1 / 8.0;
            }
            y -= 1 / 8.0;
            x = 1;
        }
    }

    @Override
    public void mouseClick(MouseClickListener.MouseClickType clickType, double x, double y) {
        if (clickType != LEFT) {
            return;
        }

        int col = (int) Math.floor(x / (1 / 8.0));
        int row = (int) Math.floor((1 - y) / (1 / 8.0));

        selected = new BoardPosition(row, col);
    }

    private void buildTiles() {
        double x = 1;
        double y = 1;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                GameTile tile = new GameTile();
                tile.setBounds(x - 1 / 8.0, y - 1 / 8.0, x, y);
                addChild(tile);
                x -= 1 / 8.0;
            }
            y -= 1 / 8.0;
            x = 1;
        }
    }

    @Override
    public void drawAfterChildren(GL2 gl, DrawBox drawBox) {
        if (selected != null) {
            drawOutline(gl, selected, Color.CYAN);

            BoardPiece piece = board.getPiece(selected);
            if (piece != null) {
                engine.updateMoves(piece.getPosition().toString());
                List<BoardPosition> availableMoves = piece.getAvailableMoves();
//                System.out.println(selected + ": " + availableMoves);
                availableMoves.forEach(move -> drawOutline(gl, move, Color.BLUE));
            }
        }
    }

    private static void drawOutline(GL2 gl, BoardPosition position, Color color) {
        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        gl.glPushMatrix();
        gl.glTranslatef(0, 1, 0);
        gl.glScaled(1 / 8.0, -1 / 8.0, 1);

        gl.glTranslatef(position.getCol(), position.getRow(), 0);
        GraphicsUtils.setColor(gl, color);
        GraphicsUtils.drawBox(gl);
        gl.glPopMatrix();
        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }
}
