package org.ooad.chess.logic.players;
import org.ooad.chess.GameStats;
import org.ooad.chess.model.*;
import java.util.List;
//keep stats in here?
public abstract class Player {
    protected ChessmanColor color;
    protected List<BoardPiece> pieces;
    private Board board;

    public GameStats stats = new GameStats();

    public ChessmanColor getColor() {
        return color;
    }

    public abstract void updatePieces(Board b);
}
