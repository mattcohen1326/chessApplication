package org.ooad.chess.model;

import org.jetbrains.annotations.Nullable;

import static org.ooad.chess.model.ChessmanColor.BLACK;
import static org.ooad.chess.model.ChessmanColor.WHITE;
import static org.ooad.chess.model.ChessmanTypes.*;

/**
 * Represents a chess board filled with pieces.
 */
public class Board {

    static final int LENGTH = 8;

    /**
     * The actual pieces on the board. This array is indexed starting from H1 as 0.
     */
    private final BoardPiece[] pieces = new BoardPiece[LENGTH * LENGTH];

    /**
     * Movement engine, to handle moves.
     */
    private final MoveEngine moveEngine = new MoveEngine(pieces, this);

    /**
     * Sets a piece at a location.
     *
     * @param location The location of the target piece expressed in Chess coords. I.e.:A1,B6.
     * @param piece    The piece to be set.
     */
    public void setPiece(String location, BoardPiece piece) {
        pieces[getIndex(location)] = piece;
        updateEngine();
    }

    /**
     * Removes a piece at a location.
     *
     * @param location The location to be removed.
     * @throws IllegalStateException if the location is empty.
     */
    public void removePiece(String location) {
        if (!hasPiece(location)) {
            throw new IllegalStateException(String.format("Cannot remove %s, location is empty", location));
        }
        pieces[getIndex(location)] = null;
    }

    /**
     * Get a piece at a location.
     *
     * @param location The location of the target piece expressed in Chess coords. I.e.:A1,B6.
     * @return The piece at the location, null if no piece is present.
     */
    public @Nullable BoardPiece getPiece(String location) {
        return getPiece(getIndex(location));
    }

    /**
     * Moves a chess piece from a location to a destination.
     *
     * @param from The source location.
     * @param to   The destination.
     */
    public void movePiece(String from, String to) {
        moveEngine.movePiece(from, to);

        updateEngine();
    }

    /**
     * Update movement engine with current board.
     */
    public void updateEngine() {
        moveEngine.updateBoard(pieces);
    }

    /**
     * Checks if a piece exists at a location.
     *
     * @param location The location of the target piece expressed in Chess coords. I.e.:A1,B6.
     * @return True if the space is occupied, false otherwise.
     */
    public boolean hasPiece(String location) {
        return getPiece(location) != null;
    }

    private @Nullable BoardPiece getPiece(int index) {
        return pieces[index];
    }

    private boolean hasPiece(int index) {
        return getPiece(index) != null;
    }

    int getIndex(String location) {
        int x = LENGTH - 1 - (location.charAt(0) - 'A');
        int y = location.charAt(1) - '1';
        if (x < 0 || x >= LENGTH || y < 0 || y >= LENGTH) {
            throw new IllegalArgumentException("Invalid location " + location);
        }
        return y * LENGTH + x;
    }

    /**
     * Prints the chess board to the console
     */
    public void print() {
        StringBuilder sb = new StringBuilder();
        for (int i = LENGTH * LENGTH - 1; i >= 0; i--) {
            if (i % LENGTH == LENGTH - 1) {
                sb.append(i / LENGTH + 1).append("|");
            }
            if (hasPiece(i)) {
                BoardPiece piece = getPiece(i);
                sb.append(piece.getType().getString(piece.getColor())).append(" ");
            } else {
                sb.append("-\u3000");
            }
            if (i % LENGTH == 0) {
                sb.append("\n");
            }
        }
        sb.append("  ");
        for (int i = 0; i < LENGTH; i++) {
            String s = String.format("%c\u3000", (char) ('A' + i));
            sb.append(s);
        }
        System.out.println(sb.toString());
    }

    /**
     * Create a new, populated, chess board.
     *
     * @return - a new chess board with the standard piece configuration
     */
    public static Board filledBoard() {
        ChessmanTypes[] bottomTypes = {ROOK, KNIGHT, BISHOP, QUEEN, KING, BISHOP, ROOK, KNIGHT};
        Board board = new Board();
        for (int i = 0; i < LENGTH; i++) {
            char col = (char) ('A' + i);
            board.setPiece(String.format("%c2", col), makePiece(PAWN, WHITE));
            board.setPiece(String.format("%c1", col), makePiece(bottomTypes[i], WHITE));

            board.setPiece(String.format("%c7", col), makePiece(PAWN, BLACK));
            board.setPiece(String.format("%c8", col), makePiece(bottomTypes[i], BLACK));
        }

        return board;
    }

    private static BoardPiece makePiece(ChessmanTypes type, ChessmanColor color) {
        return new BoardPiece(type, color);
    }
}
