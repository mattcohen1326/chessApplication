package org.ooad.chess.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.ooad.chess.model.ChessmanColor.BLACK;
import static org.ooad.chess.model.ChessmanColor.WHITE;
import static org.ooad.chess.model.ChessmanTypes.*;

/**
 * Represents a chess board filled with pieces.
 */
public class Board implements Iterable<Board.Entry> {

    public static final int LENGTH = 8;

    /**
     * The actual pieces on the board. This array is indexed starting from H1 as 0.
     */
    private final BoardPiece[] pieces = new BoardPiece[LENGTH * LENGTH];

    /**
     * Sets a piece at a location.
     *
     * @param position The location of the target piece expressed in Chess coords. I.e.:A1,B6.
     * @param piece    The piece to be set.
     */
    public void setPiece(BoardPosition position, BoardPiece piece) {
        int index = getIndex(position);
        pieces[index] = piece;
        piece.setPosition(position);
    }

    /**
     * Removes a piece at a location.
     *
     * @param position The location to be removed.
     * @throws IllegalStateException if the location is empty.
     */
    public void removePiece(BoardPosition position) {
        if (!hasPiece(position)) {
            throw new IllegalStateException(String.format("Cannot remove %s, location is empty", position));
        }
        int index = getIndex(position);
        pieces[index] = null;
    }

    public @Nullable BoardPiece getPiece(BoardPosition position) {
        int index = position.getRow() * LENGTH + position.getCol();
        return pieces[index];
    }

    /**
     * Checks if a piece exists at a position.
     *
     * @param position The position of the target piece expressed in Chess coords. I.e.:A1,B6.
     * @return True if the space is occupied, false otherwise.
     */
    public boolean hasPiece(BoardPosition position) {
        return getPiece(position) != null;
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
            if (pieces[i] != null) {
                BoardPiece piece = pieces[i];
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

    private int getIndex(BoardPosition position) {
        return position.getRow() * LENGTH + position.getCol();
    }

    /**
     * Create a new, populated, chess board.
     *
     * @return - a new chess board with the standard piece configuration
     */
    public static Board filledBoard() {
        ChessmanTypes[] bottomTypes = {ROOK, KNIGHT, BISHOP, QUEEN, KING, BISHOP, KNIGHT, ROOK};
        Board board = new Board();
        for (int col = 0; col < LENGTH; col++) {
            board.setPiece(new BoardPosition(1, col), makePiece(PAWN, WHITE));
            board.setPiece(new BoardPosition(0, col), makePiece(bottomTypes[col], WHITE));

            board.setPiece(new BoardPosition(6, col), makePiece(PAWN, BLACK));
            board.setPiece(new BoardPosition(7, col), makePiece(bottomTypes[col], BLACK));
        }

        return board;
    }

    private static BoardPiece makePiece(ChessmanTypes type, ChessmanColor color) {
        return new BoardPiece(type, color);
    }

    public BoardPiece[] getPieces() {
        return Arrays.copyOf(pieces, pieces.length);
    }

    @NotNull
    @Override
    public Iterator<Entry> iterator() {
        List<Entry> entries = new ArrayList<>(LENGTH * LENGTH);
        for (int row = 0; row < LENGTH; row++) {
            for (int col = 0; col < LENGTH; col++) {
                BoardPosition position = new BoardPosition(row, col);
                BoardPiece piece = getPiece(position);
                entries.add(new Entry(position, piece));
            }
        }
        return entries.iterator();
    }

    public static class Entry {
        private final BoardPosition position;
        private final BoardPiece piece;

        public Entry(BoardPosition position, @Nullable BoardPiece piece) {
            this.position = position;
            this.piece = piece;
        }

        public BoardPiece getPiece() {
            return piece;
        }

        public BoardPosition getPosition() {
            return position;
        }
    }
}
