package org.ooad.chess.logic;

import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ooad.chess.logic.players.AIPlayer;
import org.ooad.chess.logic.players.Player;
import org.ooad.chess.logic.players.humanPlayer;
import org.ooad.chess.model.*;

public class EngineTest {

    private Board board;
    private MoveEngine engine;

    @Before
    public void setup() {
        board = Board.filledBoard();
        engine = new MoveEngine(board);
    }

    @Test(expected = IllegalStateException.class)
    public void testPawnBlockedError() {
        engine.movePiece("B1", "C3");
        engine.movePiece("C2", "C3");
    }

    @Test(expected = IllegalStateException.class)
    public void testRookBlockedError() {
        engine.movePiece("A1", "A3");
    }

    @Test(expected = IllegalStateException.class)
    public void testBishopBlockedError() {
        engine.movePiece("C1", "A3");
    }

    @Test(expected = IllegalStateException.class)
    public void testQueenBlockedError() {
        engine.movePiece("D1", "B3");
    }

    @Test(expected = IllegalStateException.class)
    public void testKingBlockedError() {
        engine.movePiece("E1", "E2");
    }

    @Test
    public void testPawnForwardOne() {
        for (int i = 1; i <= board.LENGTH; i++) {
            Assert.assertEquals(false, engine.isBlocked(getPiece(engine.stringifyMove(2, i)).getMovement().movePath(engine.stringifyMove(2, i), engine.stringifyMove(3, i))));
            Assert.assertEquals(true, getPiece(engine.stringifyMove(2, i)).getMovement().movePossible(engine.stringifyMove(2, i), engine.stringifyMove(3, i), true, false));
            Assert.assertEquals(false, engine.isBlocked(getPiece(engine.stringifyMove(board.LENGTH - 1, i)).getMovement().movePath(engine.stringifyMove(board.LENGTH - 1, i), engine.stringifyMove(board.LENGTH - 2, i))));
            Assert.assertEquals(true, getPiece(engine.stringifyMove(board.LENGTH - 1, i)).getMovement().movePossible(engine.stringifyMove(board.LENGTH - 1, i), engine.stringifyMove(board.LENGTH - 2, i), true, false));
        }
    }

    @Test
    public void testEnPassant() {
        engine.movePiece("B2", "B3");
        engine.movePiece("B3", "B4");
        engine.movePiece("B4", "B5");
        engine.movePiece("A7", "A5");
        Assert.assertEquals(true, engine.isEliminating("B5", "A6"));
        engine.movePiece("G7", "G5");
        engine.movePiece("G5", "G4");
        engine.movePiece("F2", "F4");
        Assert.assertEquals(true, engine.isEliminating("G4", "F3"));

    }

    @Test
    public void checkPawnForwardTwo() {
        for (int i = 1; i <= board.LENGTH; i++) {
            Assert.assertEquals(false, engine.isBlocked(getPiece(engine.stringifyMove(2, i)).getMovement().movePath(engine.stringifyMove(2, i), engine.stringifyMove(4, i))));
            Assert.assertEquals(true, getPiece(engine.stringifyMove(2, i)).getMovement().movePossible(engine.stringifyMove(2, i), engine.stringifyMove(4, i), true, false));
            Assert.assertEquals(false, engine.isBlocked(getPiece(engine.stringifyMove(board.LENGTH - 1, i)).getMovement().movePath(engine.stringifyMove(board.LENGTH - 1, i), engine.stringifyMove(board.LENGTH - 3, i))));
            Assert.assertEquals(true, getPiece(engine.stringifyMove(board.LENGTH - 1, i)).getMovement().movePossible(engine.stringifyMove(board.LENGTH - 1, i), engine.stringifyMove(board.LENGTH - 3, i), true, false));
        }
    }

