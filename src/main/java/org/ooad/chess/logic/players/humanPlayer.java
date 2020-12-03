package org.ooad.chess.logic.players;

import org.ooad.chess.model.*;

import java.util.ArrayList;
import java.util.List;

public class humanPlayer extends Player {
    private List<BoardPiece> pieces;
    private Board board;
    public humanPlayer(ChessmanColor playerColor, Board b) {
        color = playerColor;
        pieces = new ArrayList<BoardPiece>();
        board = b;
    }
    public void updatePieces(Board b){
        board = b;
        for (int i = 0; i < board.getPieces().length; i++) {
            if (board.getPieces()[i] == null){
                continue;
            }
            else if (board.getPieces()[i].getColor() == color) {
                pieces.add(board.getPieces()[i]);
            }
        }
    }

}
