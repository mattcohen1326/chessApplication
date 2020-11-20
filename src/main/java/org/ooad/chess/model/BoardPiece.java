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

    public List getAvailable_moves(){
        return available_moves;
    }
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

    /*public int[] getPosition(){
        return position;
    }*/

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
    public void updateMoves(String position, MoveEngine engine){
        int col = ((int)position.charAt(0))-64;
        int row = (int)position.charAt(1);
        switch(type){
            //TODO TEST THESE CASES
            case PAWN -> {
                if(this.first_move){
                    int new_row = 0;
                    switch(color){
                        case WHITE ->{
                            new_row = row - 2;
                            //boolean first_check = checkMove(new_row,col,board);
                        }
                        case BLACK ->{
                            new_row = row + 2;
                            //boolean first_check = checkMove(new_row,col,board)
                        }
                    }
                    boolean move = movement.movePossible(stringifyMove(row,col),stringifyMove(new_row,col),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(new_row,col)));
                    int move_pos = engine.isBlocked(movement.movePath(stringifyMove(row,col),stringifyMove(new_row,col)));
                    if(move && (move_pos == 0)){
                        available_moves.add(stringifyMove(new_row,col));
                    }
                }
                else{
                    switch(color){
                        case WHITE ->{
                            //check forward one
                            int new_row = row + 1;
                            int move_pos = engine.isBlocked(movement.movePath(stringifyMove(row,col),stringifyMove(new_row,col)));
                            if (move_pos == 0 && movement.movePossible(stringifyMove(row,col),stringifyMove(new_row,col),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(new_row,col)))){
                                available_moves.add(stringifyMove(new_row,col));
                            }
                            //check diagonals
                            //forward right
                            int new_col = col + 1;
                            move_pos = engine.isBlocked(movement.movePath(stringifyMove(row,col),stringifyMove(new_row,new_col)));
                            if(move_pos == 2 && movement.movePossible(stringifyMove(row,col),stringifyMove(new_row,new_col),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(new_row,new_col)))){
                                available_moves.add(stringifyMove(new_row,new_col));
                            }
                            //forward left
                            new_col = col - 1;
                            move_pos = engine.isBlocked(movement.movePath(stringifyMove(row,col),stringifyMove(new_row,new_col)));
                            if(move_pos == 2 && movement.movePossible(stringifyMove(row,col),stringifyMove(new_row,new_col),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(new_row,new_col)))){
                                available_moves.add(stringifyMove(new_row,new_col));
                            }
                        }
                        case BLACK ->{
                            //check forward one
                            int new_row = row - 1;
                            int move_pos = engine.isBlocked(movement.movePath(stringifyMove(row,col),stringifyMove(new_row,col)));
                            if ((move_pos == 0) && movement.movePossible(stringifyMove(row,col),stringifyMove(new_row,col),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(new_row,col)))){
                                available_moves.add(stringifyMove(new_row,col));
                            }
                            //forward left
                            new_row = row - 1;
                            int new_col = col - 1;
                            move_pos = engine.isBlocked(movement.movePath(stringifyMove(row,col),stringifyMove(new_row,new_col)));
                            if(move_pos == 2 && movement.movePossible(stringifyMove(row,col),stringifyMove(new_row,new_col),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(new_row,new_col)))){
                                available_moves.add(stringifyMove(new_row,new_col));
                            }
                            //forward right
                            new_col = col + 1;
                            move_pos = engine.isBlocked(movement.movePath(stringifyMove(row,col),stringifyMove(new_row,new_col)));
                            if(move_pos == 2 && movement.movePossible(stringifyMove(row,col),stringifyMove(new_row,new_col),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(new_row,new_col)))){
                                available_moves.add(stringifyMove(new_row,new_col));
                            }
                            }
                        }
                    }
                } //COMPLETE
            case ROOK -> {
                /*boolean vertical_complete = false;
                boolean horizontal_complete = false;
                if(row == 1){
                    //should replace i < 9 with i < board_rows or something like that
                    // add vertical moves
                    for(int i = 1; i < 9; i++){
                        int move_pos = engine.isBlocked(movement.movePath(stringifyMove(row,col),stringifyMove(i,col)));
                        if(movement.movePossible(stringifyMove(row,col),stringifyMove(i,col),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(i,col)))){
                            if(move_pos == 0){
                                available_moves.add(stringifyMove(i,col));
                            }
                            else if(move_pos == 1){
                                //path blocked
                                break;
                            }
                            else{
                                available_moves.add(stringifyMove(i,col));
                                break;
                            }
                        }
                    }
                    vertical_complete = true;
                }
                if(col == 1){
                    for(int i = 1; i < 9; i++){
                        int move_pos = engine.isBlocked(movement.movePath(stringifyMove(row,col),stringifyMove(row,i)));
                        if(movement.movePossible(stringifyMove(row,col),stringifyMove(row,i),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(row,i)))){
                            if(move_pos == 0){
                                available_moves.add(stringifyMove(row,i));
                            }
                            else if(move_pos == 1){
                                //path blocked
                                break;
                            }
                            else{
                                available_moves.add(stringifyMove(row,i));
                                break;
                            }
                        }
                    }
                    horizontal_complete = true;
                }
                else{

                 */
                for(int i = row; i > 0 ; i--) {
                    int move_pos = engine.isBlocked(movement.movePath(stringifyMove(row, col), stringifyMove(i, col)));
                    if (movement.movePossible(stringifyMove(row, col), stringifyMove(i, col), first_move, engine.isEliminating(stringifyMove(row, col), stringifyMove(i, col)))) {
                        if (move_pos == 0) {
                            available_moves.add(stringifyMove(row, i));
                        } else if (move_pos == 1) {
                            //path blocked
                            break;
                        } else {
                            available_moves.add(stringifyMove(i, col));
                            break;
                        }
                    }
                }
                for(int i = row; i <= engine.getBoard().LENGTH; i++){
                    int move_pos = engine.isBlocked(movement.movePath(stringifyMove(row, col), stringifyMove(i, col)));
                    if (movement.movePossible(stringifyMove(row, col), stringifyMove(i, col), first_move, engine.isEliminating(stringifyMove(row, col), stringifyMove(i, col)))) {
                        if (move_pos == 0) {
                            available_moves.add(stringifyMove(row, i));
                        } else if (move_pos == 1) {
                            //path blocked
                            break;
                        } else {
                            available_moves.add(stringifyMove(i, col));
                            break;
                        }
                    }
                }
                for(int i = col-1; i > 0; i--) {
                    int move_pos = engine.isBlocked(movement.movePath(stringifyMove(row, col), stringifyMove(i, col)));
                    if (movement.movePossible(stringifyMove(row, col), stringifyMove(i, col), first_move, engine.isEliminating(stringifyMove(row, col), stringifyMove(i, col)))) {
                        if (move_pos == 0) {
                            available_moves.add(stringifyMove(i,col));
                        } else if (move_pos == 1) {
                            //path blocked
                            break;
                        } else {
                            available_moves.add(stringifyMove(i, col));
                            break;
                        }
                    }
                }
                for(int i = col; i <= engine.getBoard().LENGTH; i++){
                    int move_pos = engine.isBlocked(movement.movePath(stringifyMove(row, col), stringifyMove(i, col)));
                    if (movement.movePossible(stringifyMove(row, col), stringifyMove(i, col), first_move, engine.isEliminating(stringifyMove(row, col), stringifyMove(i, col)))) {
                        if (move_pos == 0) {
                            available_moves.add(stringifyMove(i,col));
                        } else if (move_pos == 1) {
                            //path blocked
                            break;
                        } else {
                            available_moves.add(stringifyMove(i, col));
                            break;
                        }
                    }
                }


            } //COMPLETE
            case KNIGHT ->{
                //check all spaces for both colors
                //check up to the left and right first
                //Front Left
                int new_row = row + 2;
                int new_col = col - 1;
                int move_pos = engine.isBlocked(movement.movePath(stringifyMove(row,col),stringifyMove(new_row,new_col)));
                if((move_pos == 0 || move_pos ==  2) && movement.movePossible(stringifyMove(row,col),stringifyMove(new_row,new_col),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(new_row,new_col)))){
                    available_moves.add(stringifyMove(new_row,new_col));
                }
                //Front right
                new_col = col + 1;
                move_pos = engine.isBlocked(movement.movePath(stringifyMove(row,col),stringifyMove(new_row,new_col)));
                if((move_pos == 0 || move_pos == 2) && movement.movePossible(stringifyMove(row,col),stringifyMove(new_row,new_col),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(new_row,new_col)))){
                    available_moves.add(stringifyMove(new_row,new_col));
                }
                //Back Right
                new_row = row - 2;
                move_pos = engine.isBlocked(movement.movePath(stringifyMove(row,col),stringifyMove(new_row,new_col)));
                if((move_pos == 0 || move_pos == 2) && movement.movePossible(stringifyMove(row,col),stringifyMove(new_row,new_col),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(new_row,new_col)))){
                    available_moves.add(stringifyMove(new_row,new_col));
                }
                //Back Left
                new_col = col - 1;
                move_pos = engine.isBlocked(movement.movePath(stringifyMove(row,col),stringifyMove(new_row,new_col)));
                if((move_pos == 0 || move_pos == 2) && movement.movePossible(stringifyMove(row,col),stringifyMove(new_row,new_col),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(new_row,new_col)))){
                    available_moves.add(stringifyMove(new_row,new_col));
                }
                //Upper Left
                new_col = col - 2;
                new_row = row + 1;
                move_pos = engine.isBlocked(movement.movePath(stringifyMove(row,col),stringifyMove(new_row,new_col)));
                if((move_pos == 0 || move_pos == 2) && movement.movePossible(stringifyMove(row,col),stringifyMove(new_row,new_col),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(new_row,new_col)))){
                    available_moves.add(stringifyMove(new_row,new_col));
                }
                //Lower Left
                new_row = row - 1;
                move_pos = engine.isBlocked(movement.movePath(stringifyMove(row,col),stringifyMove(new_row,new_col)));
                if((move_pos == 0 || move_pos == 2) && movement.movePossible(stringifyMove(row,col),stringifyMove(new_row,new_col),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(new_row,new_col)))){
                    available_moves.add(stringifyMove(new_row,new_col));
                }
                //Upper right
                new_col = col + 2;
                new_row = row + 1;
                move_pos = engine.isBlocked(movement.movePath(stringifyMove(row,col),stringifyMove(new_row,new_col)));
                if((move_pos == 0 || move_pos == 2) && movement.movePossible(stringifyMove(row,col),stringifyMove(new_row,new_col),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(new_row,new_col)))){
                    available_moves.add(stringifyMove(new_row,new_col));
                }
                //Lower Right
                new_row = row + 1;
                move_pos = engine.isBlocked(movement.movePath(stringifyMove(row,col),stringifyMove(new_row,new_col)));
                if((move_pos == 0 || move_pos == 2) && movement.movePossible(stringifyMove(row,col),stringifyMove(new_row,new_col),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(new_row,new_col)))){
                    available_moves.add(stringifyMove(new_row,new_col));
                }
            } //COMPLETE
            case KING ->{
                //check all six spots around
                for(int i = (row-1);i <= (row+1); i++){
                    for(int j = (col-1); j <= (col+1); j++){
                        int move_pos = engine.isBlocked(movement.movePath(stringifyMove(row,col),stringifyMove(i,j)));
                        if((move_pos == 0 || move_pos == 2) && movement.movePossible(stringifyMove(row,col),stringifyMove(i,j),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(i,j)))){
                            available_moves.add(stringifyMove(i,j));
                        }
                    }
                }
            } //SEMI-COMPLETE: NEED TO INCORPERATE NO CHECK MATE
            case QUEEN ->{
                //combine knight and bishop logic
                int new_col = col+1;
                int move_pos = -1;
                for(int i = row+1; i <= engine.getBoard().LENGTH; i++){
                    if(new_col <= engine.getBoard().LENGTH){
                        move_pos = engine.isBlocked(movement.movePath(stringifyMove(row, col), stringifyMove(i, new_col)));
                    }
                    else{
                        break;
                    }
                    if(movement.movePossible(stringifyMove(row,col),stringifyMove(i,new_col),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(i,new_col)))){
                        if(move_pos == 0) {
                            available_moves.add(stringifyMove(i, new_col));
                        }
                        else if(move_pos == 1){
                            break;//blocked by teammate
                        }
                        else{
                            available_moves.add(stringifyMove(i, new_col));
                            break; //can capture piece
                        }
                    }

                    new_col++;
                } //Up and right diagonal
                for(int i = row-1; i > 0; i--){
                    if(new_col < 9){
                        move_pos = engine.isBlocked(movement.movePath(stringifyMove(row, col), stringifyMove(i, new_col)));
                    }
                    else{
                        break;
                    }
                    if(movement.movePossible(stringifyMove(row,col),stringifyMove(i,new_col),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(i,new_col)))){
                        if(move_pos == 0) {
                            available_moves.add(stringifyMove(i, new_col));
                        }
                        else if(move_pos == 1){
                            break;//blocked by teammate
                        }
                        else{
                            available_moves.add(stringifyMove(i, new_col));
                            break; //can capture piece
                        }
                    }
                    new_col++;
                } //Down and Right
                new_col = col - 1;
                for(int i =row+1;i<= engine.getBoard().LENGTH;i++){
                    if(new_col > 0){
                        move_pos = engine.isBlocked(movement.movePath(stringifyMove(row, col), stringifyMove(i, new_col)));
                    }
                    else{
                        break;
                    }
                    if(movement.movePossible(stringifyMove(row,col),stringifyMove(i,new_col),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(i,new_col)))){
                        if(move_pos == 0){
                            available_moves.add(stringifyMove(i,new_col));
                        }
                        else if(move_pos == 1){
                            break;
                        }
                        else{
                            available_moves.add(stringifyMove(i,new_col));
                            break;
                        }
                    }
                    new_col--;
                } //Up and Left diagonal
                for(int i = row-1; i > 0; i++){
                    if(new_col > 0){
                        move_pos = engine.isBlocked(movement.movePath(stringifyMove(row, col), stringifyMove(i, new_col)));
                    }
                    else{
                        break;
                    }
                    if(movement.movePossible(stringifyMove(row,col),stringifyMove(i,new_col),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(i,new_col)))){
                        if(move_pos == 0){
                            available_moves.add(stringifyMove(i,new_col));
                        }
                        else if(move_pos == 1){
                            break;
                        }
                        else{
                            available_moves.add(stringifyMove(i,new_col));
                            break;
                        }
                    }
                    new_col--;
                } // DOWN and LEft Diagonal
                for(int i = row; i > 0 ; i--) {
                    int move_pos_2 = engine.isBlocked(movement.movePath(stringifyMove(row, col), stringifyMove(i, col)));
                    if (movement.movePossible(stringifyMove(row, col), stringifyMove(i, col), first_move, engine.isEliminating(stringifyMove(row, col), stringifyMove(i, col)))) {
                        if (move_pos_2 == 0) {
                            available_moves.add(stringifyMove(row, i));
                        }
                        else if (move_pos_2 == 1) {
                            //path blocked
                            break;
                        }
                        else {
                            available_moves.add(stringifyMove(i, col));
                            break;
                        }
                    }
                }
                for(int i = row; i <= engine.getBoard().LENGTH; i++){
                    int move_pos_2 = engine.isBlocked(movement.movePath(stringifyMove(row, col), stringifyMove(i, col)));
                    if (movement.movePossible(stringifyMove(row, col), stringifyMove(i, col), first_move, engine.isEliminating(stringifyMove(row, col), stringifyMove(i, col)))) {
                        if (move_pos_2 == 0) {
                            available_moves.add(stringifyMove(row, i));
                        }
                        else if (move_pos_2 == 1) {
                            //path blocked
                            break;
                        }
                        else {
                            available_moves.add(stringifyMove(i, col));
                            break;
                        }
                    }
                }
                for(int i = col-1; i > 0; i--) {
                    int move_pos_2 = engine.isBlocked(movement.movePath(stringifyMove(row, col), stringifyMove(i, col)));
                    if (movement.movePossible(stringifyMove(row, col), stringifyMove(i, col), first_move, engine.isEliminating(stringifyMove(row, col), stringifyMove(i, col)))) {
                        if (move_pos_2 == 0) {
                            available_moves.add(stringifyMove(i,col));
                        }
                        else if (move_pos_2 == 1) {
                            //path blocked
                            break;
                        }
                        else {
                            available_moves.add(stringifyMove(i, col));
                            break;
                        }
                    }
                }
                for(int i = col; i <= engine.getBoard().LENGTH; i++){
                    int move_pos_2 = engine.isBlocked(movement.movePath(stringifyMove(row, col), stringifyMove(i, col)));
                    if (movement.movePossible(stringifyMove(row, col), stringifyMove(i, col), first_move, engine.isEliminating(stringifyMove(row, col), stringifyMove(i, col)))) {
                        if (move_pos_2 == 0) {
                            available_moves.add(stringifyMove(i,col));
                        } else if (move_pos_2 == 1) {
                            //path blocked
                            break;
                        } else {
                            available_moves.add(stringifyMove(i, col));
                            break;
                        }
                    }
                }

            } //COMPLETE
            case BISHOP -> {
                int new_col = col+1;
                int move_pos = -1;
                for(int i = row+1; i <= engine.getBoard().LENGTH; i++){
                    if(new_col <= engine.getBoard().LENGTH){
                        move_pos = engine.isBlocked(movement.movePath(stringifyMove(row, col), stringifyMove(i, new_col)));
                    }
                    else{
                        break;
                    }
                    if(movement.movePossible(stringifyMove(row,col),stringifyMove(i,new_col),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(i,new_col)))){
                        if(move_pos == 0) {
                            available_moves.add(stringifyMove(i, new_col));
                        }
                        else if(move_pos == 1){
                            break;//blocked by teammate
                        }
                        else{
                            available_moves.add(stringifyMove(i, new_col));
                            break; //can capture piece
                        }
                    }

                    new_col++;
                } //Up and right diagonal
                for(int i = row-1; i > 0; i--){
                    if(new_col <= engine.getBoard().LENGTH){
                        move_pos = engine.isBlocked(movement.movePath(stringifyMove(row, col), stringifyMove(i, new_col)));
                    }
                    else{
                        break;
                    }
                    if(movement.movePossible(stringifyMove(row,col),stringifyMove(i,new_col),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(i,new_col)))){
                        if(move_pos == 0) {
                            available_moves.add(stringifyMove(i, new_col));
                        }
                        else if(move_pos == 1){
                            break;//blocked by teammate
                        }
                        else{
                            available_moves.add(stringifyMove(i, new_col));
                            break; //can capture piece
                        }
                    }
                    new_col++;
                } //Down and Right
                new_col = col - 1;
                for(int i =row+1;i<= engine.getBoard().LENGTH;i++){
                    if(new_col > 0){
                        move_pos = engine.isBlocked(movement.movePath(stringifyMove(row, col), stringifyMove(i, new_col)));
                    }
                    else{
                        break;
                    }
                    if(movement.movePossible(stringifyMove(row,col),stringifyMove(i,new_col),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(i,new_col)))){
                        if(move_pos == 0){
                            available_moves.add(stringifyMove(i,new_col));
                        }
                        else if(move_pos == 1){
                            break;
                        }
                        else{
                            available_moves.add(stringifyMove(i,new_col));
                            break;
                        }
                    }
                    new_col--;
                } //Up and Left diagonal
                for(int i = row-1; i > 0; i++){
                    if(new_col > 0){
                        move_pos = engine.isBlocked(movement.movePath(stringifyMove(row, col), stringifyMove(i, new_col)));
                    }
                    else{
                        break;
                    }
                    if(movement.movePossible(stringifyMove(row,col),stringifyMove(i,new_col),first_move,engine.isEliminating(stringifyMove(row,col),stringifyMove(i,new_col)))){
                        if(move_pos == 0){
                            available_moves.add(stringifyMove(i,new_col));
                        }
                        else if(move_pos == 1){
                            break;
                        }
                        else{
                            available_moves.add(stringifyMove(i,new_col));
                            break;
                        }
                    }
                    new_col--;
                } // DOWN and LEft Diagonal
            } //COMPLETE

        }
    }

    private String stringifyMove(int row,int col){
        StringBuilder str = new StringBuilder();
        str.append((char)(col+64));
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

