package org.ooad.chess;

import org.ooad.chess.model.Board;
import org.ooad.chess.model.BoardPiece;

public class Main {

    public static void change(String s){
        s = "HI";
        return;
    }
    public static void main(String[] args) {
        Board board = Board.filledBoard();
        BoardPiece current = board.getPiece("B2");
        boolean move_pos = current.getMovement().movePossible("B7","B5",current.getFirst(),board.getEngine().isEliminating("B7","B5",current.getEnp()));
        boolean move_blocked = board.getEngine().isBlocked(current.getMovement().movePath("B2","B4"));
        System.out.println(move_pos);
        System.out.println(move_blocked);
        board.getEngine().movePiece("B2", "B4");
        board.getEngine().movePiece("B7","B5");
        //board.getEngine().movePiece("C1", "A3");
        //board.getEngine().movePiece("B1", "C3");
        //board.getEngine().removePiece("A7");
        board.print();

    }
}
