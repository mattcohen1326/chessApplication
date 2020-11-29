package org.ooad.chess.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIPlayer {
    private String difficulty;
    private List<BoardPiece> pieces;
    private Board board;

    public AIPlayer(Board b){
        board = b;
        for (int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                StringBuilder str = new StringBuilder();
                str.append((char)(j+64));
                str.append(i);
                String check_pos = str.toString();
                if(board.hasPiece(check_pos)){
                    if(board.getPiece(check_pos).getColor() == ChessmanColor.WHITE){
                        pieces.add(board.getPiece(check_pos));
                    }
                }
            }
        }
    }
    //following these values https://en.wikipedia.org/wiki/Chess_piece_relative_value
    private int cost(String pos){
        switch(board.getPiece(pos).getType()){
            case QUEEN-> {
                return 9;
            }
            case ROOK->{
                return 5;
            }
            case PAWN -> {
                return 1;
            }
            default -> {
                return 3;
            }
        }
    }
    public String pickMove(){
        switch(difficulty){
            case "Easy" -> {
                int total_pieces = pieces.size();
                Random rand = new Random(); //instance of random class
                //generate random values from 0-24
                int randomPiece = rand.nextInt(total_pieces);
                int moves_size = pieces.get(randomPiece).getAvailable_moves().size();
                int randomMove = rand.nextInt(moves_size);
                String move = (String)pieces.get(randomPiece).getAvailable_moves().get(randomMove);
                return move;

            }
            case "Medium" ->{
                //Idea: set values to capturing each piece, go through available moves, pick highest score.
                int total_pieces = pieces.size();
                int[] scores = new int[total_pieces];
                String[] places = new String[total_pieces];
                //for each peace
                for(int i = 0; i < total_pieces; i++){
                    List<String> moves = pieces.get(i).getAvailable_moves();
                    int total_moves = moves.size();
                    int max = 0;
                    String best = "";
                    //for each move available to that piece
                    for(int j = 0; j < total_moves; j++) {
                        if(board.getPiece(moves.get(j)).getType() == ChessmanTypes.KING){
                            return moves.get(j);
                        }
                        if(cost(moves.get(j)) > max){
                            max = cost(moves.get(j));
                            best = moves.get(j);
                        }
                    }
                    places[i] = best;
                    scores[i] = max;
                }
                int max_ind = 0;
                int high_score = 0;
                for(int i = 0; i < scores.length; i ++){
                    if(scores[i] > high_score){
                        high_score = scores[i];
                        max_ind = i;
                    }
                }
                return places[max_ind];
            }
        }
        return "None";

    }
}
