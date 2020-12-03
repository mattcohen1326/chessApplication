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
        engine.testHelp("F1");
        engine.testHelp("G1");
        engine.updateMoves("H1");
        //System.out.println(board.getPieces()[48].getType());
        System.out.println(board.getPieces()[7].getAvailableMoves());
        //System.out.println(board.getPiece(new BoardPosition("E8")).getType());
        System.out.println(engine.validCastle("E1","H1"));
        engine.movePiece("H1","E1");
        System.out.println(board.getPiece(new BoardPosition("G1")).getType());
    }
}
