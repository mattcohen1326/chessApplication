package org.ooad.chess.logic;

import org.ooad.chess.logic.IGameController;
import org.ooad.chess.logic.MoveEngine;
import org.ooad.chess.model.*;
import org.ooad.chess.model.player.AutoPlayer;
import org.ooad.chess.model.player.Player;

import static org.ooad.chess.model.ChessmanColor.WHITE;

public class GameController implements IGameController {

    private final Board board;
    private final MoveEngine engine;
    private final Player white;
    private final Player black;
    private Player currentPlayer;

    public GameController(Player player, Player ai) {
        this.board = Board.filledBoard();
        this.engine = new MoveEngine(board);
        updateAvailableMoves();
        if (player.getColor() == WHITE) {
            currentPlayer = player;
        } else {
            currentPlayer = ai;
        }
        this.white = currentPlayer;
        this.black = currentPlayer == player ? ai : player;
    }

    private void updateAvailableMoves() {
        board.forEach(entry -> {
            if (entry.getPiece() != null) {
                engine.updateMoves(entry.getPosition().toString());
            }
        });
    }

    @Override
    public ChessmanColor isInCheckmate() {
        ChessmanColor kingColor = engine.isInCheck();
        BoardPiece king;
        BoardPosition[] kings = engine.getKings();
        if (kingColor != null) {
            if (kingColor == WHITE) {
                king = board.getPiece(kings[0]);
            } else {
                king = board.getPiece(kings[1]);
            }

            if (king.getAvailableMoves().size() == 0) {
                return king.getColor();
            }
        }

        return null;
    }

    @Override
    public boolean makeMove(BoardMove boardMove) {
        System.out.println("Make move: " + boardMove);
        BoardPiece sourcePiece = board.getPiece(boardMove.getFrom());
        if (sourcePiece != null && sourcePiece.getColor() == currentPlayer.getColor()) {
            BoardPiece targetPiece = board.getPiece(boardMove.getTo());
            if (targetPiece == null || targetPiece.getColor() != currentPlayer.getColor()) {
                engine.movePiece(boardMove.getFrom().toString(), boardMove.getTo().toString());
                nextPlayer();
                return true;
            }
        }
        return false;
    }

    private void nextPlayer() {
        updateAvailableMoves();
        currentPlayer = ((currentPlayer == white) ? black : white);
        if (currentPlayer instanceof AutoPlayer) {
            AutoPlayer player = (AutoPlayer) currentPlayer;
            BoardMove boardMove = player.computeMove(board);
            makeMove(boardMove); // TODO: what if there are no available moves?
        }
    }

    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public Board getBoard() {
        return board;
    }
}
