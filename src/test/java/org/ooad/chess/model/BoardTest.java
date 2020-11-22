package org.ooad.chess.model;

import org.junit.Assert;
import org.junit.Test;

public class BoardTest {
    @Test
    public void testIndex() {
        Board board = new Board();
        int i = 0;
        for (int row = 1; row <= Board.LENGTH; row++) {
            for (int letterOffset = 0; letterOffset < Board.LENGTH; letterOffset++) {
                char letter = (char) ('H' - letterOffset);
                int actual = board.getIndex(String.format("%s%d", letter, row));
                Assert.assertEquals(i, actual);
                i++;
            }
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testPawnBlockedError() {
        Board board = Board.filledBoard();
        board.movePiece("B1", "C3");
        board.movePiece("C2", "C3");
    }
    @Test(expected = IllegalStateException.class)
    public void testRookBlockedError() {
        Board board = Board.filledBoard();
        board.movePiece("A1", "A3");
    }
    @Test(expected = IllegalStateException.class)
    public void testBishopBlockedError() {
        Board board = Board.filledBoard();
        board.movePiece("C1", "A3");
    }
    @Test(expected = IllegalStateException.class)
    public void testQueenBlockedError() {
        Board board = Board.filledBoard();
        board.movePiece("D1", "B3");
    }
    @Test(expected = IllegalStateException.class)
    public void testKingBlockedError() {
        Board board = Board.filledBoard();
        board.movePiece("E1", "E2");
    }

}
