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


        board.removePiece(new BoardPosition("F8"));
        board.setPiece(new BoardPosition("D8"), new BoardPiece(ROOK, WHITE));
        AIPlayer ai = new AIPlayer(BLACK,GameDifficulty.MEDIUM);
        BoardPiece blackKing = new BoardPiece(QUEEN,BLACK);
        board.setPiece(new BoardPosition("D5"),blackKing);
        engine.updateMoves(blackKing);
        System.out.println(ai.computeMove(board,false));
        System.out.println(blackKing.getAvailableMoves());
        //System.out.println(engine.isBlocked(board.getPiece(new BoardPosition("D8")).getMovement().movePath("D8","E")))
        //System.out.println(board.getPiece(new BoardPosition("E8")).getType());
    }
}
