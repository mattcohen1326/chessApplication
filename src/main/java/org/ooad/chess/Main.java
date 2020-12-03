package org.ooad.chess;

import org.ooad.chess.gui.component.BackgroundColorComponent;
import org.ooad.chess.logic.MoveEngine;
import org.ooad.chess.logic.players.*;
import org.ooad.chess.model.*;
import org.ooad.chess.logic.gameController;
public class Main {

    public static void change(String s) {
        s = "HI";
        return;
    }
public static void main(String[] args) {
        Board board = Board.filledBoard();
        MoveEngine engine = new MoveEngine(board);
        //engine.testHelp("D2");//System.out.println(board.getPiece(new BoardPosition("A1")).getType());
        //engine.testHelp("B2");
        //engine.updateMoves("A2");
        //System.out.println(board.getPieces()[48].getType());
        //System.out.println(board.getPieces()[8].getAvailableMoves());
        //System.out.println(board.getPieces()[8].getAvailableMoves().get(0).toString().equals("A3") );
        AIPlayer p = new AIPlayer(ChessmanColor.BLACK,board);
        p.setDifficulty(GameDifficulty.MEDIUM);
        humanPlayer human = new humanPlayer(ChessmanColor.WHITE,board);
        gameController controller = new gameController(human,p);
        controller.testHelp();
        //System.out.println(p.getPieces().get(1).getAvailableMoves());
    }
}
