package org.ooad.chess.logic.behaviors;

import java.util.ArrayList;
import java.util.List;

public class VerticalMovement implements MoveStrategy {
    @Override
    public boolean movePossible(String pre, String post, boolean firstMove, boolean eliminating) {
        return pre.charAt(0) == post.charAt(0);
    }

    @Override
    public List<String> movePath(String pre, String post) {
        int pos = Math.abs(pre.charAt(1) - post.charAt(1));

        int add;
        int index = pre.charAt(1) - '0';

        if (pre.charAt(1) < post.charAt(1)) {
            add = 1;
        } else {
            add = -1;
        }

        List<String> path = new ArrayList<>();

        for (int i = 0; i < pos; i++) {
            path.add(String.format("%s%s", pre.charAt(0), index + (i * add)));
        }

        return path;
    }
}
