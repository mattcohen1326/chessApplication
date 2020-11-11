package org.ooad.chess.model.behaviors;

public class PawnMovement implements MoveStrategy {
    @Override
    public boolean movePossible(String pre, String post) {
        return pre.charAt(0) == post.charAt(0) && pre.charAt(1) == post.charAt(1) - 1;
    }
}
