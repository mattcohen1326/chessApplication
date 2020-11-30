package org.ooad.chess.model;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static org.ooad.chess.model.ChessmanColor.BLACK;
import static org.ooad.chess.model.ChessmanColor.WHITE;
import static org.ooad.chess.model.ChessmanTypes.*;
import static org.ooad.chess.model.ChessmanTypes.PAWN;

public class MoveEngine {

    MoveEngine(Board board) {
        this.pieces = board.getPieces();
        this.board = board;
    }

    private BoardPiece[] pieces;

    private final Board board;

    /**
     * Sets a piece at a location.
     *
     * @param location The location of the target piece expressed in Chess coords. I.e.:A1,B6.
     * @param piece    The piece to be set.
     */
    public void setPiece(String location, BoardPiece piece) {
        pieces[board.getIndex(location)] = piece;
        pieces[board.getIndex(location)].setPosition(location);
        board.update(pieces);
    }

    /**
     * Removes a piece at a location.
     *
     * @param location The location to be removed.
     * @throws IllegalStateException if the location is empty.
     */
    public void removePiece(String location) {
        if (!board.hasPiece(location)) {
            throw new IllegalStateException(String.format("Cannot remove %s, location is empty", location));
        }
        pieces[board.getIndex(location)] = null;
    }

    public void updateMoves(String position) {

        int index = board.getIndex(position);
        BoardPiece piece = pieces[index];
        List<String> availableMoves = null;

        for (int i = 1; i <= Board.LENGTH; i++) {
            for (int j = 1; j <= Board.LENGTH; j++) {
                boolean blocked = isBlocked(piece.getMovement().movePath(position,stringifyMove(i,j)));
                if(!blocked && piece.getMovement().movePossible(position,stringifyMove(i,j),piece.getFirst(),isEliminating(position,stringifyMove(i,j)))){
                    //1.out.printf("%s%n", stringifyMove(i,j));
                    availableMoves.add(stringifyMove(i,j));
                }
            }
        }

        piece.updateMoves(availableMoves);
    }

    /**
     * Moves a chess piece from a location to a destination.
     *
     * @param from The source location.
     * @param to   The destination.
     * @throws IllegalStateException if the source location is empty or the destination is full, if the move is invalid given the type, or if the move is blocked by another piece.
     */
    public void movePiece(String from, String to) {
        if (board.hasPiece(to)) {
            throw new IllegalStateException(String.format("Cannot move %s-%s, %s is not empty", from, to, to));
        }
        if (!board.hasPiece(from)) {
            throw new IllegalStateException(String.format("Cannot move %s-%s, %s is empty", from, to, from));
        }

        int fromIndex = board.getIndex(from);
        int toIndex = board.getIndex(to);

        if(!pieces[fromIndex].getMovement().movePossible(from, to, pieces[fromIndex].getFirst(), isEliminating(from, to))) {
            throw new IllegalStateException(String.format("Cannot move %s-%s, invalid move for type %s", from, to, pieces[fromIndex].getType()));
        }
        /*updateMoves(from);
        if (!pieces[fromIndex].getAvailable_moves().contains(to)) {
            throw new IllegalStateException(String.format("Cannot move %s-%s, invalid move for type %s", from, to, pieces[fromIndex].getType()));
        }*/

        List<String> path = pieces[fromIndex].getMovement().movePath(from, to);
        if (isBlocked(path)){
            throw new IllegalStateException(String.format("Cannot move %s-%s, move blocked!", from, to));
        }

        isInCheck(to, from);
        isInCheckmate(to, from);

        if (isEliminating(from, to)) {
            if(board.getPiece(from).getType() == PAWN){
                if(!board.hasPiece(to)) {
                    switch (board.getPiece(from).getColor()) {
                        case WHITE -> {
                            if (board.getPiece(getNeighbor(to, "down", 1)).getEnp()) {
                                removePiece(getNeighbor(to, "down", 1));
                            }
                        }
                        case BLACK -> {
                            if (board.getPiece(getNeighbor(to, "up", 1)).getEnp()) {
                                removePiece(getNeighbor(to, "up", 1));
                            }
                        }
                    }
                }
                else{
                    removePiece(to);
                }
            }
            else{
                removePiece(to);
            }
        }

        if(board.getPiece(from)!=null){
            board.getPiece(from).setFirstMove(false);
            if(board.getPiece(from).getType() == ChessmanTypes.PAWN){
                if(Math.abs(from.charAt(1)-to.charAt(1))==2){
                    board.getPiece(from).setEnp(true);
                }
            }
        }
        setPiece(to, pieces[fromIndex]);
        board.getPiece(from).setFirstMove(false);;
        removePiece(from);



    }

