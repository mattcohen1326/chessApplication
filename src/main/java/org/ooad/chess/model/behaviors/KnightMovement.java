package org.ooad.chess.model.behaviors;

public class KnightMovement implements MoveStrategy {
    @Override
    public boolean movePossible(String pre, String post) {
        int pos1 = Math.abs(pre.charAt(0) - post.charAt(0));
        int pos2 = Math.abs(pre.charAt(1) - post.charAt(1));

        return (pos1 == 1 && pos2 == 2) || (pos1 == 2 && pos2 == 1);
    }
}
