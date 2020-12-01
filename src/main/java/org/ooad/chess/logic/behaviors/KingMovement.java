package org.ooad.chess.logic.behaviors;

import java.util.ArrayList;
import java.util.List;

public class KingMovement implements MoveStrategy {
    @Override
    public boolean movePossible(String pre, String post, boolean firstMove, boolean eliminating) {
        int pos1 = Math.abs(pre.charAt(0) - post.charAt(0));
        int pos2 = Math.abs(pre.charAt(1) - post.charAt(1));

        if ((pre.charAt(0) == post.charAt(0)) || (pre.charAt(1) == post.charAt(1))) {
            return pos1 + pos2 == 1;
        }
        else {
            return pos1 + pos2 == 2;
        }
    }

    @Override
    public List<String> movePath(String pre, String post) {
        List<String> path = new ArrayList<>();

        path.add(pre);
        path.add(post);

        return path;
    }
}
