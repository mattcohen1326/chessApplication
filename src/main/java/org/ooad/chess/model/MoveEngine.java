package org.ooad.chess.model;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import static org.ooad.chess.model.ChessmanColor.BLACK;
import static org.ooad.chess.model.ChessmanColor.WHITE;
import static org.ooad.chess.model.ChessmanTypes.*;
import static org.ooad.chess.model.ChessmanTypes.PAWN;

public class MoveEngine {

    MoveEngine(Board board) {
        this.pieces = board.getPieces();
        this.board = board;
    }

    private BoardPiece[] pieces;

    private final Board board;

    private Boolean firstMove = true;

    /**
     * Sets a piece at a location.
     *
     * @param location The location of the target piece expressed in Chess coords. I.e.:A1,B6.
     * @param piece    The piece to be set.
     */
    public void setPiece(String location, BoardPiece piece) {
        pieces[board.getIndex(location)] = piece;
        board.update(pieces);
    }

    /**
     * Removes a piece at a location.
     *
     * @param location The location to be removed.
     * @throws IllegalStateException if the location is empty.
     */
    public void removePiece(String location) {
        if (!board.hasPiece(location)) {
            throw new IllegalStateException(String.format("Cannot remove %s, location is empty", location));
        }
        pieces[board.getIndex(location)] = null;
    }

    /**
     * Moves a chess piece from a location to a destination.
     *
     * @param from The source location.
     * @param to   The destination.
     * @throws IllegalStateException if the source location is empty or the destination is full, if the move is invalid given the type, or if the move is blocked by another piece.
     */
    //Probably need to return the board
    public void movePiece(String from, String to) {
        if (board.hasPiece(to)) {
            throw new IllegalStateException(String.format("Cannot move %s-%s, %s is not empty", from, to, to));
        }
        if (!board.hasPiece(from)) {
            throw new IllegalStateException(String.format("Cannot move %s-%s, %s is empty", from, to, from));
        }

        int fromIndex = board.getIndex(from);
        int toIndex = board.getIndex(to);

        if(!pieces[fromIndex].getMovement().movePossible(from, to, firstMove, isEliminating(from, to,false))) {
            throw new IllegalStateException(String.format("Cannot move %s-%s, invalid move for type %s", from, to, pieces[fromIndex].getType()));
        }

        List<String> path = pieces[fromIndex].getMovement().movePath(from, to);
        if (isBlocked(path)){
            throw new IllegalStateException(String.format("Cannot move %s-%s, move blocked!", from, to));
        }

        if (isEliminating(from, to,false)) {
            removePiece(to);
        }

        if(board.getPiece(from).getType() == ChessmanTypes.PAWN){
            if(Math.abs(from.charAt(1)-to.charAt(1))==2){
                board.getPiece(from).setEnp(true);
            }
        }
        setPiece(to, pieces[fromIndex]);
        removePiece(from);
        firstMove = false;


    }

    public boolean isBlocked(List<String> path) {
        BoardPiece current = board.getPiece(path.get(0));
        boolean valid = true;
        if (current.getType() != ChessmanTypes.KNIGHT) {
            for (int i = 1; i < path.size()-1; i++) {
                if(board.getPiece(i) != null){
                    return true;
                }
            }
        }
        if(board.getPiece(path.get(path.size() - 1)) != null) {
            return board.getPiece(path.get(path.size() - 1)).getColor() == current.getColor();
        }

        return false;
    }

    public boolean isEliminating(String pre, String post, boolean enp) {
        BoardPiece current = board.getPiece(pre);
        BoardPiece movingTo = board.getPiece(post);
        //assumes white on bottom
        if (movingTo == null) {
            if(current.getType() == ChessmanTypes.PAWN){
                switch(current.getColor()){
                    case WHITE -> {
                        int enp_check = post.charAt(1)-1;
                        StringBuilder str = new StringBuilder();
                        str.append(post.charAt(0));
                        str.append(enp_check);
                        String s = str.toString();
                        //System.out.print(s);
                        if(board.getPiece(s) != null){
                            if(board.getPiece(s).getEnp()){
                                return true;
                            }
                        }
                    }
                    case BLACK -> {
                        int enp_check = post.charAt(1)+1;
                        StringBuilder str = new StringBuilder();
                        str.append(post.charAt(0));
                        str.append(enp_check);
                        String s = str.toString();
                        if(board.getPiece(s) != null){
                            if(board.getPiece(s).getEnp()){
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
        return current.getColor() != movingTo.getColor();
    }

    public Board getBoard(){
        return board;
    }
    public BoardPiece[] getPieces() {
        return pieces;
    }
}
