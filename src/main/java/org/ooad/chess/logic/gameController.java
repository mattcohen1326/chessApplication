package org.ooad.chess.logic;
import org.ooad.chess.model.*;
import org.ooad.chess.logic.players.*;
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
    private boolean gameOver(){
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
                    while (!gameOver()) {
                        //current player take turn.
                        if(currentPlayer instanceof AIPlayer){
                            ((AIPlayer) currentPlayer).pickMove();
                        }
                        else{
                            //get move from human player via gui
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
