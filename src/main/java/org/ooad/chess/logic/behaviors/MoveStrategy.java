package org.ooad.chess.logic.behaviors;

import java.util.List;

public interface MoveStrategy {

    boolean movePossible(String pre, String post, boolean firstMove, boolean eliminating);

    List<String> movePath(String pre, String post);

}
