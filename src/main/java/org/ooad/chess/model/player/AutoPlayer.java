package org.ooad.chess.model.player;

import org.ooad.chess.model.Board;
import org.ooad.chess.model.BoardMove;

public interface AutoPlayer {

    BoardMove computeMove(Board board, boolean inCheck);

}
