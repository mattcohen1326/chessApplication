package org.ooad.chess.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ooad.chess.logic.MoveEngine;

public class PieceTest {
    private Board board;
    private MoveEngine engine;

    @Before
    public void setup() {
        board = Board.filledBoard();
        engine = new MoveEngine(board);
    }

    @Test
    public void updateMovesTest() {
        engine.updateMoves("A2");
        Assert.assertEquals(true, board.getPieces()[8].getAvailableMoves().get(0).toString().equals("A3"));
        Assert.assertEquals(true, board.getPieces()[8].getAvailableMoves().get(1).toString().equals("A4"));

    }
}
