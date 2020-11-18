package org.ooad.chess.model;

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
    public String makeMove(){
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
        }
        return "None";

    }
}
