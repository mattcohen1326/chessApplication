package org.ooad.chess.model.behaviors;

import java.util.ArrayList;
import java.util.List;

public class HorizontalMovement implements MoveStrategy {
    @Override
    public boolean movePossible(String pre, String post, boolean firstMove, boolean eliminating) {
        return pre.charAt(1) == post.charAt(1);
    }

    @Override
    public List<String> movePath(String pre, String post) {
        int pos = Math.abs(pre.charAt(1) - post.charAt(1));

        int add;

        if (pre.charAt(0) < post.charAt(0)) {
            add = 1;
        } else {
            add = -1;
        }

        List<String> path = new ArrayList<>();
        if( pos == 0){
            path.add(pre);
            path.add(post);
        }
        for (int i = 0; i < pos; i++) {
            path.add(String.format("%s%s", (char) (pre.charAt(0) + (i * add)), pre.charAt(1)));
        }

        return path;
    }
}
