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

}
