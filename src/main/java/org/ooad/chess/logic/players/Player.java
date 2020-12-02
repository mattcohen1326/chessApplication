package org.ooad.chess.logic.players;
import org.ooad.chess.GameStats;
import org.ooad.chess.model.*;
import java.util.List;
//keep stats in here?
public abstract class Player {
    protected ChessmanColor color;
    protected List<BoardPiece> pieces;
    public GameStats stats = new GameStats();

    public ChessmanColor getColor() {
        return color;
    }
}
