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

    public void updateMoves(String position) {
        BoardPiece piece = getPiece(position);
        //System.out.println(piece.getPosition().toString());
        if(piece == null){
            return;
        }
        List<String> availableMoves = new ArrayList<String>();

        for (int i = 1; i <= LENGTH; i++) {
            for (int j = 1; j <= LENGTH; j++) {
                boolean blocked = isBlocked(piece.getMovement().movePath(position, stringifyMove(i, j)));
                if (!blocked && piece.getMovement().movePossible(position, stringifyMove(i, j), piece.getFirst(), isEliminating(position, stringifyMove(i, j)))) {
                    if (piece.getType() == PAWN){
                        //System.out.println((int)piece.getPosition().toString().charAt(0)-63);
                        //TODO MAKE THIS SPECIFIC TO COLOR
                        switch(piece.getColor()){
                            case WHITE -> {
                                //System.out.println(i);
                                //System.out.println(((int)piece.getPosition().toString().charAt(1))-48);
                                if(i < ((int)piece.getPosition().toString().charAt(1))-48 ){
                                    continue;
                                }
                                else{
                                    //System.out.println(stringifyMove(i,j));
                                    //System.out.println(i);

                                    availableMoves.add(stringifyMove(i,j));
                                }
                            }
                            case BLACK -> {

                                if(i > ((int)piece.getPosition().toString().charAt(1))-48){
                                    continue;
                                }
                                else{
                                    //System.out.println(stringifyMove(i,j));
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
                        //System.out.println(stringifyMove(i,j));
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




    public void movePiece(String from, String to) {
        /*if (hasPiece(to)) {
            throw new IllegalStateException(String.format("Cannot move %s-%s, %s is not empty", from, to, to));
        }*/
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
        /*if (!sourcePiece.getMovement().movePossible(from, to, sourcePiece.getFirst(), isEliminating(from, to))) {
            throw new IllegalStateException(String.format("Cannot move %s-%s, invalid move for type %s", from, to, sourcePiece.getType()));
        }*/
        updateMoves(from);
        if (!sourcePiece.getAvailableMoves().contains(toPos)) {
            throw new IllegalStateException(String.format("Cannot move %s-%s, invalid move for type %s", from, to, board.getPiece(fromPosition).getType()));
        }

        /*List<String> path = sourcePiece.getMovement().movePath(from, to);
        if (isBlocked(path)) {
            throw new IllegalStateException(String.format("Cannot move %s-%s, move blocked!", from, to));
        }*/

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

    public boolean isBlocked(List<String> path) {
        BoardPiece current = getPiece(path.get(0));
        if(current == null){
            return true;
        }
        boolean valid = true;
        if (current.getType() != ChessmanTypes.KNIGHT) {
            //System.out.println(path);

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
        }
        else {
            if (current.getType() == PAWN) {
                //System.out.println(post + ", " + col_diff + " , " + row_diff);
                //System.out.println(getPiece(post).getColor());
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
            System.out.println(1);
            return false;
        }
        ChessmanColor color = getPiece(king).getColor();
        if (!getPiece(king).getFirst() || !getPiece(rook).getFirst()) {
            System.out.println(2);
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

    public ChessmanColor isInCheck() {
        BoardPosition[] kings = getKings();

        for (int i = 0; i < board.getPieces().length; i++) {
            BoardPiece piece = board.getPieces()[i];
            BoardPiece king = null;
            if (piece != null) {
                if (piece.getColor().equals(WHITE)) {
                    king = board.getPiece(kings[1]);
                } else {
                    king = board.getPiece(kings[0]);
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

    public boolean movesToCheck(String to, ChessmanColor color) {
        for (int i = 0; i < board.getPieces().length; i++) {
            BoardPiece piece = board.getPieces()[i];
            if (piece != null) {
                if (piece.getColor() != color) {
                    if (piece.getMovement().movePossible(piece.getPosition().toString(), to, false, true)) {
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
                                    if (!isBlocked(path)) {
                                        return true;
                                    }
                                } else {
                                    boolean blocked = false;
                                    for (String s : path) {
                                        BoardPiece test = getPiece(s);
                                        if (test != null && test != board.getKing(color) && test != piece) {
                                            blocked = true;
                                        }
                                    }
                                    return !blocked;
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

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
