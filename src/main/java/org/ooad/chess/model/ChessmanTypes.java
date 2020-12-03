package org.ooad.chess.model;

import static org.ooad.chess.model.ChessmanColor.*;

/**
 * Represents the types of Chessmen.
 */
public enum ChessmanTypes {

    // TODO move the unicode out.

    PAWN("\u265F", "\u2659", "p"),
    ROOK("\u265C", "\u2656", "r"),
    KNIGHT("\u265E", "\u2658", "n"),
    BISHOP("\u265D", "\u2657", "b"),
    QUEEN("\u265B", "\u2655", "q"),
    KING("\u265A", "\u2654", "k");

    private final String blackString;
    private final String whiteString;
    private final String fen;

    ChessmanTypes(String blackString, String whiteString, String fen) {
        this.blackString = blackString;
        this.whiteString = whiteString;
        this.fen = fen;
    }

    public char getFen(ChessmanColor color) {
        return color == WHITE ? fen.toUpperCase().charAt(0) : fen.charAt(0);
    }

    public String getString(ChessmanColor color) {
        return color == BLACK ? blackString : whiteString;
    }

}
