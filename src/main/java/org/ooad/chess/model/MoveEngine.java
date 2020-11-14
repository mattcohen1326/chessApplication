package org.ooad.chess.model;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MoveEngine {

    MoveEngine(BoardPiece[] pieces, Board board) {
        this.pieces = pieces;
        this.board = board;
    }

    private BoardPiece[] pieces;

    private final Board board;

    private Boolean firstMove = true;

    /**
     * Moves a chess piece from a location to a destination.
     *
     * @param from The source location.
     * @param to   The destination.
     * @throws IllegalStateException if the source location is empty or the destination is full, if the move is invalid given the type, or if the move is blocked by another piece.
     */
    public void movePiece(String from, String to) {
        if (board.hasPiece(to)) {
            throw new IllegalStateException(String.format("Cannot move %s-%s, %s is not empty", from, to, to));
        }
        if (!board.hasPiece(from)) {
            throw new IllegalStateException(String.format("Cannot move %s-%s, %s is empty", from, to, from));
        }

        int fromIndex = board.getIndex(from);
        int toIndex = board.getIndex(to);

        if(!pieces[fromIndex].getMovement().movePossible(from, to, firstMove, isEliminating(from, to))) {
            throw new IllegalStateException(String.format("Cannot move %s-%s, invalid move for type %s", from, to, pieces[fromIndex].getType()));
        }

        List<String> path = pieces[fromIndex].getMovement().movePath(from, to);
        if (isBlocked(path)){
            throw new IllegalStateException(String.format("Cannot move %s-%s, move blocked!", from, to));
        }

        pieces[toIndex] = pieces[fromIndex];
        pieces[fromIndex] = null;

        firstMove = false;
    }

    public boolean isBlocked(List<String> path) {
        BoardPiece current = board.getPiece(path.get(0));

        if (current.getType() != ChessmanTypes.KNIGHT) {
            for (int i = 1; i < path.size() - 1; i++) {
                if (board.getPiece(path.get(i)) == null) {
                    return true;
                }
            }
        }

        if (board.getPiece(path.get(path.size() - 1)) != null) {
            return board.getPiece(path.get(path.size() - 1)).getColor() == current.getColor();
        }

        return false;
    }

    public boolean isEliminating(String pre, String post) {
        BoardPiece current = board.getPiece(pre);
        BoardPiece movingTo = board.getPiece(post);

        if (movingTo == null) {
            return false;
        }
        return current.getColor() != movingTo.getColor();
    }

    public void updateBoard(BoardPiece[] pieces) {
        this.pieces = pieces;
    }

}
