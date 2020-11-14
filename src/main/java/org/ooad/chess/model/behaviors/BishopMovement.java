package org.ooad.chess.model.behaviors;

import java.util.List;

public class BishopMovement implements MoveStrategy {
    DiagonalMovement diagonalMovement = new DiagonalMovement();
    @Override
    public boolean movePossible(String pre, String post, boolean firstMove, boolean eliminating) {
        return diagonalMovement.movePossible(pre, post, firstMove, eliminating);
    }

    @Override
    public List<String> movePath(String pre, String post) {
        return diagonalMovement.movePath(pre, post);
    }
}
