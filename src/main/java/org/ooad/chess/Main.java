package org.ooad.chess;

import org.ooad.chess.model.Board;

public class Main {
    public static void main(String[] args) {
        Board board = Board.filledBoard();
        board.movePiece("A2", "A4");
        board.movePiece("A1", "A3");
        board.movePiece("C1", "E3");
        board.removePiece("A7");
        board.print();
    }
}
