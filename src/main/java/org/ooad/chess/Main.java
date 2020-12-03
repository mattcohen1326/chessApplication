package org.ooad.chess;

import org.junit.Assert;
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

        engine.testHelp("B1");
        engine.testHelp("C1");
        engine.testHelp("D1");
        engine.updateMoves("A1");
        engine.updateMoves("D8");
        System.out.println(engine.validCastle("E1","A1"));
        System.out.println(board.getPiece(new BoardPosition("A1")).getAvailableMoves());
        engine.movePiece("A1","E1");
        System.out.println(board.getPiece(new BoardPosition("D1")).getType());
        //System.out.println(engine.isBlocked(board.getPiece(new BoardPosition("D8")).getMovement().movePath("D8","E")))
        //System.out.println(board.getPiece(new BoardPosition("E8")).getType());
    }
}
