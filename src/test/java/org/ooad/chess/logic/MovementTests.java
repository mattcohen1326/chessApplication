package org.ooad.chess.logic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ooad.chess.logic.players.AIPlayer;
import org.ooad.chess.logic.players.HumanPlayer;
import org.ooad.chess.model.Board;
import org.ooad.chess.model.BoardMove;
import org.ooad.chess.model.BoardPiece;
import org.ooad.chess.model.BoardPosition;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.ooad.chess.model.Board.LENGTH;
import static org.ooad.chess.model.ChessmanColor.BLACK;
import static org.ooad.chess.model.ChessmanColor.WHITE;
import static org.ooad.chess.model.ChessmanTypes.*;
import static org.ooad.chess.model.GameDifficulty.EASY;
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
        GameController gameController = new GameControllerImpl(player, aiPlayer);
        board = gameController.getBoard();


        gameController.makeMove(new BoardMove(pos("E2"), pos("E4")));

        assertNull(board.getPiece(pos("E2")));
        assertNull(board.getPiece(pos("E3")));
        assertNull(board.getPiece(pos("D3")));
        assertNull(board.getPiece(pos("F3")));
        board.print();
    }

    @Test
    public void testCheckMovements() {
        // When in check, what moves are possible?
        BoardPiece king = new BoardPiece(KING, WHITE);
        board.setPiece(pos("A1"), king);
        board.setPiece(pos("A7"), new BoardPiece(ROOK, BLACK));
        engine.updateMoves(king);

        Set<BoardPosition> expectedMoves = Set.of(pos("B1"), pos("B2"));
        assertEquals(expectedMoves, Set.copyOf(king.getAvailableMoves()));
    }

    @Test
    public void testMoveIntoCheck() {
        // When not in check, do not move into check.
        BoardPiece king = new BoardPiece(KING, WHITE);
        board.setPiece(pos("A1"), king);
        board.setPiece(pos("B7"), new BoardPiece(ROOK, BLACK));
        engine.updateMoves(king);

        Set<BoardPosition> expectedMoves = Set.of(pos("A1"));
        assertEquals(expectedMoves, Set.copyOf(king.getAvailableMoves()));
    }

    @Test
    public void testPawnCaptureFirstMove() {
        // Pawn capture on first move
        BoardPiece pawn = new BoardPiece(PAWN, WHITE);
        board.setPiece(pos("C2"), pawn);
        board.setPiece(pos("D3"), new BoardPiece(PAWN, BLACK));
        engine.updateMoves(pawn);

        Set<BoardPosition> expectedMoves = Set.of(pos("C3"), pos("C4"), pos("D3"));
        assertEquals(expectedMoves, Set.copyOf(pawn.getAvailableMoves()));
    }

    @Test
    public void testPawnMove_idk2() {
        // Pawns move around pieces?
        BoardPiece pawn = new BoardPiece(PAWN, BLACK);
        board.setPiece(pos("B4"), pawn);
        board.setPiece(pos("B3"), new BoardPiece(QUEEN, WHITE));
        engine.updateMoves(pawn);

        Set<BoardPosition> expectedMoves = Set.of();
        assertEquals(expectedMoves, Set.copyOf(pawn.getAvailableMoves()));
    }

    @Test
    public void testPawnFirstMoveBlock() {
        BoardPiece whitePawn = new BoardPiece(PAWN, WHITE);
        board.setPiece(pos("A2"), whitePawn);
        board.setPiece(pos("A4"), new BoardPiece(PAWN, BLACK));
        engine.updateMoves(whitePawn);

        Set<BoardPosition> expectedMoves = Set.of(pos("A3"));
        assertEquals(expectedMoves, Set.copyOf(whitePawn.getAvailableMoves()));
    }

    @Test
    public void testRookObstructed() {
        BoardPiece whiteRook = new BoardPiece(ROOK, WHITE);
        board.setPiece(pos("A1"), whiteRook);
        board.setPiece(pos("A2"), new BoardPiece(PAWN, BLACK));
        board.setPiece(pos("B1"), new BoardPiece(PAWN, BLACK));
        engine.updateMoves(whiteRook);

        Set<BoardPosition> expectedMoves = Set.of(pos("A2"), pos("B1"));
        assertEquals(expectedMoves, Set.copyOf(whiteRook.getAvailableMoves()));
    }

    @Test
    public void testCheckCapture() {
        board = Board.filledBoard();
        engine = new MoveEngine(board);

        board.setPiece(pos("E7"), new BoardPiece(QUEEN, WHITE));

        BoardPiece blackKing = board.getKing(WHITE);
        engine.updateMoves(blackKing);

        Set<BoardPosition> actualMoves = new HashSet<>(blackKing.getAvailableMoves());
        Set<BoardPosition> expectedMoves = Set.of(new BoardPosition("E7"));
        assertEquals(expectedMoves, actualMoves);
        assertNull(engine.isInCheck());
    }
}
