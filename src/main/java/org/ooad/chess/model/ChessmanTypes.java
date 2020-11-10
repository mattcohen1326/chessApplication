package org.ooad.chess.model;

/**
 * Represents the types of Chessmen.
 */
public enum ChessmanTypes {

    // TODO move the unicode out.

    PAWN("\u265F", "\u2659"),
    ROOK("\u265C", "\u2656"),
    KNIGHT("\u265E", "\u2658"),
    BISHOP("\u265D", "\u2657"),
    QUEEN("\u265B", "\u2655"),
    KING("\u265A", "\u2654");

    private final String blackString;
    private final String whiteString;

    ChessmanTypes(String blackString, String whiteString) {
        this.blackString = blackString;
        this.whiteString = whiteString;
    }

    public String getString(ChessmanColor color) {
        return color == ChessmanColor.BLACK ? blackString : whiteString;
    }
}
