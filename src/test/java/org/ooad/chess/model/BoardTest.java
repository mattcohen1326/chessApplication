package org.ooad.chess.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.ooad.chess.model.ChessmanColor.*;
import static org.ooad.chess.model.ChessmanTypes.*;
import static org.ooad.chess.util.TestUtils.pos;

public class BoardTest {
    @Test
    public void testFen() {
        String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
        Board board = Board.filledBoard();
        assertEquals(fen, board.toFen());
    }

    @Test
    public void testFenSpace() {
        String fen = "rnbqkbnr/pppppppp/8/8/8/1R6/PPPPPPPP/RNBQKBNR";
        Board board = Board.filledBoard();
        board.setPiece(pos("B3"), new BoardPiece(ROOK, WHITE));
        assertEquals(fen, board.toFen());
    }

    @Test
    public void testFenLoad() {
        String fen = "rnbqkbnr/pppppppp/8/8/8/1R6/PPPPPPPP/RNBQKBNR";

        Board expected = Board.filledBoard();
        expected.setPiece(pos("B3"), new BoardPiece(ROOK, WHITE));

        Board actual = Board.fromFen(fen);
        assertEquals(expected.toFen(), actual.toFen());
    }
}
