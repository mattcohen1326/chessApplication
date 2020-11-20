package org.ooad.chess;

import org.ooad.chess.model.Board;

public class Main {
    public static void main(String[] args) {
        Board board = Board.filledBoard();
        board.getEngine().movePiece("B2", "B4");
        board.getEngine().movePiece("C1", "A3");
        board.getEngine().movePiece("B1", "C3");
        board.getEngine().removePiece("A7");
        board.print();

    }
}
