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
    public void testHelp(){
        updateAllMoves();
        if(players[1] instanceof AIPlayer){
            System.out.println(((AIPlayer) players[1]).pickMove());
        }
    }
    private void updateAllMoves(){
        for(int i = 0; i < board.LENGTH; i++){
            for(int j = 0; j < board.LENGTH; j++){
                if(board.getPiece(new BoardPosition(i,j)) == null){
                    continue;
                }
                engine.updateMoves(new BoardPosition(i,j).toString());
                if(currentPlayer.getColor() == board.getPiece(new BoardPosition(i,j)).getColor() && board.getPiece(new BoardPosition(i,j)).getEnp()){
                    board.getPiece(new BoardPosition(i,j)).setEnp(false);
                }
            }
        }
        players[1].updatePieces(board);

    }

    public ChessmanColor isInCheckmate() {
        ChessmanColor kingColor = engine.isInCheck();
        BoardPiece king;
        BoardPosition[] kings = engine.getKings();
        if (kingColor != null) {
            if (kingColor == WHITE) {
                king = board.getPiece(kings[0]);
            }
            else {
                king = board.getPiece(kings[1]);
            }

            if (king.getAvailableMoves().size() == 0) {
                return king.getColor();
            }
        }

        return null;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    public void iterateTurn() {

        updateAllMoves();
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
                    while (isInCheckmate() == null) {
                        //current player take turn.
                        if(currentPlayer instanceof AIPlayer){
                            ((AIPlayer) currentPlayer).pickMove();
                        }
                        else{
                            //get move from human player via gui
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
