package org.ooad.chess;

import org.ooad.chess.logic.MoveEngine;
import org.ooad.chess.logic.players.AIPlayer;
import org.ooad.chess.model.Board;
import org.ooad.chess.model.ChessmanColor;
import org.ooad.chess.model.GameDifficulty;

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
        engine.updateMoves("A2");
        //System.out.println(board.getPieces()[48].getType());
        System.out.println(board.getPieces()[8].getAvailableMoves().get(0).toString().equals("A3"));
        AIPlayer p = new AIPlayer(ChessmanColor.BLACK, GameDifficulty.EASY);
    }
}
