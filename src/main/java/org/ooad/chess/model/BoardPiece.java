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
        int col = ((int)position.charAt(0))-64;
        int row = (int)position.charAt(1);
        switch(type){
            case PAWN -> {
                if(this.first_move){
                    switch(color){
                        case WHITE ->{
                            int new_row = row - 2;
                            boolean first_check = checkMove(new_row,col,board);
                            new_row = row - 1;
                            boolean second_check = checkMove(new_row,col,board);
                            if(first_check && second_check){
                                available_moves.add(stringifyMove(new_row,col));
                            }
                        }
                        case BLACK ->{
                            int new_row = row + 2;
                            boolean first_check = checkMove(new_row,col,board);
                            new_row = row + 1;
                            boolean second_check = checkMove(new_row,col,board);
                            if(first_check && second_check){
                                available_moves.add(stringifyMove(new_row,col));
                            }
                        }
                    }
                }
                else{
                    switch(color){
                        case WHITE ->{
                            //check forward one
                            int new_row = row + 1;
                            if (checkMove(new_row,col,board)){
                                available_moves.add(stringifyMove(new_row,col));
                            };
                            //check diagonals
                            //forward right
                            new_row = row  + 1;
                            int new_col = col + 1;
                            if(checkMove(new_row,new_col,board)){
                                available_moves.add(stringifyMove(new_row,new_col));
                            }
                            //forward left
                            new_col = col - 1;
                            if(checkMove(new_row,new_col,board)){
                                available_moves.add(stringifyMove(new_row,new_col));
                            }
                        }
                        case BLACK ->{
                            //check forward one
                            int new_row = row - 1;
                            if(checkMove(new_row,col,board)){
                                available_moves.add(stringifyMove(new_row,col));
                            }
                            //forward left
                            new_row = row - 1;
                            int new_col = col - 1;
                            if(checkMove(new_row,new_col,board)){
                                available_moves.add(stringifyMove(new_row,new_col));
                            }
                            //forward right
                            new_col = col + 1;
                            if(checkMove(new_row,new_col,board)){
                                available_moves.add(stringifyMove(new_row,new_col));
                            }
                            }
                        }
                    }
                }
                //TODO MAKE SURE YOU THINK ABOUT PIECES IN THE WAY
            case ROOK -> {
                for(int i = 1; i < 9; i++){
                    if(checkMove(row,i,board)){

                    }
                    checkMove(i,col,board);
                }
            }
            case KNIGHT ->{
                //check all spaces for both colors
                //check up to the left and right first
                //Front Left
                int new_row = row + 2;
                int new_col = col - 1;
                if(checkMove(new_row,new_col,board)){
                    available_moves.add(stringifyMove(new_row,new_col));
                }
                //Front right
                new_col = col + 1;
                if(checkMove(new_row,new_col,board)){
                    available_moves.add(stringifyMove(new_row,new_col));
                }
                //Back Right
                new_row = row - 2;
                if(checkMove(new_row,new_col,board)){
                    available_moves.add(stringifyMove(new_row,new_col));
                }
                //Back Left
                new_col = col - 1;
                if(checkMove(new_row,new_col,board)){
                    available_moves.add(stringifyMove(new_row,new_col));
                }
                //Upper Left
                new_col = col - 2;
                new_row = row + 1;
                if(checkMove(new_row,new_col,board)){
                    available_moves.add(stringifyMove(new_row,new_col));
                }
                //Lower Left
                new_row = row - 1;
                if(checkMove(new_row,new_col,board)){
                    available_moves.add(stringifyMove(new_row,new_col));
                }
                //Upper right
                new_col = col + 2;
                new_row = row + 1;
                if(checkMove(new_row,new_col,board)){
                    available_moves.add(stringifyMove(new_row,new_col));
                }
                //Lower Right
                new_row = row + 1;
                if(checkMove(new_row,new_col,board)){
                    available_moves.add(stringifyMove(new_row,new_col));
                }
            }
            case KING ->{
                //check all six spots around
                for(int i = (row-1);i <= (row+1); i++){
                    for(int j = (col-1); j <= (col+1); j++){
                        if(checkMove(i,j,board)){
                            available_moves.add(stringifyMove(i,j));
                        }
                    }
                }
            }
        }
    }

    private String stringifyMove(int row,int col){
        StringBuilder str = new StringBuilder();
        str.append((char)col+64);
        str.append(row);
        String check_pos = str.toString();
        return check_pos;
    }
    private boolean checkMove(int row, int col, Board board){
        if(!(row >= 1) || !(row<=8) || !(col>=1) || !(col<=8)){
            return false;
        }
        else{
            StringBuilder str = new StringBuilder();
            str.append((char)col+64);
            str.append(row);
            String check_pos = str.toString();
            if(!board.hasPiece(check_pos)){
                return true;
            }
            else if(board.getPiece(check_pos).getColor() != color){
                return true;
            }
        }

        return false;
    }
}

