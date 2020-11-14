package org.ooad.chess.model.behaviors;

import java.util.List;

public class QueenMovement implements MoveStrategy {
    HorizontalMovement horizontalMovement = new HorizontalMovement();
    VerticalMovement verticalMovement = new VerticalMovement();
    DiagonalMovement diagonalMovement = new DiagonalMovement();

    @Override
    public boolean movePossible(String pre, String post, boolean firstMove, boolean eliminating) {
        return horizontalMovement.movePossible(pre, post, firstMove, eliminating) || verticalMovement.movePossible(pre, post, firstMove, eliminating) || diagonalMovement.movePossible(pre, post, firstMove, eliminating);
    }

    @Override
    public List<String> movePath(String pre, String post) {
        if (horizontalMovement.movePossible(pre, post, false, false)) {
            return horizontalMovement.movePath(pre, post);
        }
        else if (verticalMovement.movePossible(pre, post, false, false)) {
            return verticalMovement.movePath(pre, post);
        }
        else {
            return diagonalMovement.movePath(pre, post);
        }
    }
}
