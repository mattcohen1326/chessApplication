package org.ooad.chess.model.player;

import org.jetbrains.annotations.Nullable;
import org.ooad.chess.model.Board;
import org.ooad.chess.model.BoardMove;

public interface AutoPlayer {

    @Nullable BoardMove computeMove(Board board);

}
