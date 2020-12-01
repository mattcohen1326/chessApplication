package org.ooad.chess.logic;

import org.jetbrains.annotations.Nullable;
import org.ooad.chess.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.ooad.chess.model.Board.LENGTH;
import static org.ooad.chess.model.ChessmanColor.BLACK;
import static org.ooad.chess.model.ChessmanColor.WHITE;
import static org.ooad.chess.model.ChessmanTypes.*;

public class MoveEngine {

    private final Board board;

    public MoveEngine(Board board) {
        this.board = board;
    }

    public void updateMoves(String position) {
        BoardPiece piece = getPiece(position);
        List<String> availableMoves = new ArrayList<>();

        for (int i = 1; i <= LENGTH; i++) {
            for (int j = 1; j <= LENGTH; j++) {
                boolean blocked = isBlocked(piece.getMovement().movePath(position, stringifyMove(i, j)));
                if (!blocked && piece.getMovement().movePossible(position, stringifyMove(i, j), piece.getFirst(), isEliminating(position, stringifyMove(i, j)))) {
                    //1.out.printf("%s%n", stringifyMove(i,j));
                    availableMoves.add(stringifyMove(i, j));
                }
            }
        }

        piece.setAvailableMoves(availableMoves.stream().map(BoardPosition::new).collect(Collectors.toList()));
    }

    /**
     * Moves a chess piece from a location to a destination.
     *
     * @param from The source location.
     * @param to   The destination.
     * @throws IllegalStateException if the source location is empty or the destination is full, if the move is invalid given the type, or if the move is blocked by another piece.
     */
    public void movePiece(String from, String to) {
        if (hasPiece(to)) {
            throw new IllegalStateException(String.format("Cannot move %s-%s, %s is not empty", from, to, to));
        }
        if (!hasPiece(from)) {
            throw new IllegalStateException(String.format("Cannot move %s-%s, %s is empty", from, to, from));
        }

        int fromIndex = getIndex(from);
        int toIndex = getIndex(to);

        if (!getPiece(fromIndex).getMovement().movePossible(from, to, getPiece(fromIndex).getFirst(), isEliminating(from, to))) {
            throw new IllegalStateException(String.format("Cannot move %s-%s, invalid move for type %s", from, to, getPiece(fromIndex).getType()));
        }
        /*updateMoves(from);
        if (!getPiece(fromIndex).getAvailable_moves().contains(to)) {
            throw new IllegalStateException(String.format("Cannot move %s-%s, invalid move for type %s", from, to, getPiece(fromIndex).getType()));
        }*/

        List<String> path = getPiece(fromIndex).getMovement().movePath(from, to);
        if (isBlocked(path)) {
            throw new IllegalStateException(String.format("Cannot move %s-%s, move blocked!", from, to));
        }

        isInCheck(to, from);
        isInCheckmate(to, from);

        if (isEliminating(from, to)) {
            if (getPiece(from).getType() == PAWN) {
                if (!hasPiece(to)) {
                    switch (getPiece(from).getColor()) {
                        case WHITE -> {
                            if (getPiece(getNeighbor(to, "down", 1)).getEnp()) {
                                removePiece(getNeighbor(to, "down", 1));
                            }
                        }
                        case BLACK -> {
                            if (getPiece(getNeighbor(to, "up", 1)).getEnp()) {
                                removePiece(getNeighbor(to, "up", 1));
                            }
                        }
                    }
                } else {
                    removePiece(to);
                }
            } else {
                removePiece(to);
            }
        }

        if (getPiece(from) != null) {
            getPiece(from).setFirstMove(false);
            if (getPiece(from).getType() == ChessmanTypes.PAWN) {
                if (Math.abs(from.charAt(1) - to.charAt(1)) == 2) {
                    getPiece(from).setEnp(true);
                }
            }
        }
        setPiece(to, getPiece(fromIndex));
        getPiece(from).setFirstMove(false);
        removePiece(from);
    }

    private String makePos(char col, char row) {
        StringBuilder s = new StringBuilder();
        s.append(col);
        s.append(row);
        return s.toString();
    }

    public String stringifyMove(int row, int col) {
        StringBuilder str = new StringBuilder();
        str.append((char) (col + 64));
        str.append(row);
        String check_pos = str.toString();
        return check_pos;
    }

