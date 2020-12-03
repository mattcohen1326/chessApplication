package org.ooad.chess.logic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ooad.chess.logic.players.AIPlayer;
import org.ooad.chess.logic.players.HumanPlayer;
import org.ooad.chess.model.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;
import static org.ooad.chess.model.Board.LENGTH;
import static org.ooad.chess.model.ChessmanColor.BLACK;
import static org.ooad.chess.model.ChessmanColor.WHITE;
import static org.ooad.chess.model.ChessmanTypes.PAWN;
import static org.ooad.chess.model.ChessmanTypes.ROOK;
import static org.ooad.chess.model.GameDifficulty.*;
import static org.ooad.chess.util.TestUtils.pos;

public class MovementTests {

    private Board board;
    private MoveEngine engine;

    @Before
    public void setup() {
        board = new Board();
        engine = new MoveEngine(board);
    }

    @Test
    public void testStartingPawnMoves() {
        // White
        for (int col = 0; col < LENGTH; col++) {
            board = new Board();
            engine = new MoveEngine(board);

            BoardPosition position = new BoardPosition(1, col);
            BoardPiece piece = new BoardPiece(PAWN, WHITE);
            board.setPiece(position, piece);
            engine.updateMoves(position.toString());
            List<BoardPosition> actualMoves = piece.getAvailableMoves();
            List<BoardPosition> expectedMoves = Arrays.asList(position.relativeTo(1, 0), position.relativeTo(2, 0));
            assertEquals("Moves at " + position + " not equal", expectedMoves, actualMoves);
        }

        // Black
        for (int col = 0; col < LENGTH; col++) {
            board = new Board();
            engine = new MoveEngine(board);

            BoardPosition position = new BoardPosition(6, col);
            BoardPiece piece = new BoardPiece(PAWN, BLACK);
            board.setPiece(position, piece);
            engine.updateMoves(position.toString());
            List<BoardPosition> actualMoves = piece.getAvailableMoves();
            List<BoardPosition> expectedMoves = Arrays.asList(position.relativeTo(-1, 0), position.relativeTo(-2, 0));
            assertEquals("Moves at " + position + " not equal", new HashSet<>(expectedMoves), new HashSet<>(actualMoves));
        }
    }

    @Test
    public void testCapture() {
        BoardPosition sourceLocation = pos("A1");
        BoardPosition targetLocation = pos("A7");
        BoardPiece whiteRook = new BoardPiece(ROOK, WHITE);
        board.setPiece(sourceLocation, whiteRook);
        board.setPiece(targetLocation, new BoardPiece(PAWN, BLACK));

        engine.movePiece(sourceLocation.toString(), targetLocation.toString());

        assertNull(board.getPiece(sourceLocation));
        assertEquals(whiteRook, board.getPiece(targetLocation));
    }

    @Test(expected = IllegalStateException.class)
    public void testCollide() {
        BoardPosition sourceLocation = pos("A1");
        BoardPosition targetLocation = pos("A7");
        BoardPiece whiteRook = new BoardPiece(ROOK, WHITE);
        board.setPiece(sourceLocation, whiteRook);
        board.setPiece(targetLocation, new BoardPiece(PAWN, WHITE));

        engine.movePiece(sourceLocation.toString(), targetLocation.toString());
    }

    @Test
    public void testPawnMove_idk() {
        HumanPlayer player = new HumanPlayer(WHITE);
        AIPlayer aiPlayer = new AIPlayer(BLACK, EASY);
        IGameController gameController = new GameController(player, aiPlayer);
        board = gameController.getBoard();

        gameController.makeMove(new BoardMove(pos("E2"), pos("E4")));

        assertNull(board.getPiece(pos("E2")));
        assertNull(board.getPiece(pos("E3")));
        assertNull(board.getPiece(pos("D3")));
        assertNull(board.getPiece(pos("F3")));
        board.print();
    }
}
