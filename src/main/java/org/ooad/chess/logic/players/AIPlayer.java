package org.ooad.chess.logic.players;

import org.jetbrains.annotations.Nullable;
import org.ooad.chess.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.ooad.chess.model.ChessmanTypes.KING;

public class AIPlayer extends Player {
    private GameDifficulty difficulty;
    private List<BoardPiece> pieces;
    private Board board;


    public AIPlayer(ChessmanColor playerColor, Board gameBoard) {
        color = playerColor;
        board = gameBoard;
        pieces = new ArrayList<BoardPiece>();
        updatePieces(board);
    }
    public List<BoardPiece> getPieces(){
        return pieces;
    }
    //following these values https://en.wikipedia.org/wiki/Chess_piece_relative_value
    private int cost(BoardPosition pos) {
        if(board.getPiece(pos) == null){
            return 0;
        }
        switch (board.getPiece(pos).getType()) {
            case QUEEN -> {
                return 9;
            }
            case ROOK -> {
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

    public @Nullable BoardPosition pickMove() {
        if (difficulty == GameDifficulty.EASY) {
            return pickEasyMove();
        } else if (difficulty == GameDifficulty.MEDIUM) {
            return pickMediumMove();
        } else {
            return null;
        }
    }

    private BoardPosition pickMediumMove() {
        //Idea: set values to capturing each piece, go through available moves, pick highest score.
        int totalPieces = pieces.size();
        //System.out.println(totalPieces);
        int[] scores = new int[totalPieces];
        BoardPosition[] places = new BoardPosition[totalPieces];
        //for each peace
        for (int i = 0; i < totalPieces; i++) {
            List<BoardPosition> moves = pieces.get(i).getAvailableMoves();
            System.out.println(moves);
            int max = -1;
            BoardPosition best = null;
            for (BoardPosition move : moves) {
                if (board.getPiece(move) != null && board.getPiece(move).getType() == KING) {
                    return move;
                }
                if (cost(move) > max) {
                    max = cost(move);
                    best = move;
                }
            }
            places[i] = best;
            scores[i] = max;
        }
        int max_ind = 0;
        int high_score = 0;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] > high_score) {
                high_score = scores[i];
                max_ind = i;
            }
        }
        return places[max_ind];
    }

    private @Nullable BoardPosition pickEasyMove() {
        int total_pieces = pieces.size();
        Random rand = new Random(); //instance of random class
        //generate random values from 0-24
        int randomPiece = rand.nextInt(total_pieces);
        int movesSize = pieces.get(randomPiece).getAvailableMoves().size();
        int randomMove = rand.nextInt(movesSize);
        return pieces.get(randomPiece).getAvailableMoves().get(randomMove);
    }

    public void setDifficulty(GameDifficulty dif){
        difficulty = dif;
        return;
    }
    public void updatePieces(Board b){
        board = b;
        pieces = new ArrayList<BoardPiece>();
        for (int i = 0; i < board.getPieces().length; i++) {
            if (board.getPieces()[i] == null){
                continue;
            }
            else if (board.getPieces()[i].getColor() == color) {
                pieces.add(board.getPieces()[i]);
            }
        }
    }

}