    private ArrayList<BoardPiece> getColorPieces(ChessmanColor color) {
        ArrayList<BoardPiece> color_pieces = new ArrayList<BoardPiece>();
        for (int i = 1; i <= LENGTH; i++) {
            for (int j = 1; j <= LENGTH; j++) {
                if (getPiece(stringifyMove(i, j)) == null) {
                    continue;
                }
                if (getPiece(stringifyMove(i, j)).getColor() == color) {
                    color_pieces.add(getPiece(stringifyMove(i, j)));
                }
            }
        }
        return color_pieces;
    }

    public boolean isBlocked(List<String> path) {
        BoardPiece current = getPiece(path.get(0));
        boolean valid = true;
        if (current.getType() != ChessmanTypes.KNIGHT) {
            for (int i = 1; i < path.size() - 1; i++) {
                //System.out.println(getPiece(path.get(i)));
                if (getPiece(path.get(i)) != null) {
                    return true;
                }
            }
        }
        if (getPiece(path.get(path.size() - 1)) != null) {
            if (current.getType() == PAWN) {
                if (!(getPiece(path.get(path.size() - 1)).getColor() == current.getColor())) {
                    if (isEliminating(current.getPosition().toString(), path.get(path.size() - 1))) {
                        return false;
                    } else {
                        return true;
                    }
                }
            } else {
                return getPiece(path.get(path.size() - 1)).getColor() == current.getColor();
            }
        }

        return false;
    }

    public @Nullable String getNeighbor(String p, String dir, int amt) {
        int row = (int) p.charAt(1) - 48;
        int col = (int) p.charAt(0) - 64;
        String neighbor = null;
        //if(row <= board.LENGTH && row > 0 && col <= board.LENGTH && col > 0) {
        switch (dir) {
            case "up" -> {
                if (row + amt <= LENGTH) {
                    neighbor = stringifyMove(row + amt, col);
                }
            }
            case "down" -> {
                if (row - amt > 0) {
                    neighbor = stringifyMove(row - amt, col);
                }
            }
            case "left" -> {
                if (col - amt > 0) {
                    neighbor = stringifyMove(row, col - amt);
                }
            }
            case "right" -> {
                if (col + amt <= LENGTH) {
                    neighbor = stringifyMove(row, col + amt);
                }
            }
        }
        // }
        return neighbor;
    }

