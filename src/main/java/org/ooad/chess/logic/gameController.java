package org.ooad.chess.logic;
import org.ooad.chess.model.*;
import org.ooad.chess.logic.players.*;

import static org.ooad.chess.model.ChessmanColor.WHITE;

public class gameController {
    private final Board board;
    private final MoveEngine engine;
    private String state;
    private final Player[] players;
    private Player currentPlayer;

    public gameController(humanPlayer player, AIPlayer AI){
        board = Board.filledBoard();
        engine = new MoveEngine(board);
        if (player.getColor() == WHITE) {
            currentPlayer = player;
        }
        else {
            currentPlayer = AI;
        }
        players = new Player[]{player, AI};
        state = "start";
    }

    public gameController(humanPlayer player1, humanPlayer player2) {
        board = Board.filledBoard();
        engine = new MoveEngine(board);
        if (player1.getColor() == WHITE) {
            currentPlayer = player1;
        }
        else {
            currentPlayer = player2;
        }
        players = new Player[]{player1, player2};
        state = "start";
    }

    boolean isInCheckmate() {
        BoardPosition[] kings = engine.getKings();

        for (int i = 0; i < board.getPieces().length; i++) {
            BoardPiece piece = board.getPieces()[i];
            BoardPiece king;
            if (piece.getColor().equals(WHITE)) {
                king = board.getPiece(kings[1]);
            }
            else {
                king = board.getPiece(kings[0]);
            }

            engine.updateMoves(piece.getPosition().toString());
            engine.updateMoves(king.getPosition().toString());
            if (piece.getAvailableMoves().contains(king.getPosition().toString()) && king.getAvailableMoves().size() == 0) {
                return true;
            }
        }

        return false;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void iterateTurn() {
        if (currentPlayer == players[0]) {
            currentPlayer = players[1];
        }
        else {
            currentPlayer = players[0];
        }
    }

    public void playGame(){
        while(state != "finish") {
            switch (state) {
                case "start" -> {
                    //get the info from the gui and set player 1 and player2 to the appropriate types
                    state = "playing";
                }
                case "playing" -> {
                    while (!isInCheckmate()) {
                        //current player take turn.
                        if(currentPlayer instanceof AIPlayer){
                            ((AIPlayer) currentPlayer).pickMove();
                        }
                        else{
                            //get move from human player via gui
                            //then make the move
                        }
                        //after turn switch the current player
                        iterateTurn();
                        currentPlayer = getCurrentPlayer();
                    }
                    state = "finish";
                }
            }
        }
        //Exit the game and display stats?
    }



}
