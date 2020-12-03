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

        engine.movePiece("A2","A4");
        System.out.println(board.getPiece(new BoardPosition("A4")).getFirst());
        //System.out.println(board.getPiece(new BoardPosition("E8")).getType());
    }
}