    private String makePos(char col, char row){
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
    private ArrayList<BoardPiece> getColorPieces(ChessmanColor color){
        ArrayList<BoardPiece> color_pieces = new ArrayList<BoardPiece>();
        for(int i = 1; i <= board.LENGTH; i++){
            for(int j = 1; j <= board.LENGTH; j++){
                if(board.getPiece(stringifyMove(i,j)) == null){
                    continue;
                }
                if(board.getPiece(stringifyMove(i,j)).getColor() == color){
                    color_pieces.add(board.getPiece(stringifyMove(i,j)));
                }
            }
        }
        return color_pieces;
    }
    public boolean isBlocked(List<String> path) {
        BoardPiece current = board.getPiece(path.get(0));
        boolean valid = true;
        if (current.getType() != ChessmanTypes.KNIGHT) {
            for (int i = 1; i < path.size()-1; i++) {
                //System.out.println(board.getPiece(path.get(i)));
                if(board.getPiece(path.get(i)) != null){
                    return true;
                }
            }
        }
        if(board.getPiece(path.get(path.size() - 1)) != null) {
            if(current.getType() == PAWN){
                if(!(board.getPiece(path.get(path.size() - 1)).getColor() == current.getColor())){
                    if(isEliminating(current.getPosition(),path.get(path.size()-1))){
                        return false;
                    }
                    else{
                        return true;
                    }

                }
            }
            else {
                return board.getPiece(path.get(path.size() - 1)).getColor() == current.getColor();
            }
        }

        return false;
    }
    public @Nullable String getNeighbor(String p,String dir, int amt){
        int row = (int)p.charAt(1)-48;
        int col = (int)p.charAt(0)-64;
        String neighbor = null;
        //if(row <= board.LENGTH && row > 0 && col <= board.LENGTH && col > 0) {
        switch(dir){
            case "up" ->{
                if(row+amt <= board.LENGTH){
                    neighbor = stringifyMove(row+amt,col);
                }
            }
            case "down" -> {
                if(row-amt > 0){
                    neighbor = stringifyMove(row-amt,col);
                }
            }
            case "left" -> {
                if(col-amt > 0){
                    neighbor = stringifyMove(row,col-amt);
                }
            }
            case "right" -> {
                if(col+amt <= board.LENGTH){
                    neighbor = stringifyMove(row,col+amt);
                }
            }
        }
       // }
        return neighbor;
    }
    public boolean isEliminating(String pre, String post) {
        BoardPiece current = board.getPiece(pre);
        BoardPiece movingTo = board.getPiece(post);
        int col_diff = Math.abs(pre.charAt(0)-post.charAt(0));
        int row_diff = Math.abs(pre.charAt(1)-post.charAt(1));
        //assumes white on bottom
        if (movingTo == null) {
            if(current.getType() == ChessmanTypes.PAWN){
                StringBuilder str = new StringBuilder();
                int enp_check = 0;
                switch(current.getColor()){
                    case WHITE -> {
                        enp_check = Character.getNumericValue(post.charAt(1))-1;
                    }
                    case BLACK -> {
                        enp_check = Character.getNumericValue(post.charAt(1))+1;
                    }
                }
                str.append(post.charAt(0));
                str.append(enp_check);
                String s = str.toString();
                if(board.getPiece(s) != null){
                    if(board.getPiece(s).getEnp() && Math.abs(col_diff) == 1 && Math.abs(row_diff) == 1){
                        return true;
                    }
                }
            }
            return false;
        }
        else{
            if(current.getType() == PAWN){
                if(row_diff == 1 && col_diff == 1){
                    return true;
                }
                else{
                    return false;
                }
            }
        }

        return current.getColor() != movingTo.getColor();
    }

    public boolean validCastle(String king, String rook){
        ChessmanColor color = board.getPiece(king).getColor();
        if(!board.getPiece(king).getFirst() || !board.getPiece(rook).getFirst()){
            return false;
        }
        switch(color){
            case WHITE -> {
                ArrayList<BoardPiece> enemies = getColorPieces(BLACK);
                System.out.println(board.getPiece(getNeighbor(king,"right",3)).getType());
                if(board.getPiece(getNeighbor(king,"right",3)).getType() == ROOK){
                    if(getNeighbor(king,"right",1) == null && getNeighbor(king,"right",2)==null){
                        for(int i = 1; i < 3; i++){
                            for(int j = 0; j < enemies.size(); j++){
                                if(enemies.get(i).getMovement().movePossible(enemies.get(j).getPosition(),getNeighbor(king,"right",i),true,false)){
                                    System.out.println(1);
                                    return false;
                                }
                            }
                        }
                    }
                    else{
                        System.out.println(2);
                        return false;
                    }
                }
                else if(board.getPiece(getNeighbor(king,"left",4)).getType() == ROOK) {
                    System.out.print("ok");
                    if (getNeighbor(king, "left", 1) == null && getNeighbor(king, "left", 2) == null && getNeighbor(king, "left", 3) == null) {
                        for (int i = 1; i < 4; i++) {
                            for (int j = 0; j < enemies.size(); j++) {
                                if (enemies.get(i).getMovement().movePossible(enemies.get(j).getPosition(), getNeighbor(king, "right", i), true, false)) {
                                    System.out.println(3);
                                    return false;
                                }
                            }

                        }
                    }
                    else{
                        System.out.println(4);
                        return false;
                    }
                }
            }
            case BLACK -> {
                ArrayList<BoardPiece> enemies = getColorPieces(WHITE);
                if(board.getPiece(getNeighbor(king,"right",4)).getType() == ROOK){
                    if(getNeighbor(king,"right",1) == null && getNeighbor(king,"right",2)==null  && getNeighbor(king, "right", 3) == null){
                        for(int i = 1; i < 3; i++){
                            for(int j = 0; j < enemies.size(); j++){
                                if(enemies.get(i).getMovement().movePossible(enemies.get(j).getPosition(),getNeighbor(king,"right",i),true,false)){
                                    return false;
                                }
                            }
                        }
                    }
                    else{
                        return false;
                    }
                }
                else if(board.getPiece(getNeighbor(king,"left",3)).getType() == ROOK) {
                    if (getNeighbor(king, "left", 1) == null && getNeighbor(king, "left", 2) == null) {
                        for (int i = 1; i < 4; i++) {
                            for (int j = 0; j < enemies.size(); j++) {
                                if (enemies.get(i).getMovement().movePossible(enemies.get(j).getPosition(), getNeighbor(king, "right", i), true, false)) {
                                    return false;
                                }
                            }

                        }
                    }
                    else{
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public String[] getKings() {
        String white = null, black = null;
        for (int i = 1; i <= Board.LENGTH; i++) {
            for (int j = 1; j <= Board.LENGTH; j++) {
                String toTest = stringifyMove(i,j);
                if (board.hasPiece(toTest)) {
                    BoardPiece pieceCur = board.getPiece(toTest);
                    if (pieceCur.getType() == KING) {
                        if (pieceCur.getColor() == WHITE) {
                            white = toTest;
                        }
                        else {
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
        int index = board.getIndex(from);
        BoardPiece movedPiece = board.getPiece(index);

        if (movedPiece.getColor() == WHITE) {
            return movedPiece.getMovement().movePossible(to, kings[1], false, true);
        }
        else {
            return movedPiece.getMovement().movePossible(to, kings[0], false, true);
        }
    }

    boolean isInCheckmate(String to, String from) {
        String[] kings = getKings();
        int movedIndex = board.getIndex(from);
        int kingIndex;
        BoardPiece movedPiece = board.getPiece(movedIndex);
        BoardPiece king;
        String pos;

        if (isInCheck(to, from)) {
            if (movedPiece.getColor() == WHITE) {
                kingIndex = board.getIndex(kings[1]);
                pos = kings[1];
            }
            else {
                kingIndex = board.getIndex(kings[0]);
                pos = kings[0];
            }
            king = board.getPiece(kingIndex);
            updateMoves(pos);

            return king.getAvailable_moves().size() == 0;
        }

        return false;
    }

    public Board getBoard(){
        return board;
    }
    public BoardPiece[] getPieces() {
        return pieces;
    }


}
