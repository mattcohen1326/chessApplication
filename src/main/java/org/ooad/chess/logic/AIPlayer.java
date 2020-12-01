package org.ooad.chess.logic;

import org.jetbrains.annotations.Nullable;
import org.ooad.chess.model.*;

import java.util.List;
import java.util.Random;

import static org.ooad.chess.model.ChessmanTypes.KING;

public class AIPlayer {
    private GameDifficulty difficulty;
    private List<BoardPiece> pieces;
    private Board board;

    public AIPlayer(Board board) {
        this.board = board;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                BoardPosition check_pos = new BoardPosition(i, j);
                if (this.board.hasPiece(check_pos)) {
                    if (this.board.getPiece(check_pos).getColor() == ChessmanColor.WHITE) {
                        pieces.add(this.board.getPiece(check_pos));
                    }
                }
            }
        }
    }

    //following these values https://en.wikipedia.org/wiki/Chess_piece_relative_value
    private int cost(BoardPosition pos) {
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
        int[] scores = new int[totalPieces];
        BoardPosition[] places = new BoardPosition[totalPieces];
        //for each peace
        for (int i = 0; i < totalPieces; i++) {
            List<BoardPosition> moves = pieces.get(i).getAvailableMoves();
            int max = 0;
            BoardPosition best = null;
            for (BoardPosition move : moves) {
                if (board.getPiece(move).getType() == KING) {
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
}
