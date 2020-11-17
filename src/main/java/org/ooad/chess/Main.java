package org.ooad.chess;

import org.ooad.chess.model.Board;

public class Main {
    public static void main(String[] args) {
        /*Board board = Board.filledBoard();
        board.movePiece("B2", "B4");
        board.movePiece("C1", "A3");
        board.removePiece("A7");
        board.print();*/
        StringBuilder x = new StringBuilder();
        int a = 65;
        x.append((char)a);
        x.append(2);
        System.out.println(x.toString());
        x.setLength(0);
        System.out.println(x.toString());
    }
}
