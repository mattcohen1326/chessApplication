package org.ooad.chess.logic.behaviors;

import java.util.List;

public class RookMovement implements MoveStrategy {
    HorizontalMovement horizontalMovement = new HorizontalMovement();
    VerticalMovement verticalMovement = new VerticalMovement();

    @Override
    public boolean movePossible(String pre, String post, boolean firstMove, boolean eliminating) {
        return horizontalMovement.movePossible(pre, post, firstMove, eliminating) || verticalMovement.movePossible(pre, post, firstMove, eliminating);
    }

    @Override
    public List<String> movePath(String pre, String post) {
        if (horizontalMovement.movePossible(pre, post, false, false)) {
            return horizontalMovement.movePath(pre, post);
        }
        else {
            return verticalMovement.movePath(pre, post);
        }
    }
}