    @Test
    public void checkPawnMoves() {
        board = new Board();
        engine = new MoveEngine(board);
        BoardPiece p = new BoardPiece(ChessmanTypes.PAWN, ChessmanColor.WHITE);
        setPiece("D3", p);
        BoardPiece enemy = new BoardPiece(ChessmanTypes.PAWN, ChessmanColor.BLACK);
        setPiece("D4", enemy);
        setPiece("C4", enemy);
        setPiece("E4", enemy);
        Assert.assertEquals(true, engine.isBlocked(p.getMovement().movePath("D3", "D4")));
        Assert.assertEquals(false, engine.isBlocked(p.getMovement().movePath("D3", "C4")));
        Assert.assertEquals(false, engine.isBlocked(p.getMovement().movePath("D3", "E4")));
        Assert.assertEquals(true, p.getMovement().movePossible("D3", "C4", false, engine.isEliminating("D3", "C4")));
    }

    @Test
    public void checkRookMoves() {
        board = new Board();
        engine = new MoveEngine(board);
        BoardPiece r = new BoardPiece(ChessmanTypes.ROOK, ChessmanColor.WHITE);
        setPiece("A1", r);
        Assert.assertEquals(true, r.getMovement().movePossible("A1", "A8", true, false));
        BoardPiece teammate = new BoardPiece(ChessmanTypes.PAWN, ChessmanColor.WHITE);
        BoardPiece enemy = new BoardPiece(ChessmanTypes.PAWN, ChessmanColor.BLACK);
        setPiece("A5", teammate);
        setPiece("B1", enemy);
        Assert.assertEquals(true, engine.isBlocked(r.getMovement().movePath("A1", "A8")));
        Assert.assertEquals(false, engine.isBlocked(r.getMovement().movePath("A1", "B1")));
        Assert.assertEquals(true, engine.isEliminating("A1", "B1"));

    }

    @Test
    public void checkBishopMoves() {
        board = new Board();
        engine = new MoveEngine(board);
        BoardPiece bishop = new BoardPiece(ChessmanTypes.BISHOP, ChessmanColor.WHITE);
        setPiece("D4", bishop);
        BoardPiece enemy = new BoardPiece(ChessmanTypes.BISHOP, ChessmanColor.BLACK);
        setPiece("E5", enemy);
        Assert.assertEquals(false, bishop.getMovement().movePossible("D4", "D5", false, false));
        Assert.assertEquals(true, bishop.getMovement().movePossible("D4", "F6", false, false));
        Assert.assertEquals(true, bishop.getMovement().movePossible("D4", "A1", false, false));
        Assert.assertEquals(false, bishop.getMovement().movePossible("D4", "A8", false, false));
        Assert.assertEquals(true, engine.isBlocked(bishop.getMovement().movePath("D4", "F6")));
    }

    @Test
    public void checkKnightMoves() {
        board = new Board();
        engine = new MoveEngine(board);
        BoardPiece knight = new BoardPiece(ChessmanTypes.KNIGHT, ChessmanColor.WHITE);
        BoardPiece toEliminate = new BoardPiece(ChessmanTypes.PAWN, ChessmanColor.BLACK);
        setPiece("E6", toEliminate);
        setPiece("D4", knight);

        Assert.assertTrue(knight.getMovement().movePossible("D4", "E6", false, false));
        Assert.assertTrue(knight.getMovement().movePossible("D4", "F5", false, false));
        Assert.assertTrue(knight.getMovement().movePossible("D4", "C6", false, false));
        Assert.assertTrue(knight.getMovement().movePossible("D4", "B5", false, false));
        Assert.assertTrue(knight.getMovement().movePossible("D4", "E2", false, false));
        Assert.assertTrue(knight.getMovement().movePossible("D4", "F3", false, false));
        Assert.assertTrue(knight.getMovement().movePossible("D4", "C2", false, false));
        Assert.assertTrue(knight.getMovement().movePossible("D4", "B3", false, false));
        Assert.assertFalse(knight.getMovement().movePossible("D4", "A6", false, false));
    }

