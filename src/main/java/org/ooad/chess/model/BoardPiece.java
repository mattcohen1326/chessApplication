package org.ooad.chess.model;

import org.ooad.chess.model.behaviors.*;
import java.util.*;
/**
 * Represents a piece on a Chess board.
 */
public class BoardPiece {

    private final ChessmanTypes type;
    private final ChessmanColor color;
    private List available_moves;
    private MoveStrategy movement;
    private boolean first_move = true;
    public BoardPiece(ChessmanTypes type, ChessmanColor color) {
        this.type = type;
        this.color = color;

        setMovementBehavior(type);
    }

    public ChessmanColor getColor() {
        return color;
    }

    public ChessmanTypes getType() {
        return type;
    }

    public void setFirstMove(boolean first){
        this.first_move = first;
        return;
    }

    public MoveStrategy getMovement() {
        return movement;
    }

    public void setMovementBehavior(ChessmanTypes type) {
        switch (type) {
            case PAWN -> this.movement = new PawnMovement();
            case ROOK -> this.movement = new RookMovement();
            case KNIGHT -> this.movement = new KnightMovement();
            case BISHOP -> this.movement = new BishopMovement();
            case QUEEN -> this.movement = new QueenMovement();
            case KING -> this.movement = new KingMovement();
        }
    }
    // Assumes that the White pieces will be at the top of the board and the black pieces will be at the bottom (1,1)
    public void updateMoves(String position, Board board){
        int col = (int)position.charAt(0);
        int row = (int)position.charAt(1);
        switch(type){
            case PAWN -> {
                if(this.first_move){
                    switch(color){
                        case WHITE ->{
                            int new_row = row - 2;
                            StringBuilder new_pos = new StringBuilder();
                            new_pos.append(position.charAt(0));
                            new_pos.append(new_row);
                            String check_pos = new_pos.toString();
                            if(!board.hasPiece(check_pos)){
                                available_moves.add(check_pos);
                            }
                        }
                        case BLACK ->{
                            int new_row = row + 2;
                            StringBuilder new_pos = new StringBuilder();
                            new_pos.append(position.charAt(0));
                            new_pos.append(new_row);
                            String check_pos = new_pos.toString();
                            if(!board.hasPiece(check_pos)){
                                available_moves.add(check_pos);
                            }
                        }
                    }
                }
                else{
                    switch(color){
                        case WHITE ->{
                            //check forward one
                            int new_row = row + 1;
                            StringBuilder new_pos = new StringBuilder();
                            new_pos.append(position.charAt(0));
                            new_pos.append(new_row);
                            String check_pos = new_pos.toString();
                            if(!board.hasPiece(check_pos)) {
                                available_moves.add(check_pos);
                            }
                            //check diagonals
                            //forward right
                            new_row = row  + 1;
                            int new_col = col + 1;
                            new_pos.setLength(0);
                            new_pos.append((char)new_col);
                            new_pos.append(new_row);
                            check_pos = new_pos.toString();
                            if(!board.hasPiece(check_pos)) {
                                available_moves.add(check_pos);
                            }
                            else if(board.getPiece(check_pos).getColor() == ChessmanColor.BLACK){
                                available_moves.add(check_pos);
                            }
                            //forward left
                            new_col = col - 1;
                            new_pos.setLength(0);
                            new_pos.append((char)new_col);
                            new_pos.append(new_row);
                            check_pos = new_pos.toString();
                            if(!board.hasPiece(check_pos)) {
                                available_moves.add(check_pos);
                            }
                            else if(board.getPiece(check_pos).getColor() == ChessmanColor.BLACK){
                                available_moves.add(check_pos);
                            }
                        }
                        case BLACK ->{
                            //check forward one
                            int new_row = row - 1;
                            StringBuilder new_pos = new StringBuilder();
                            new_pos.append(position.charAt(0));
                            new_pos.append(new_row);
                            String check_pos = new_pos.toString();
                            if(!board.hasPiece(check_pos)) {
                                available_moves.add(check_pos);
                            }
                            //forward left
                            new_row = row - 1;
                            int new_col = col + 1;
                            new_pos.setLength(0);
                            new_pos.append((char)new_col);
                            new_pos.append(new_row);
                            check_pos = new_pos.toString();
                            if(!board.hasPiece(check_pos)) {
                                available_moves.add(check_pos);
                            }
                            else if(board.getPiece(check_pos).getColor() == ChessmanColor.WHITE){
                                available_moves.add(check_pos);
                            }
                            //forward right
                            new_col = col - 1;
                            new_pos.setLength(0);
                            new_pos.append((char)new_col);
                            new_pos.append(new_row);
                            check_pos = new_pos.toString();
                            if(!board.hasPiece(check_pos)) {
                                available_moves.add(check_pos);
                            }
                            else if(board.getPiece(check_pos).getColor() == ChessmanColor.WHITE){
                                available_moves.add(check_pos);
                            }
                        }
                    }
                }
            }
            case ROOK -> {
                StringBuilder str = new StringBuilder();
                for(int i = 1; i < 9; i++){
                    str.append(position.charAt(0));
                    str.append(i);
                    String check_pos = str.toString();
                    if(!board.hasPiece(check_pos)){
                        available_moves.add(check_pos);
                    }
                    else if(board.getPiece(check_pos).getColor() != color){
                        available_moves.add(check_pos);
                    }
                    str.setLength(0);
                    char new_col = (char)(i+64);
                    str.append(new_col);
                    str.append(row);
                    check_pos = str.toString();
                    if(!board.hasPiece(check_pos)){
                        available_moves.add(check_pos);
                    }
                    else if(board.getPiece(check_pos).getColor() != color){
                        available_moves.add(check_pos);
                    }
                }
            }
            case KNIGHT ->{
                //check all spaces for both colors
                //check up to the left and right first
                //Front Left
                int new_row = row + 2;
                int new_col = col - 1;
                StringBuilder str = new StringBuilder();
                str.append((char)new_col);
                str.append((char)new_row);
                String check_pos = str.toString();
                if(!board.hasPiece(check_pos)){
                    available_moves.add(check_pos);
                }
                else if(board.getPiece(check_pos).getColor() != color){
                    available_moves.add(check_pos);
                }
                //Front right
                str.setLength(0);
                new_col = col + 1;
                str.append((char)new_col);
                str.append(new_row);
                check_pos = str.toString();
                if(!board.hasPiece(check_pos)){
                    available_moves.add(check_pos);
                }
                else if(board.getPiece(check_pos).getColor() != color){
                    available_moves.add(check_pos);
                }
                //Back Right
                str.setLength(0);
                new_row = row - 2;
                str.append((char)new_col);
                str.append(new_row);
                check_pos = str.toString();
                if(!board.hasPiece(check_pos)){
                    available_moves.add(check_pos);
                }
                else if(board.getPiece(check_pos).getColor() != color){
                    available_moves.add(check_pos);
                }
                //Back Left
                str.setLength(0);
                new_col = col - 1;
                str.append((char)new_col);
                str.append(new_row);
                check_pos = str.toString();
                if(!board.hasPiece(check_pos)){
                    available_moves.add(check_pos);
                }
                else if(board.getPiece(check_pos).getColor() != color){
                    available_moves.add(check_pos);
                }
                //Upper Left
                str.setLength(0);
                new_col = col - 2;
                new_row = row + 1;
                str.append((char)new_col);
                str.append(new_row);
                check_pos = str.toString();
                if(!board.hasPiece(check_pos)){
                    available_moves.add(check_pos);
                }
                else if(board.getPiece(check_pos).getColor() != color){
                    available_moves.add(check_pos);
                }
                //Lower Left
                str.setLength(0);
                new_row = row - 1;
                str.append((char)new_col);
                str.append(new_row);
                check_pos = str.toString();
                if(!board.hasPiece(check_pos)){
                    available_moves.add(check_pos);
                }
                else if(board.getPiece(check_pos).getColor() != color){
                    available_moves.add(check_pos);
                }
                //Upper right
                str.setLength(0);
                new_col = col + 2;
                new_row = row + 1;
                str.append((char)new_col);
                str.append(new_row);
                check_pos = str.toString();
                if(!board.hasPiece(check_pos)){
                    available_moves.add(check_pos);
                }
                else if(board.getPiece(check_pos).getColor() != color){
                    available_moves.add(check_pos);
                }
                //Lower Right
                str.setLength(0);
                new_row = row + 1;
                str.append((char)new_col);
                str.append(new_row);
                check_pos = str.toString();
                if(!board.hasPiece(check_pos)){
                    available_moves.add(check_pos);
                }
                else if(board.getPiece(check_pos).getColor() != color){
                    available_moves.add(check_pos);
                }

            }
        }
    }
}

