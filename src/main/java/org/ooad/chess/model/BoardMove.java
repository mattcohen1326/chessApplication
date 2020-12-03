package org.ooad.chess.model;

import java.util.Objects;

public class BoardMove {

    private final BoardPosition from;
    private final BoardPosition to;

    public BoardMove(BoardPosition from, BoardPosition to) {
        this.from = from;
        this.to = to;
    }

    public BoardPosition getFrom() {
        return from;
    }

    public BoardPosition getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardMove boardMove = (BoardMove) o;
        return Objects.equals(from, boardMove.from) &&
                Objects.equals(to, boardMove.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return String.format("BoardMove{from=%s, to=%s}", from, to);
    }
}
