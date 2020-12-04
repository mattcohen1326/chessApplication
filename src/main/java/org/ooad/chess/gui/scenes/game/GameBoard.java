package org.ooad.chess.gui.scenes.game;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import org.ooad.chess.gui.component.ImageComponent;
import org.ooad.chess.gui.graphics.GraphicsUtils;
import org.ooad.chess.gui.model.Component;
import org.ooad.chess.gui.model.DrawBox;
import org.ooad.chess.gui.model.listener.MouseClickListener;
import org.ooad.chess.logic.GameController;
import org.ooad.chess.model.Board;
import org.ooad.chess.model.BoardMove;
import org.ooad.chess.model.BoardPiece;
import org.ooad.chess.model.BoardPosition;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.jogamp.opengl.GL.GL_FRONT_AND_BACK;
import static com.jogamp.opengl.GL2GL3.GL_LINE;
import static org.ooad.chess.gui.model.listener.MouseClickListener.MouseClickType.LEFT;
import static org.ooad.chess.model.Board.LENGTH;
import static org.ooad.chess.model.ChessmanColor.BLACK;

class GameBoard extends Component {

    private final GameController game;
    private final Board board;

    private final Set<Component> childPieces = new HashSet<>();
    private boolean completed;

    private BoardPosition selected;

    GameBoard(GameController game) {
        this.game = game;
        this.board = game.getBoard();
    }

    @Override
    public void init(GLAutoDrawable gl) {
        addChild(new ImageComponent("board.png"));
        addTileChildren();
        addPieceChildren();
    }

    public void updateBoard() {
        addPieceChildren();
        if (game.isInCheck(game.getCurrentPlayer())) {
            selected = game.getBoard().getKing(game.getCurrentPlayer().getColor()).getPosition();
        }
    }

    @Override
    public void drawBeforeChildren(GL2 gl, DrawBox drawBox) {
        if (game.isInCheckmate() && !completed) {
            completed = true;
            addChild(new EndScreen(game.getWinner().getColor()));

            // remove tiles
            getChildren().stream()
                    .filter(it -> it instanceof GameTile)
                    .forEach(this::removeChild);
        }
    }

    @Override
    public void mouseClick(MouseClickListener.MouseClickType clickType, double x, double y) {
        if (clickType != LEFT || completed) {
            return;
        }

        int col = (int) Math.floor(x / (1 / 8.0));
        int row = (int) Math.floor((1 - y) / (1 / 8.0));

        row = getPerspectiveRow(row);
        col = getPerspectiveCol(col);

        BoardPosition position = new BoardPosition(row, col);
        if (selected == null) {
            BoardPiece piece = board.getPiece(position);
            if (piece == null || piece.getColor() == game.getCurrentPlayer().getColor()) {
                selected = position;
            } else {
                selected = null;
            }
        } else {
            boolean result = game.makeMove(new BoardMove(selected, position));
            if (result) {
                updateBoard();
            }

            if (!game.isInCheck(game.getCurrentPlayer())) {
                selected = null;
            }
        }
    }

    @Override
    public void drawAfterChildren(GL2 gl, DrawBox drawBox) {
        if (selected != null) {
            drawOutline(gl, selected, Color.CYAN);

            BoardPiece piece = board.getPiece(selected);
            if (piece != null) {
                List<BoardPosition> availableMoves = piece.getAvailableMoves();
                availableMoves.forEach(move -> drawOutline(gl, move, Color.BLUE));
            }
        }
    }

    private void addPieceChildren() {
        childPieces.forEach(this::removeChild);
        childPieces.clear();
        double x = 0;
        double y = 1;
        double yStep = -1 / 8.0;

        for (int row = 0; row < 8; row++) {
            int perspectiveRow = getPerspectiveRow(row);
            for (int col = 0; col < 8; col++) {
                BoardPiece boardPiece = board.getPiece(new BoardPosition(perspectiveRow, getPerspectiveCol(col)));
                if (boardPiece != null) {
                    boolean active = game.getCurrentPlayer().getColor() == boardPiece.getColor();
                    // TODO disable active textures for now
                    active = false;
                    GameChessman gameChessman = new GameChessman(boardPiece.getColor(),
                            boardPiece.getType(),
                            active);
                    gameChessman.setBounds(x + 1 / 8.0, y - 1 / 8.0, x, y);

                    addChild(gameChessman);
                    childPieces.add(gameChessman);
                }
                x += 1 / 8.0;
            }
            y += yStep;
            x = 0;
        }
    }

    private void addTileChildren() {
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

    private void drawOutline(GL2 gl, BoardPosition position, Color color) {
        gl.glPushAttrib(GL_FRONT_AND_BACK);
        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        gl.glPushMatrix();
        gl.glTranslatef(0, 1, 0);
        gl.glScaled(1 / 8.0, -1 / 8.0, 1);

        gl.glTranslatef(getPerspectiveCol(position.getCol()), getPerspectiveRow(position.getRow()), 0);
        GraphicsUtils.setColor(gl, color);
        gl.glLineWidth(3);
        GraphicsUtils.drawBox(gl);
        gl.glPopMatrix();
        gl.glPopAttrib();
    }

    private int getPerspectiveRow(int row) {
        return isInverted() ? LENGTH - 1 - row : row;
    }

    private int getPerspectiveCol(int col) {
        return isInverted() ? LENGTH - 1 - col : col;
    }

    private boolean isInverted() {
        return game.getCurrentPlayer().getColor() == BLACK;
    }
}
