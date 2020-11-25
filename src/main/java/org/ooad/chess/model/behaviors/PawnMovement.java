package org.ooad.chess.model.behaviors;

import java.util.ArrayList;
import java.util.List;

public class PawnMovement implements MoveStrategy {
    @Override
    public boolean movePossible(String pre, String post, boolean firstMove, boolean eliminating) {

        //NEED TO IMPLEMENT EN PASSANT AND BOARD FLIP
        int col_diff = Math.abs(pre.charAt(0) - post.charAt(0));
        int row_diff = Math.abs(pre.charAt(1) - post.charAt(1));
        boolean forward_one = (col_diff == 0) && Math.abs(row_diff) == 1;
        boolean forward_two = (col_diff == 0) && Math.abs(row_diff) == 2;
        if(eliminating){
            System.out.println(col_diff);
            System.out.println(row_diff);
            return (col_diff == 1 && row_diff == 1);
        }
        if(firstMove){
            return (forward_one || forward_two);
        }
        else{
            return forward_one;
        }

    }

    @Override
    public List<String> movePath(String pre, String post) {
        List<String> path = new ArrayList<>();

        int pos1 = Math.abs(pre.charAt(0) - post.charAt(0));
        int pos2 = Math.abs(pre.charAt(1) - post.charAt(1));
        int add;

        int index = pre.charAt(1) - '0';

        if (Math.abs(pos2 - pos1) == 2) {
            if (pre.charAt(1) < post.charAt(1)) {
                add = 1;
            }
            else {
                add = -1;
            }
            for (int i = 0; i < 2; i++) {
                path.add(String.format("%s%d", pre.charAt(0), index + (i * add)));
            }
        }
        else {
            path.add(pre);
            path.add(post);
        }

        return path;
    }
}
