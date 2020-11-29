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
        //b.getEngine().setPiece("B2",enemy);
        //Assert.assertEquals(,r.getMovement().movePath("A1","A8"));
        List<String> path = r.getMovement().movePath("A1","D4");
        //System.out.println(b.getIndex("A5"));
        //System.out.println(b.getPieces()[38]);
        //boolean blocked = b.getEngine().isBlocked(path);
        System.out.println(path);
        b.getEngine().movePiece("B2","B3");

    }
}
