package org.ooad.chess.model.behaviors;

public interface MoveStrategy {

    boolean movePossible(String pre, String post);

}
