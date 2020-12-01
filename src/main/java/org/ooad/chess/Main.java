package org.ooad.chess;

import org.ooad.chess.logic.MoveEngine;
import org.ooad.chess.model.*;

public class Main {

    public static void change(String s) {
        s = "HI";
        return;
    }

    public static void main(String[] args) {
        Board board = Board.filledBoard();
        MoveEngine engine = new MoveEngine(board);
        BoardPiece r = new BoardPiece(ChessmanTypes.BISHOP, ChessmanColor.WHITE);
        board.setPiece(new BoardPosition("A1"), r);
        //Assert.assertEquals(true,r.getMovement().movePossible("A1","A8",true,false));
        BoardPiece enemy = new BoardPiece(ChessmanTypes.PAWN, ChessmanColor.WHITE);
        board = Board.filledBoard();
        //b.getEngine().removePiece("F1");
        //b.getEngine().removePiece("G1");
        System.out.println(board.getPiece(new BoardPosition("G1")).getType());
        boolean castle = engine.validCastle("E1", "H1");
        System.out.println(castle);
        System.out.println(engine.getNeighbor("E1", "left", 4));

    }
}
