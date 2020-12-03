package org.ooad.chess.model;

import org.ooad.chess.logic.behaviors.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.ooad.chess.model.ChessmanColor.BLACK;
import static org.ooad.chess.model.ChessmanColor.WHITE;

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

    public char toFen() {
        return type.getFen(color);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardPiece that = (BoardPiece) o;
        return firstMove == that.firstMove &&
                enp == that.enp &&
                type == that.type &&
                color == that.color &&
                Objects.equals(availableMoves, that.availableMoves) &&
                Objects.equals(movement, that.movement) &&
                Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, color, availableMoves, movement, firstMove, enp, position);
    }

    @Override
    public String toString() {
        return String.format("BoardPiece{type=%s, color=%s, availableMoves=%s, movement=%s, firstMove=%s, enp=%s, position=%s}",
                type,
                color,
                availableMoves,
                movement,
                firstMove,
                enp,
                position);
    }

    public static BoardPiece fromFen(char fen) {
        ChessmanColor color = Character.isUpperCase(fen) ? WHITE : BLACK;
        ChessmanTypes type = Arrays.stream(ChessmanTypes.values())
                .filter(it -> it.getFen(BLACK) == Character.toLowerCase(fen))
                .findFirst()
                .orElseThrow();
        return new BoardPiece(type, color);
    }
}


