package org.ooad.chess.logic;

import org.ooad.chess.model.Board;
import org.ooad.chess.model.BoardMove;
import org.ooad.chess.model.BoardPiece;
import org.ooad.chess.model.player.AutoPlayer;
import org.ooad.chess.model.player.Player;

import static org.ooad.chess.model.ChessmanColor.WHITE;

public class GameControllerImpl implements GameController {

    private final Board board;
    private final MoveEngine engine;
    private final Player white;
    private final Player black;
    private Player currentPlayer;

    public GameControllerImpl(Player player, Player ai) {
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
    public boolean isInCheckmate() {
        return isInCheckmate(white) || isInCheckmate(black);
    }

    public boolean isInCheckmate(Player player) {
        return engine.isInCheckmate() == player.getColor();
    }

    @Override
    public boolean isInCheck(Player player) {
        return engine.isInCheck() == player.getColor();
    }

    @Override
    public boolean makeMove(BoardMove boardMove) {
        System.out.println("Before FEN: " + board.toFen());
        System.out.println("Make move: " + currentPlayer.getColor() + " " + boardMove);
        BoardPiece sourcePiece = board.getPiece(boardMove.getFrom());
        if (sourcePiece != null && sourcePiece.getColor() == currentPlayer.getColor()) {
            engine.updateMoves(sourcePiece);
            if (sourcePiece.getAvailableMoves().contains(boardMove.getTo())) {
                engine.movePiece(boardMove.getFrom().toString(), boardMove.getTo().toString());
                System.out.println("After FEN: " + board.toFen());
                nextPlayer();
                return true;
            }
        }
        System.out.println("After FEN: " + board.toFen());
        return false;
    }

    private void nextPlayer() {
        updateAvailableMoves();
        currentPlayer = ((currentPlayer == white) ? black : white);
        if (!isInCheckmate() && currentPlayer instanceof AutoPlayer) {
            AutoPlayer player = (AutoPlayer) currentPlayer;
            BoardMove boardMove = player.computeMove(board, isInCheck(currentPlayer));
            makeMove(boardMove);
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
