package org.ooad.chess;

import org.ooad.chess.model.Board;

public class Main {
    public static void main(String[] args) {
        Board board = Board.filledBoard();
        board.movePiece("B2", "B4");
        board.movePiece("C1", "A3");
        board.movePiece("B1", "C3");
        board.getEngine().removePiece("A7");
        board.print();

    }
}
