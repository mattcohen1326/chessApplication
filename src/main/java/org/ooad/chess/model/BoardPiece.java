package org.ooad.chess.model;

/**
 * Represents a piece on a Chess board.
 */
public class BoardPiece {

    private final ChessmanTypes type;
    private final ChessmanColor color;

    public BoardPiece(ChessmanTypes type, ChessmanColor color) {
        this.type = type;
        this.color = color;
    }

    public ChessmanColor getColor() {
        return color;
    }

    public ChessmanTypes getType() {
        return type;
    }
}
