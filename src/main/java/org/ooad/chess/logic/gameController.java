package org.ooad.chess.logic;
import org.ooad.chess.model.*;
import org.ooad.chess.logic.players.*;

import static org.ooad.chess.model.ChessmanColor.WHITE;

public class gameController {
    private Board board;
    private MoveEngine engine;
    private String state;
    private Player player1;
    private Player player2;
    private Player currentPlayer;

    public gameController(){
        board = Board.filledBoard();
        engine = new MoveEngine(board);
        currentPlayer = player1;
        state = "start";
    }
    //check to see if any piece is in checkMate (need to update the inCheckMate function)
    //TODO

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

    private Player nextPlayer(Player p){
        if (p == player1){
            return player2;
        }
        else{
            return player1;
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
                        currentPlayer = nextPlayer(currentPlayer);
                    }
                    state = "finish";
                }
            }
        }
        //Exit the game and display stats?
    }



}
