package org.ooad.chess.model;
import org.junit.Assert;
import org.junit.Test;

public class EngineTest {
    Board b = Board.filledBoard();
    MoveEngine engine = new MoveEngine(b);
    @Test
    public void testPawnForwardOne(){
        for(int i = 1; i <= b.LENGTH; i++){
            Assert.assertEquals(false,engine.isBlocked(b.getPiece(engine.stringifyMove(2,i)).getMovement().movePath(engine.stringifyMove(2,i),engine.stringifyMove(3,i))));
            Assert.assertEquals(true,b.getPiece(engine.stringifyMove(2,i)).getMovement().movePossible(engine.stringifyMove(2,i),engine.stringifyMove(3,i),true,false));
            Assert.assertEquals(false,engine.isBlocked(b.getPiece(engine.stringifyMove(b.LENGTH-1,i)).getMovement().movePath(engine.stringifyMove(b.LENGTH-1,i),engine.stringifyMove(b.LENGTH-2,i))));
            Assert.assertEquals(true,b.getPiece(engine.stringifyMove(b.LENGTH-1,i)).getMovement().movePossible(engine.stringifyMove(b.LENGTH-1,i),engine.stringifyMove(b.LENGTH-2,i),true,false));
        }
    }
    @Test
    public void testEnPassant(){
        b.movePiece("B2","B3");
        b.movePiece("B3","B4");
        b.movePiece("B4","B5");
        b.movePiece("A7","A5");
        Assert.assertEquals(true, engine.isEliminating("B5","A6"));
        b = Board.filledBoard();

    }
    @Test
    public void checkPawnForwardTwo(){
        for(int i = 1; i <= b.LENGTH; i++){
            Assert.assertEquals(false,engine.isBlocked(b.getPiece(engine.stringifyMove(2,i)).getMovement().movePath(engine.stringifyMove(2,i),engine.stringifyMove(4,i))));
            Assert.assertEquals(true,b.getPiece(engine.stringifyMove(2,i)).getMovement().movePossible(engine.stringifyMove(2,i),engine.stringifyMove(4,i),true,false));
            Assert.assertEquals(false,engine.isBlocked(b.getPiece(engine.stringifyMove(b.LENGTH-1,i)).getMovement().movePath(engine.stringifyMove(b.LENGTH-1,i),engine.stringifyMove(b.LENGTH-3,i))));
            Assert.assertEquals(true,b.getPiece(engine.stringifyMove(b.LENGTH-1,i)).getMovement().movePossible(engine.stringifyMove(b.LENGTH-1,i),engine.stringifyMove(b.LENGTH-3,i),true,false));
        }
    }
    @Test
    public void checkPawnMoves(){
        b = new Board();
        BoardPiece p = new BoardPiece(ChessmanTypes.PAWN, ChessmanColor.WHITE);
        engine.setPiece("D3",p);
        BoardPiece enemy = new BoardPiece(ChessmanTypes.PAWN, ChessmanColor.BLACK);
        engine.setPiece("D4",enemy);
        engine.setPiece("C4",enemy);
        engine.setPiece("E4",enemy);
        Assert.assertEquals(true, engine.isBlocked(p.getMovement().movePath("D3","D4")));
        Assert.assertEquals(false, engine.isBlocked(p.getMovement().movePath("D3","C4")));
        Assert.assertEquals(false, engine.isBlocked(p.getMovement().movePath("D3","E4")));
        Assert.assertEquals(true,p.getMovement().movePossible("D3","C4",false,engine.isEliminating("D3","C4")));
    }
}
