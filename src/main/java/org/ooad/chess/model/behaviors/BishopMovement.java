package org.ooad.chess.model.behaviors;

public class BishopMovement implements MoveStrategy {
    @Override
    public boolean movePossible(String pre, String post) {
        int pos1 = Math.abs(pre.charAt(0) - post.charAt(0));
        int pos2 = Math.abs(pre.charAt(1) - post.charAt(1));

        return pos1 == pos2;
    }
}
