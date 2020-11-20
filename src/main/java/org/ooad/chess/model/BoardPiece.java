package org.ooad.chess.model;

import org.ooad.chess.model.behaviors.*;
import java.util.*;
/**
 * Represents a piece on a Chess board.
 */
public class BoardPiece {

    private final ChessmanTypes type;
    private final ChessmanColor color;
    private List available_moves;
    private MoveStrategy movement;
    private boolean first_move = true;

    public List getAvailable_moves() {
        return available_moves;
    }

    public BoardPiece(ChessmanTypes type, ChessmanColor color) {
        this.type = type;
        this.color = color;
        setMovementBehavior(type);
    }

    public ChessmanColor getColor() {
        return color;
    }

    public ChessmanTypes getType() {
        return type;
    }

    /*public int[] getPosition(){
        return position;
    }*/

    public void setFirstMove(boolean first) {
        this.first_move = first;
        return;
    }

    public MoveStrategy getMovement() {
        return movement;
    }

    public void setMovementBehavior(ChessmanTypes type) {
        switch (type) {
            case PAWN -> this.movement = new PawnMovement();
            case ROOK -> this.movement = new RookMovement();
            case KNIGHT -> this.movement = new KnightMovement();
            case BISHOP -> this.movement = new BishopMovement();
            case QUEEN -> this.movement = new QueenMovement();
            case KING -> this.movement = new KingMovement();
        }
    }

    // Assumes that the White pieces will be at the top of the board and the black pieces will be at the bottom (1,1)
    public void updateMoves(String position, MoveEngine engine) {
        int col = ((int) position.charAt(0)) - 64;
        int row = (int) position.charAt(1);
        
        for (int i = 1; i <= engine.getBoard().LENGTH; i++) {
            for (int j = 1; j <= engine.getBoard().LENGTH; j++) {
                if(movement.movePossible(position,stringifyMove(i,j),first_move,engine.isEliminating(position,stringifyMove(i,j))) && !engine.isBlocked(movement.movePath(position,stringifyMove(i,j)))){
                    available_moves.add(stringifyMove(i,j));
                }
            }
        }
    }

    private String stringifyMove(int row, int col) {
        StringBuilder str = new StringBuilder();
        str.append((char) (col + 64));
        str.append(row);
        String check_pos = str.toString();
        return check_pos;
    }
}


