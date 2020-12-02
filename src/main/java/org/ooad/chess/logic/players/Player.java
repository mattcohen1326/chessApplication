package org.ooad.chess.logic.players;
import org.ooad.chess.model.*;
import java.util.List;
//keep stats in here?
public abstract class Player {
    ChessmanColor color;
    private List<BoardPiece> pieces;

    public ChessmanColor getColor() {
        return color;
    }
}
