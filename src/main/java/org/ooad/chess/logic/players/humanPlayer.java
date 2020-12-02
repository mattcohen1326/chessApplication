package org.ooad.chess.logic.players;

import org.ooad.chess.model.*;
import java.util.List;

public class humanPlayer extends Player {
    private Board board;
    private List<BoardPiece> pieces;

    public humanPlayer(ChessmanColor playerColor, Board board) {
        color = playerColor;

        for (int i = 0; i < board.getPieces().length; i++) {
            if (board.getPieces()[i].getColor() == color) {
                pieces.add(board.getPieces()[i]);
            }
        }
    }
}
