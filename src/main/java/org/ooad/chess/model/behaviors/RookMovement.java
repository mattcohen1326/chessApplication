package org.ooad.chess.model.behaviors;

public class RookMovement implements MoveStrategy {
    @Override
    public boolean movePossible(String pre, String post) {
        if (pre.charAt(0) == post.charAt(0) || pre.charAt(1) == post.charAt(1)) {
            return !pre.equals(post);
        }
        return false;
    }
}
