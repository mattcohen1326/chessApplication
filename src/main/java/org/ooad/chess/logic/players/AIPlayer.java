package org.ooad.chess.logic.players;

import org.jetbrains.annotations.Nullable;
import org.ooad.chess.model.*;
import org.ooad.chess.model.player.AutoPlayer;
import org.ooad.chess.model.player.Player;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static org.ooad.chess.model.ChessmanTypes.KING;

public class AIPlayer extends Player implements AutoPlayer {

    private final GameDifficulty difficulty;

    public AIPlayer(ChessmanColor color, GameDifficulty difficulty) {
        super(color);
        this.difficulty = difficulty;
    }

    //following these values https://en.wikipedia.org/wiki/Chess_piece_relative_value
    private int cost(Board board, BoardPosition pos) {
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

    @Override
    public BoardMove computeMove(Board board) {
        if (difficulty == GameDifficulty.EASY) {
            return pickEasyMove(board);
        } else if (difficulty == GameDifficulty.MEDIUM) {
            return pickMediumMove(board);
        } else {
            return null;
        }
    }

    private BoardMove pickMediumMove(Board board) {
        //Idea: set values to capturing each piece, go through available moves, pick highest score.
        List<BoardPiece> pieces = computePieces(board);
        int totalPieces = pieces.size();
        int[] scores = new int[totalPieces];
        BoardMove[] places = new BoardMove[totalPieces];
        //for each peace
        for (int i = 0; i < totalPieces; i++) {
            BoardPiece sourcePiece = pieces.get(i);
            List<BoardPosition> moves = sourcePiece.getAvailableMoves();
            int max = 0;
            BoardPosition best = null;
            for (BoardPosition move : moves) {
                if (board.getPiece(move).getType() == KING) {
                    return new BoardMove(sourcePiece.getPosition(), move);
                }
                if (cost(board, move) > max) {
                    max = cost(board, move);
                    best = move;
                }
            }
            places[i] = new BoardMove(sourcePiece.getPosition(), best);
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

    private @Nullable BoardMove pickEasyMove(Board board) {
        return computePieces(board).stream()
                .flatMap(piece -> piece.getAvailableMoves().stream().map(it -> new BoardMove(piece.getPosition(), it)))
                .sorted((o1, o2) -> ThreadLocalRandom.current().nextInt(-1, 2))
                .findAny()
                .orElse(null);
    }

    private List<BoardPiece> computePieces(Board board) {
        return Arrays.stream(board.getPieces())
                .filter(it -> it != null && it.getColor() == color)
                .collect(Collectors.toList());
    }
}
