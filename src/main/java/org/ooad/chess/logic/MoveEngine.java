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

    public void updateMoves(BoardPiece piece) {
        updateMoves(piece.getPosition().toString());
    }

    /**
     * given a piece's location, update its available moves that it can legally make
     * @param position
     */
    public void updateMoves(String position) {
        BoardPiece piece = getPiece(position);
        if(piece == null){
            return;
        }
        List<String> availableMoves = new ArrayList<String>();

        for (int i = 1; i <= LENGTH; i++) {
            for (int j = 1; j <= LENGTH; j++) {
                boolean blocked = isBlocked(piece.getMovement().movePath(position, stringifyMove(i, j)));
                if (!blocked && piece.getMovement().movePossible(position, stringifyMove(i, j), piece.getFirst(), isEliminating(position, stringifyMove(i, j)))) {
                    if (piece.getType() == PAWN){
                        switch(piece.getColor()){
                            case WHITE -> {
                                if(!(i < ((int)piece.getPosition().toString().charAt(1))-48 )){
                                    availableMoves.add(stringifyMove(i,j));
                                }
                            }
                            case BLACK -> {

                                if(!(i > ((int)piece.getPosition().toString().charAt(1))-48)){
                                    availableMoves.add(stringifyMove(i,j));
                                }
                            }

                        }

                    }
                    else if (piece.getType().equals(KING)) {
                        if (!movesToCheck(stringifyMove(i,j), piece.getColor())) {
                            availableMoves.add(stringifyMove(i,j));
                        }
                    }
                    else{
                        availableMoves.add(stringifyMove(i,j));
                    }
                }
                else if(blocked && piece.getType() == ROOK && getPiece(stringifyMove(i,j))!=null){
                    if(getPiece(stringifyMove(i,j)).getType() == KING && getPiece(stringifyMove(i,j)).getColor() == piece.getColor()){
                        if(validCastle(stringifyMove(i,j),piece.getPosition().toString())){
                            availableMoves.add(stringifyMove(i,j));
                        }
                    }
                }
            }
        }
        piece.setAvailableMoves(availableMoves.stream().map(BoardPosition::new).collect(Collectors.toList()));

    }

    /**
     * Execute a move, throw an exception if the move is invalid for a given piece
     * @param from
     * @param to
     */
    public void movePiece(String from, String to) {
        if (!hasPiece(from)) {
            throw new IllegalStateException(String.format("Cannot move %s-%s, %s is empty", from, to, from));
        }
        if(getPiece(from) != null && getPiece(to) != null && getPiece(from).getType() == ROOK && getPiece(to).getType() == KING && getPiece(from).getColor() == getPiece(to).getColor()){
            if(validCastle(to,from)){
                executeCatle(to,from);
                updateEnpassant(new BoardPosition(from));
                return;
            }
        }
        BoardPosition fromPosition = new BoardPosition(from);
        BoardPiece sourcePiece = board.getPiece(fromPosition);
        BoardPosition toPos = new BoardPosition(to);
        updateMoves(from);
        if (!sourcePiece.getAvailableMoves().contains(toPos)) {
            throw new IllegalStateException(String.format("Cannot move %s-%s, invalid move for type %s", from, to, board.getPiece(fromPosition).getType()));
        }

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
                    if (pawnBackwardsCapture(getPiece(from), to)) {
                        removePiece(to);
                    }
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

        setPiece(to, board.getPiece(fromPosition));
        getPiece(from).setFirstMove(false);
        removePiece(from);
        updateEnpassant(new BoardPosition(to));

    }

    /**
     * Movement function for specialized movement castling
     * @param king
     * @param rook
     */
    private void executeCatle(String king, String rook){
        if(getPiece(king).getPosition().equals(new BoardPosition("D8"))){
            if(getPiece(rook).getPosition().equals(new BoardPosition("H8"))){
                setPiece("F8",getPiece(rook));
                getPiece(rook).setFirstMove(false);
                removePiece(rook);
                setPiece("G8",getPiece(king));
            }
            else{
                setPiece("D8",getPiece(rook));
                getPiece(rook).setFirstMove(false);
                removePiece(rook);
                setPiece("C8",getPiece(king));
            }
            getPiece(king).setFirstMove(false);
            removePiece(king);
        }
        else if(getPiece(king).getPosition().equals(new BoardPosition("E1"))){
            if(getPiece(rook).getPosition().equals(new BoardPosition("H1"))){
                setPiece("F1",getPiece(rook));
                getPiece(rook).setFirstMove(false);
                removePiece(rook);
                setPiece("G1",getPiece(king));
            }
            else{
                setPiece("D1",getPiece(rook));
                getPiece(rook).setFirstMove(false);
                removePiece(rook);
                setPiece("C1",getPiece(king));
            }
            getPiece(king).setFirstMove(false);
            removePiece(king);


        }
    }

    private void updateEnpassant(BoardPosition bp){
        for(int i = 0; i < board.LENGTH;i++){
            for(int j = 0; j < board.LENGTH; j++){
               BoardPosition check = new BoardPosition(i,j);
               if(!check.equals(bp) && board.getPiece(check)!=null){
                   board.getPiece(check).setEnp(false);
               }
            }
        }
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

    /**
     * Loop through a piece's movement path and determine if the path is blocked by another piece
     * @param path
     * @return
     */
    public boolean isBlocked(List<String> path) {
        BoardPiece current = getPiece(path.get(0));
        if(current == null){
            return true;
        }
        if (current.getType() != ChessmanTypes.KNIGHT) {
            for (int i = 1; i < path.size() - 1; i++) {
                if (getPiece(path.get(i)) != null) {
                    return true;
                }
            }
        }
        if (getPiece(path.get(path.size() - 1)) != null) {
            if (current.getType() == PAWN) {
                if (!(getPiece(path.get(path.size() - 1)).getColor() == current.getColor())) {
                    return !isEliminating(current.getPosition().toString(), path.get(path.size() - 1));
                }
                else{
                    return true;
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

    /**
     * Determine if a given move involves eliminating an enemy piece
     * @param pre
     * @param post
     * @return
     */
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
                    return getPiece(s).getEnp() && Math.abs(col_diff) == 1 && Math.abs(row_diff) == 1;
                }
            }
            return false;
        }
        else {
            if (current.getType() == PAWN) {
                return row_diff == 1 && col_diff == 1 && getPiece(post).getColor() != getPiece(pre).getColor();
            }
        }

        return current.getColor() != movingTo.getColor();
    }

    private boolean castleCheck(ArrayList<BoardPiece> enemies, int dist, String king, String rook, String dir) {
        if ((getNeighbor(king, dir, dist)) == null){
            return false;
        }
        if(getPiece(getNeighbor(king,dir,dist))==null){
            return false;
        }

        if (getPiece(getNeighbor(king, dir, dist)).getType() == ChessmanTypes.ROOK && getNeighbor(king,dir,dist).equals(rook)) {
            for (int i = 1; i < dist; i++) {
                if (getPiece(getNeighbor(king, dir, i)) != null) {
                    return false;
                } else {

                    for (int j = 0; j < enemies.size(); j++) {
                        boolean check = false;
                        for(int k = 0; k < enemies.get(j).getAvailableMoves().size(); k++) {
                            if(enemies.get(j).getAvailableMoves().get(k).toString().equals(getNeighbor(king, dir, i))){
                                check = true;
                            }
                        }
                            if (check) {
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
        ArrayList<BoardPiece> enemies = new ArrayList<>();
        switch (color) {
            case WHITE -> {
                enemies = getColorPieces(BLACK);

            }
            case BLACK -> {
                enemies = getColorPieces(WHITE);
            }
        }
        check1 = castleCheck(enemies, 3, king, rook, "right");
        check2 = castleCheck(enemies, 4, king, rook, "left");
        return check1 || check2;
    }

    /**
     * Determine if a king is in checkmate
     * @return color of king in checkmate or null
     */
    public @Nullable ChessmanColor isInCheckmate() {
        for (ChessmanColor color : ChessmanColor.values()) {
            if (isInCheck() == color) {
                BoardPiece king = board.getKing(color);
                if (king.getAvailableMoves().isEmpty()) {
                    return color;
                }
            }
        }
        return null;
    }

    /**
     * Determines if a king is in check
     * @return color of king in check or null
     */
    public ChessmanColor isInCheck() {
        for (int i = 0; i < board.getPieces().length; i++) {
            BoardPiece piece = board.getPieces()[i];
            BoardPiece king = null;
            if (piece != null) {
                if (piece.getColor().equals(WHITE)) {
                    king = board.getKing(BLACK);
                } else {
                    king = board.getKing(WHITE);
                }

                if (piece.getMovement().movePossible(piece.getPosition().toString(), king.getPosition().toString(), false, true)) {
                    List<String> path = piece.getMovement().movePath(piece.getPosition().toString(), king.getPosition().toString());
                    if (!isBlocked(path)) {
                        if (piece.getType() == PAWN) {
                            if (pawnBackwardsCapture(piece, king.getPosition().toString())) {
                                return king.getColor();
                            }
                        }
                        else {
                            return king.getColor();
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Check if a movement by a king would result in that king being checked by the opposing team
     * @param to
     * @param color
     * @return
     */
    public boolean movesToCheck(String to, ChessmanColor color) {
        for (int i = 0; i < board.getPieces().length; i++) {
            BoardPiece piece = board.getPieces()[i];
            if (piece != null) {
                if (piece.getColor() != color) {
                    if (piece.getMovement().movePossible(piece.getPosition().toString(), to, piece.getFirst(), true)) {
                        List<String> path = piece.getMovement().movePath(piece.getPosition().toString(), to);
                        if (!isBlocked(path)) {
                            if (piece.getType() == PAWN) {
                                return pawnBackwardsCapture(piece, to);
                            }
                            else {
                                return true;
                            }
                        }
                        else {
                            if (getPiece(to) != piece) {
                                if (getPiece(to) != null) {
                                    String toRemove = path.get(path.size() - 1);
                                    path.remove(toRemove);
                                    if ((!isBlocked(path) && getPiece(path.get(path.size()-1)) == null) || path.size() == 1) {
                                        return true;
                                    }
                                }
                                else {
                                    boolean blocked = false;
                                    for (String s : path) {
                                        BoardPiece test = getPiece(s);
                                        if (test != null && test != board.getKing(color) && test != piece) {
                                            blocked = true;
                                        }
                                    }
                                    if (!blocked) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Prevent backwards pawn movement
     * @param pawn
     * @param to
     * @return
     */
    private boolean pawnBackwardsCapture(BoardPiece pawn, String to) {
        if (pawn.getColor() == WHITE && (pawn.getPosition().toString().charAt(1)) < to.charAt(1)) {
            return true;
        }
        if (pawn.getColor() == BLACK && (pawn.getPosition().toString().charAt(1)) > to.charAt(1)) {
            return true;
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

    private void removePiece(String location) {
        board.removePiece(new BoardPosition(location));
    }
}
