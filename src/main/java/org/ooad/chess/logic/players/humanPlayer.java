package org.ooad.chess.logic.players;

import org.ooad.chess.model.*;
import java.util.List;

public class humanPlayer extends Player {
    private Board board;
    private List<BoardPiece> pieces;
    public humanPlayer(Board b) {
        board = b;
        for (int i = 1; i <= board.LENGTH; i++) {
            for (int j = 1; j <= board.LENGTH; j++) {
                BoardPosition check_pos = new BoardPosition(i, j);
                if (this.board.hasPiece(check_pos)) {
                    if (this.board.getPiece(check_pos).getColor() == ChessmanColor.BLACK) {
                        pieces.add(this.board.getPiece(check_pos));
                    }
                }
            }
        }
    }

}
