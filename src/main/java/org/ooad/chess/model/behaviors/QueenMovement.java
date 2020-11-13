package org.ooad.chess.model.behaviors;

import java.util.ArrayList;
import java.util.List;

public class QueenMovement implements MoveStrategy {
    @Override
    public boolean movePossible(String pre, String post, boolean firstMove, boolean eliminating) {
        int pos1 = Math.abs(pre.charAt(0) - post.charAt(0));
        int pos2 = Math.abs(pre.charAt(1) - post.charAt(1));

        if (pos1 == pos2) {
            return true;
        }

        return pre.charAt(0) == post.charAt(0) || pre.charAt(1) == post.charAt(1);
    }

    @Override
    public List<String> movePath(String pre, String post) {
        int pos1 = Math.abs(pre.charAt(0) - post.charAt(0));
        int pos2 = Math.abs(pre.charAt(1) - post.charAt(1));

        int add1, add2;
        int index = pre.charAt(1) - '0';

        if (pos1 == pos2) {
            if (pre.charAt(0) < post.charAt(0)) {
                add1 = 1;
            }
            else {
                add1 = -1;
            }

            if (pre.charAt(1) < post.charAt(1)) {
                add2 = 1;
            }
            else {
                add2 = -1;
            }
        }
        else {
            if (pre.charAt(0) < post.charAt(0) || pre.charAt(1) < post.charAt(1)) {
                if (pre.charAt(0) == post.charAt(0)) {
                    add1 = 0;
                    add2 = 1;
                } else {
                    add1 = 1;
                    add2 = 0;
                }
            } else {
                if (pre.charAt(0) == post.charAt(0)) {
                    add1 = 0;
                    add2 = -1;
                } else {
                    add1 = -1;
                    add2 = 0;
                }
            }
        }

        List<String> path = new ArrayList<>();

        for (int i = 0; i < Math.abs(post.charAt(0) - pre.charAt(0)); i++) {
            path.add(String.format("%s%s", (char) (pre.charAt(0) + (i * add1)), index + (i * add2)));
        }

        return path;
    }
}
