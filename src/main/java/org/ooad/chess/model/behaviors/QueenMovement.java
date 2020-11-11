package org.ooad.chess.model.behaviors;

public class QueenMovement implements MoveStrategy {
    @Override
    public boolean movePossible(String pre, String post) {
        int pos1 = Math.abs(pre.charAt(0) - post.charAt(0));
        int pos2 = Math.abs(pre.charAt(1) - post.charAt(1));

        if (pos1 == pos2) {
            return true;
        }

        if (pre.charAt(0) == post.charAt(0) || pre.charAt(1) == post.charAt(1)) {
            return !pre.equals(post);
        }
        return false;
    }
}
