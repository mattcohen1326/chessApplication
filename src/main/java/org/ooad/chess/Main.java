package org.ooad.chess;

import org.ooad.chess.logic.MoveEngine;
import org.ooad.chess.logic.players.AIPlayer;
import org.ooad.chess.model.*;

import static org.ooad.chess.model.ChessmanColor.BLACK;
import static org.ooad.chess.model.ChessmanColor.WHITE;
import static org.ooad.chess.model.ChessmanTypes.*;
import static org.ooad.chess.model.ChessmanTypes.PAWN;

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
        //System.out.println(board.getPieces()[8].getAvailableMoves().get(0).toString().equals("A3"));
        engine.movePiece("B1", "C3");
        System.out.println(board.getPiece(new BoardPosition("C2")).getColor());
        System.out.println(board.getPiece(new BoardPosition("C2")).getMovement().movePath("C2","C3"));
        System.out.println(engine.isBlocked(board.getPiece(new BoardPosition("C2")).getMovement().movePath("C2","C3")));
        engine.movePiece("C2", "C3");
    }
}
