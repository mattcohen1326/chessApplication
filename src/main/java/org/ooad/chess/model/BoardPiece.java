package org.ooad.chess.model;

import org.ooad.chess.model.behaviors.*;

/**
 * Represents a piece on a Chess board.
 */
public class BoardPiece {

    private final ChessmanTypes type;
    private final ChessmanColor color;

    private MoveStrategy movement;

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
}
