package org.ooad.chess;

import org.ooad.chess.model.Board;

public class Main {
    public  static String stringifyMove(int row,int col){
        StringBuilder str = new StringBuilder();
       // System.out.print((char)(col+64));
        str.append((char)(col+64));
        str.append(row);
        String check_pos = str.toString();
        return check_pos;
    }
    public static void main(String[] args) {
        /*Board board = Board.filledBoard();
        board.movePiece("B2", "B4");
        board.movePiece("C1", "A3");
        board.movePiece("B1", "C3");
        board.removePiece("A7");
        board.print();*/
        System.out.println(stringifyMove(1,2));
    }
}
