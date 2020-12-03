package org.ooad.chess.logic.behaviors;

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
        int pos2 = Math.abs(pre.charAt(0)-post.charAt(0));
        int add;
        if (pre.charAt(0) < post.charAt(0)) {
            add = 1;
        } else {
            add = -1;
        }

        List<String> path = new ArrayList<>();
        if(pos == 0){
            for (int i = 0; i < pos2; i++) {
                path.add(String.format("%s%s", (char) (pre.charAt(0) + (i * add)), pre.charAt(1)));
            }
        }

        path.add(post);
        return path;
    }
}
