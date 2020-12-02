package org.ooad.chess.model;

import org.ooad.chess.logic.behaviors.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a piece on a Chess board.
 */
public class BoardPiece {

    private final ChessmanTypes type;
    private final ChessmanColor color;
    private final List<BoardPosition> availableMoves = new ArrayList<>(0);
    private MoveStrategy movement;
    private boolean firstMove = true;
    private boolean enp = false;
    private BoardPosition position;

    public List<BoardPosition> getAvailableMoves() {
        return availableMoves;
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

    public void setPosition(BoardPosition position) {
        this.position = position;
    }

    public BoardPosition getPosition() {
        return position;
    }

    public boolean getFirst() {
        return firstMove;
    }

    public void setFirstMove(boolean first) {
        firstMove = first;
    }

    public boolean getEnp() {
        return enp;
    }

    public void setEnp(boolean e) {
        enp = e;
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

    public void setAvailableMoves(List<BoardPosition> availableMoves) {
        this.availableMoves.clear();
        this.availableMoves.addAll(availableMoves);
    }


}


