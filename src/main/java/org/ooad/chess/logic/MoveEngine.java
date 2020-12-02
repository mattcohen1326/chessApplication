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

                    if (piece.getType() == PAWN){
                        //System.out.println((int)piece.getPosition().toString().charAt(0)-63);
                        if(i < ((int)piece.getPosition().toString().charAt(0)-63)){
                            continue;
                        }
                        else{
                            availableMoves.add(stringifyMove(i, j));
                        }
                    }
                    else{
                        availableMoves.add(stringifyMove(i, j));
                    }
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
        if(current == null){
            return true;
        }
        boolean valid = true;
        if (current.getType() != ChessmanTypes.KNIGHT) {
            //System.out.println(path);
            for (int i = 1; i < path.size() - 1; i++) {
                if (getPiece(path.get(i)) != null) {
                    //System.out.println(getPiece(path.get(i)).getType());
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
                if (row_diff == 1 && col_diff == 1 && getPiece(post).getColor() != getPiece(pre).getColor()) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        return current.getColor() != movingTo.getColor();
    }

    private boolean castleCheck(ArrayList<BoardPiece> enemies, int dist, String king, String rook, String dir) {
        if (getPiece(getNeighbor(king, dir, dist)).getType() == ChessmanTypes.ROOK && getNeighbor(king,dir,dist).equals(rook)) {
            for (int i = 1; i < dist; i++) {
                if (getPiece(getNeighbor(king, dir, i)) != null) {
                    return false;
                } else {
                    for (int j = 0; j < enemies.size(); j++) {
                        if (enemies.get(j).getMovement().movePossible(enemies.get(j).getPosition().toString(), getNeighbor(king, dir, i), enemies.get(j).getFirst(), true)) {
                            return false;
                        }
                    }
                }
            }
        }
        else{
            return false;
        }
        return true;
    }

    public boolean validCastle(String king, String rook) {
        if(getPiece(rook) == null || getPiece(king) == null){
            return false;
        }
        ChessmanColor color = getPiece(king).getColor();
        if (!getPiece(king).getFirst() || !getPiece(rook).getFirst()) {
            return false;
        }
        //IT IS A BISHOP IT SHOULD BE A ROOK
        boolean check1 = false;
        boolean check2 = false;

        switch (color) {
            case WHITE -> {
                ArrayList<BoardPiece> enemies = getColorPieces(BLACK);
                check1 = castleCheck(enemies, 3, king, rook, "right");
                check2 = castleCheck(enemies, 4, king, rook, "left");
            }
                case BLACK -> {
                    ArrayList<BoardPiece> enemies = getColorPieces(WHITE);
                    check1 = castleCheck(enemies, 4, king, rook, "right");
                    check2 = castleCheck(enemies, 3, king, rook, "left");
                }
        }
        if(check1 || check2){
            return true;
        }
        return false;
    }

    public BoardPosition[] getKings() {
        BoardPosition white = null, black = null;
        for (int i = 0; i < board.getPieces().length; i++) {
            BoardPiece current = board.getPieces()[i];
            if (current != null) {
                if (current.getColor().equals(WHITE) && current.getType().equals(KING)) {
                    white = current.getPosition();
                }
                if (current.getColor().equals(BLACK) && current.getType().equals(KING)) {
                    black = current.getPosition();
                }
            }
        }

        return new BoardPosition[]{white, black};
    }
    public void testHelp(String loc){
        removePiece(loc);
    }
    boolean isInCheck(String to, String from) {
        BoardPosition[] kings = getKings();
        int index = getIndex(from);
        BoardPiece movedPiece = board.getPieces()[index];

        if (movedPiece.getColor() == WHITE) {
            return movedPiece.getMovement().movePossible(to, kings[1].toString(), false, true);
        } else {
            return movedPiece.getMovement().movePossible(to, kings[0].toString(), false, true);
        }
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
