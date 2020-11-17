package org.ooad.chess.gui.screens.game;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import org.ooad.chess.gui.component.ImageComponent;
import org.ooad.chess.gui.model.Component;
import org.ooad.chess.gui.model.DrawBox;
import org.ooad.chess.model.Board;
import org.ooad.chess.model.BoardPiece;

import java.util.HashMap;
import java.util.Map;

class GameBoard extends Component {

    private final Map<GameChessman, BoardPiece> pieceGuiMapping = new HashMap<>();
    private final Board board;

    GameBoard(Board board) {
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
                int i = row * 8 + col;
                BoardPiece boardPiece = board.getPiece(i);
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
        // Move all pieces to their locations

        // TODO figure out why the board texture is slightly off
//        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
//        gl.glPushMatrix();
//        gl.glTranslatef(1, 1, 0);
//        gl.glScaled(-1 / 8.0, -1 / 8.0, 1);
//        for (int row = 0; row < 8; row++) {
//            for (int col = 0; col < 8; col++) {
//                GraphicsUtils.setColor(gl, Color.CYAN);
//                GraphicsUtils.drawBox(gl);
//                gl.glTranslatef(1, 0, 0);
//            }
//            gl.glTranslatef(-8, 0, 0);
//            gl.glTranslatef(0, 1, 0);
//        }
//        gl.glPopMatrix();
//        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }
}
