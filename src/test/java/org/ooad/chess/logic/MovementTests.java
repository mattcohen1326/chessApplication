package org.ooad.chess.logic;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ooad.chess.logic.players.AIPlayer;
import org.ooad.chess.logic.players.HumanPlayer;
import org.ooad.chess.model.Board;
import org.ooad.chess.model.BoardMove;
import org.ooad.chess.model.BoardPiece;
import org.ooad.chess.model.BoardPosition;

import java.util.*;

import static org.junit.Assert.*;
import static org.ooad.chess.model.Board.LENGTH;
import static org.ooad.chess.model.ChessmanColor.BLACK;
import static org.ooad.chess.model.ChessmanColor.WHITE;
import static org.ooad.chess.model.ChessmanTypes.*;
import static org.ooad.chess.model.GameDifficulty.EASY;
import static org.ooad.chess.util.TestUtils.pos;

public class
MovementTests {

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
    public void testPawnMovesinGeneral() {
        board = Board.filledBoard();
        engine = new MoveEngine(board);
        engine.movePiece("A2", "A4");
        Assert.assertEquals(false, board.getPiece(new BoardPosition("A4")).getFirst());
        Assert.assertEquals(true, board.getPiece(new BoardPosition("A4")).getEnp());
        engine.movePiece("B2", "B4");
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

        Set<BoardPosition> expectedMoves = Set.of(pos("A2"));
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

        BoardPiece blackKing = board.getKing(BLACK);
        engine.updateMoves(blackKing);

        Set<BoardPosition> actualMoves = new HashSet<>(blackKing.getAvailableMoves());
        Set<BoardPosition> expectedMoves = Set.of(new BoardPosition("E7"));
        assertEquals(expectedMoves, actualMoves);
        assertEquals(BLACK, engine.isInCheck());
        assertNull(engine.isInCheckmate());

        // cannot capture into check
        board.setPiece(pos("E3"), new BoardPiece(ROOK, WHITE));
        engine.updateMoves(blackKing);
        assertEquals(Collections.emptyList(), blackKing.getAvailableMoves());
        assertEquals(BLACK, engine.isInCheckmate());
    }

    @Test
    public void reversePawnBlock() {
        BoardPiece whiteKing = new BoardPiece(KING, WHITE);
        board.setPiece(pos("C4"), whiteKing);
        board.setPiece(pos("A3"), new BoardPiece(PAWN, BLACK));
        engine.updateMoves(whiteKing);

        assertTrue(whiteKing.getAvailableMoves().contains(pos("B4")));
    }

    @Test
    public void exposeKingCheck() {
        board.setPiece(pos("A1"), new BoardPiece(KING, WHITE));
        board.setPiece(pos("B2"), new BoardPiece(KING, WHITE));
        BoardPiece whiteRook = new BoardPiece(ROOK, WHITE);
        board.setPiece(pos("B1"), whiteRook);
        board.setPiece(pos("C1"), new BoardPiece(QUEEN, BLACK));

        engine.updateMoves(whiteRook);

        Set<BoardPosition> expected = Set.of(pos("C1"));
        assertEquals(expected, new HashSet<>(whiteRook.getAvailableMoves()));
    }

    @Test
    public void pawnBackwardsCapture() {
        BoardPiece pawn = new BoardPiece(PAWN, WHITE);
        board.setPiece(pos("A5"), pawn);
        board.setPiece(pos("B4"), new BoardPiece(BISHOP, BLACK));

        engine.updateMoves(pawn);

        assertFalse(pawn.getAvailableMoves().contains(pos("B4")));

        BoardPiece blackPawn = new BoardPiece(PAWN, BLACK);
        board.setPiece(pos("E4"), blackPawn);
        board.setPiece(pos("D5"), new BoardPiece(BISHOP, WHITE));

        engine.updateMoves(blackPawn);

        assertFalse(blackPawn.getAvailableMoves().contains(pos("D5")));
    }

    @Test
    public void testMoveKingIntoCheckmate() {
        board = Board.filledBoard();
        engine = new MoveEngine(board);

        board.removePiece(pos("F8"));
        board.setPiece(pos("D8"), new BoardPiece(ROOK, WHITE));

        BoardPiece blackKing = board.getKing(BLACK);
        engine.updateMoves(blackKing);

        Set<BoardPosition> expected = Set.of(pos("D8"));
        assertEquals(expected, new HashSet<>(blackKing.getAvailableMoves()));
    }

    @Test
    public void testMoveProtectingPiece() {
        loadFen("rnb1kbnr/1p1ppppp/P7/2p5/2P5/B7/1p1PPPPP/q2QKBNR");

        BoardPiece whiteQueen = board.getPiece(pos("D1"));
        engine.updateMoves(whiteQueen);

        assertFalse(whiteQueen.getAvailableMoves().contains(pos("C2"))); // cannot expose king to check
    }

    @Test
    public void idk() {
        loadFen("rnb5/pp1p3r/1qPkppp1/P2n2b1/2P1BB2/3Q3p/1PKRPP1P/6NR");

        BoardPiece blackKing = board.getKing(BLACK);
        engine.updateMoves(blackKing);

        assertTrue(blackKing.getAvailableMoves().contains(pos("C6"))); // cannot expose king to check
    }

    @Test
    public void kingAttackIntoCheck() {
        loadFen("rnbq1bnr/ppppp1p1/8/4Qp1p/PPk5/3P4/4PPPP/RNB1KBNR");
        BoardPiece blackKing = board.getKing(BLACK);
        engine.updateMoves(blackKing);

        assertFalse(blackKing.getAvailableMoves().contains(pos("D3"))); // cannot expose king to check
    }

    @Test
    public void kingMoveIntoCheck() {
        loadFen("rnbq1bnr/ppppp1p1/8/4Qp1p/PP3P2/2NP4/2k1P1PP/R1B1KBNR");
        BoardPiece blackKing = board.getKing(BLACK);
        engine.updateMoves(blackKing);

        assertFalse(blackKing.getAvailableMoves().contains(pos("D1"))); // cannot expose king to check
    }

    @After
    public void after() {
        board.print();
    }

    private void loadFen(String fen) {
        this.board = Board.fromFen(fen);
        this.engine = new MoveEngine(board);
    }
}
