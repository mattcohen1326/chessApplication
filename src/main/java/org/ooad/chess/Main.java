package org.ooad.chess;

import org.ooad.chess.gui.component.BackgroundColorComponent;
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
        //engine.removePiece("C8");
        //engine.removePiece("B8");
        //System.out.println(board.getPiece(new BoardPosition("F1")).getType());
        //System.out.println(engine.getNeighbor("E1","right",1));
        //engine.
        //System.out.println(board.getPiece(new BoardPosition("H1")).getType());
        //For some reason A1 comes up as a bishop
        boolean castle = engine.validCastle("E1", "A1");
        //System.out.println(board.getPiece(new BoardPosition("C1")).getType());
        System.out.println(castle);
        //ystem.out.println(engine.getNeighbor("E1", "left", 4));

    }
}
