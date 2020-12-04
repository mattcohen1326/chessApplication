package org.ooad.chess.logic;

import org.ooad.chess.model.Board;
import org.ooad.chess.model.BoardMove;
import org.ooad.chess.model.BoardPosition;
import org.ooad.chess.model.ChessmanColor;
import org.ooad.chess.model.player.Player;

public interface GameController {

    boolean isInCheckmate();

    boolean isInCheck(Player player);

    Player getCurrentPlayer();

    boolean makeMove(BoardMove boardMove);

    Board getBoard();

    Player getWinner();

}
