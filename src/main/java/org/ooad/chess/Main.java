package org.ooad.chess;

import org.junit.Assert;
import org.ooad.chess.model.*;
import java.util.List;
public class Main {

    public static void change(String s){
        s = "HI";
        return;
    }
    public static void main(String[] args) {
        Board b = Board.filledBoard();
        BoardPiece r = new BoardPiece(ChessmanTypes.BISHOP, ChessmanColor.WHITE);
        b.getEngine().setPiece("A1",r);
        //Assert.assertEquals(true,r.getMovement().movePossible("A1","A8",true,false));
        BoardPiece enemy = new BoardPiece(ChessmanTypes.PAWN,ChessmanColor.WHITE);
        b = Board.filledBoard();
        //b.getEngine().removePiece("F1");
        //b.getEngine().removePiece("G1");
        System.out.println(b.getPiece("G1").getType());
        boolean castle = b.getEngine().validCastle("E1","H1");
        System.out.println(castle);
        System.out.println(b.getEngine().getNeighbor("E1","left",4));

    }
}
