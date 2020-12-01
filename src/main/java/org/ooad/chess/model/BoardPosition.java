package org.ooad.chess.model;

import java.util.Objects;

import static org.ooad.chess.model.Board.LENGTH;

public class BoardPosition {

    private final int row;
    private final int col;

    /**
     * Constructs a new board position
     *
     * @param row The row, indexed from 0
     * @param col The column, indexed from 0
     */
    public BoardPosition(int row, int col) {
        if (col < 0 || col >= LENGTH || row < 0 || row >= LENGTH) {
            throw new IllegalArgumentException(String.format("Invalid location row=%d, col=%d", row, col));
        }
        this.row = row;
        this.col = col;
    }

    public BoardPosition(String position) {
        this.col = position.charAt(0) - 'A';
        this.row = position.charAt(1) - '1';
        if (col < 0 || col >= LENGTH || row < 0 || row >= LENGTH) {
            throw new IllegalArgumentException("Invalid location " + position);
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public BoardPosition relativeTo(int row, int col) {
        return new BoardPosition(this.row + row, this.col + col);
    }

    @Override
    public String toString() {
        return String.format("%c%d", (char) col + 'A', row + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardPosition position = (BoardPosition) o;
        return row == position.row &&
                col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    public static boolean isValid(String position) {
        int col = position.charAt(0) - 'A';
        int row = position.charAt(1) - '1';
        return isValid(row, col);
    }

    public static boolean isValid(int row, int col) {
        return !(col < 0 || col >= LENGTH || row < 0 || row >= LENGTH);
    }
}
