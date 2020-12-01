package org.ooad.chess.model;

import org.junit.Assert;
import org.junit.Test;

public class BoardLocationTest {

    @Test
    public void testStringLocation() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int rowValue = row + 1;
                char colValue = (char) ('A' + col);
                String locationString = String.format("%c%d", colValue, rowValue);

                BoardPosition position = new BoardPosition(locationString);

                Assert.assertEquals(position + " != " + locationString, row, position.getRow());
                Assert.assertEquals(position + " != " + locationString, col, position.getCol());
            }
        }
    }

}