    @Test
    public void checkQueenMoves() {
        board = new Board();
        engine = new MoveEngine(board);
        BoardPiece queen = new BoardPiece(ChessmanTypes.QUEEN, ChessmanColor.WHITE);
        BoardPiece toEliminate = new BoardPiece(ChessmanTypes.PAWN, ChessmanColor.BLACK);
        setPiece("E5", toEliminate);
        setPiece("D4", queen);

        Assert.assertTrue(queen.getMovement().movePossible("D4", "E5", false, false));
        Assert.assertTrue(queen.getMovement().movePossible("D4", "C5", false, false));
        Assert.assertTrue(queen.getMovement().movePossible("D4", "D5", false, false));
        Assert.assertTrue(queen.getMovement().movePossible("D4", "E3", false, false));
        Assert.assertTrue(queen.getMovement().movePossible("D4", "C3", false, false));
        Assert.assertTrue(queen.getMovement().movePossible("D4", "D3", false, false));
        Assert.assertFalse(queen.getMovement().movePossible("D4", "E1", false, false));
    }

    @Test
    public void checkKingMoves() {
        board = new Board();
        engine = new MoveEngine(board);

        BoardPiece king = new BoardPiece(ChessmanTypes.KING, ChessmanColor.WHITE);
        BoardPiece toEliminate = new BoardPiece(ChessmanTypes.PAWN, ChessmanColor.BLACK);
        setPiece("E5", toEliminate);
        setPiece("D4", king);

        Assert.assertTrue(king.getMovement().movePossible("D4", "E5", false, false));
        Assert.assertTrue(king.getMovement().movePossible("D4", "C5", false, false));
        Assert.assertTrue(king.getMovement().movePossible("D4", "D5", false, false));
        Assert.assertTrue(king.getMovement().movePossible("D4", "E3", false, false));
        Assert.assertTrue(king.getMovement().movePossible("D4", "C3", false, false));
        Assert.assertTrue(king.getMovement().movePossible("D4", "D3", false, false));
        Assert.assertFalse(king.getMovement().movePossible("D4", "E1", false, false));
    }

    @Test
    public void checkCastling() {
        board.removePiece(new BoardPosition("C8"));
        board.removePiece(new BoardPosition("B8"));
        board.removePiece(new BoardPosition("B1"));
        board.removePiece(new BoardPosition("C1"));
        board.removePiece(new BoardPosition("D1"));
        Assert.assertEquals(true, engine.validCastle("D8", "A8"));
        //Assert.assertEquals(true,engine.validCastle("E1","A1"));
        Assert.assertEquals(false,engine.validCastle("E1","H1"));
    }

    // TODO remove
    private void setPiece(String location, BoardPiece piece) {
        board.setPiece(new BoardPosition(location), piece);
    }

    private @Nullable BoardPiece getPiece(String location) {
        return board.getPiece(new BoardPosition(location));
    }

    @Test
    public void checkCheckMethod() {
        board = new Board();
        engine = new MoveEngine(board);

        BoardPiece king1 = new BoardPiece(ChessmanTypes.KING, ChessmanColor.WHITE);
        BoardPiece king2 = new BoardPiece(ChessmanTypes.KING, ChessmanColor.BLACK);
        BoardPiece bishop = new BoardPiece(ChessmanTypes.BISHOP, ChessmanColor.BLACK);

        setPiece("A3", king1);
        setPiece("A8", king2);
        setPiece("D4", bishop);

        Assert.assertTrue(engine.isInCheck("C5", "D4"));
        Assert.assertFalse(engine.isInCheck("E3", "D4"));
    }

    @Test
    public void checkMateTest() {
        board = new Board();
        engine = new MoveEngine(board);
        AIPlayer a = null;
        humanPlayer b = null;
        gameController controller = new gameController(b, a);

        BoardPiece king1 = new BoardPiece(ChessmanTypes.KING, ChessmanColor.WHITE);
        BoardPiece king2 = new BoardPiece(ChessmanTypes.KING, ChessmanColor.BLACK);
        BoardPiece rook1 = new BoardPiece(ChessmanTypes.ROOK, ChessmanColor.BLACK);
        BoardPiece rook2 = new BoardPiece(ChessmanTypes.ROOK, ChessmanColor.BLACK);

        setPiece("A1", king1);
        setPiece("A8", king2);
        setPiece("A3", rook1);
        setPiece("B3", rook2);

        Assert.assertTrue(controller.isInCheckmate());
    }
}