    public boolean isEliminating(String pre, String post) {
        BoardPiece current = getPiece(pre);
        BoardPiece movingTo = getPiece(post);
        int col_diff = Math.abs(pre.charAt(0) - post.charAt(0));
        int row_diff = Math.abs(pre.charAt(1) - post.charAt(1));
        //assumes white on bottom
        if (movingTo == null) {
            if (current.getType() == ChessmanTypes.PAWN) {
                StringBuilder str = new StringBuilder();
                int enp_check = 0;
                switch (current.getColor()) {
                    case WHITE -> {
                        enp_check = Character.getNumericValue(post.charAt(1)) - 1;
                    }
                    case BLACK -> {
                        enp_check = Character.getNumericValue(post.charAt(1)) + 1;
                    }
                }
                str.append(post.charAt(0));
                str.append(enp_check);
                String s = str.toString();
                if (getPiece(s) != null) {
                    if (getPiece(s).getEnp() && Math.abs(col_diff) == 1 && Math.abs(row_diff) == 1) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            if (current.getType() == PAWN) {
                if (row_diff == 1 && col_diff == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        return current.getColor() != movingTo.getColor();
    }

    public boolean validCastle(String king, String rook) {
        ChessmanColor color = getPiece(king).getColor();
        if (!getPiece(king).getFirst() || !getPiece(rook).getFirst()) {
            return false;
        }
        switch (color) {
            case WHITE -> {
                ArrayList<BoardPiece> enemies = getColorPieces(BLACK);
                if (getPiece(getNeighbor(king, "right", 3)).getType() == ROOK) {
                    if (getNeighbor(king, "right", 1) == null && getNeighbor(king, "right", 2) == null) {
                        for (int i = 1; i < 3; i++) {
                            for (int j = 0; j < enemies.size(); j++) {
                                if (enemies.get(i).getMovement().movePossible(enemies.get(j).getPosition().toString(), getNeighbor(king, "right", i), true, false)) {
                                    return false;
                                }
                            }
                        }
                    } else {
                        return false;
                    }
                } else if (getPiece(getNeighbor(king, "left", 4)).getType() == ROOK) {
                    if (getNeighbor(king, "left", 1) == null && getNeighbor(king, "left", 2) == null && getNeighbor(king, "left", 3) == null) {
                        for (int i = 1; i < 4; i++) {
                            for (int j = 0; j < enemies.size(); j++) {
                                if (enemies.get(i).getMovement().movePossible(enemies.get(j).getPosition().toString(), getNeighbor(king, "right", i), true, false)) {
                                    return false;
                                }
                            }

                        }
                    } else {
                        return false;
                    }
                }
            }
            case BLACK -> {
                ArrayList<BoardPiece> enemies = getColorPieces(WHITE);
                if (getPiece(getNeighbor(king,"right",4)).getType() == ROOK) {
                    if (getNeighbor(king, "right", 1) == null && getNeighbor(king, "right", 2) == null && getNeighbor(king, "right", 3) == null) {
                        for (int i = 1; i < 3; i++) {
                            for (int j = 0; j < enemies.size(); j++) {
                                if (enemies.get(i).getMovement().movePossible(enemies.get(j).getPosition().toString(), getNeighbor(king, "right", i), true, false)) {
                                    return false;
                                }
                            }
                        }
                    } else {
                        return false;
                    }
                } else if (getPiece(getNeighbor(king,"left",3)).getType() == ROOK) {
                    if (getNeighbor(king, "left", 1) == null && getNeighbor(king, "left", 2) == null) {
                        for (int i = 1; i < 4; i++) {
                            for (int j = 0; j < enemies.size(); j++) {
                                if (enemies.get(i).getMovement().movePossible(enemies.get(j).getPosition().toString(), getNeighbor(king, "right", i), true, false)) {
                                    return false;
                                }
                            }

                        }
                    } else {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public String[] getKings() {
        String white = null, black = null;
        for (int i = 1; i <= LENGTH; i++) {
            for (int j = 1; j <= LENGTH; j++) {
                String toTest = stringifyMove(i, j);
                if (hasPiece(toTest)) {
                    BoardPiece pieceCur = getPiece(toTest);
                    if (pieceCur.getType() == KING) {
                        if (pieceCur.getColor() == WHITE) {
                            white = toTest;
                        } else {
                            black = toTest;
                        }
                    }
                }
            }
        }

        return new String[]{white, black};
    }

    boolean isInCheck(String to, String from) {
        String[] kings = getKings();
        int index = getIndex(from);
        BoardPiece movedPiece = getPiece(index);

        if (movedPiece.getColor() == WHITE) {
            return movedPiece.getMovement().movePossible(to, kings[1], false, true);
        } else {
            return movedPiece.getMovement().movePossible(to, kings[0], false, true);
        }
    }

    boolean isInCheckmate(String to, String from) {
        String[] kings = getKings();
        int movedIndex = getIndex(from);
        int kingIndex;
        BoardPiece movedPiece = getPiece(movedIndex);
        BoardPiece king;
        String pos;

        if (isInCheck(to, from)) {
            if (movedPiece.getColor() == WHITE) {
                kingIndex = getIndex(kings[1]);
                pos = kings[1];
            } else {
                kingIndex = getIndex(kings[0]);
                pos = kings[0];
            }
            king = getPiece(kingIndex);
            updateMoves(pos);

            return king.getAvailableMoves().size() == 0;
        }

        return false;
    }

    // Compatability methods
    private boolean hasPiece(String location) {
        return board.hasPiece(new BoardPosition(location));
    }

    private void setPiece(String location, BoardPiece piece) {
        board.setPiece(new BoardPosition(location), piece);
    }

    private @Nullable BoardPiece getPiece(String location) {
        if (BoardPosition.isValid(location)) {
            return board.getPiece(new BoardPosition(location));
        } else {
            return null;
        }
    }

    private @Nullable BoardPiece getPiece(int index) {
        int row = index / LENGTH;
        int col = index % LENGTH;
        return board.getPiece(new BoardPosition(row, col));
    }

    private int getIndex(String location) {
        BoardPosition position = new BoardPosition(location);
        return position.getRow() * LENGTH + position.getCol();
    }

    private void removePiece(String location) {
        board.removePiece(new BoardPosition(location));
    }
}
