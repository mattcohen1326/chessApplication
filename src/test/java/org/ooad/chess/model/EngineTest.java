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
        engine = new MoveEngine(b);
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
    @Test
    public void checkRookMoves(){
        b = new Board();
        engine = new MoveEngine(b);
        BoardPiece r = new BoardPiece(ChessmanTypes.ROOK,ChessmanColor.WHITE);
        engine.setPiece("A1",r);
        Assert.assertEquals(true,r.getMovement().movePossible("A1","A8",true,false));
        BoardPiece teammate = new BoardPiece(ChessmanTypes.PAWN,ChessmanColor.WHITE);
        BoardPiece enemy = new BoardPiece(ChessmanTypes.PAWN,ChessmanColor.BLACK);
        engine.setPiece("A5",teammate);
        engine.setPiece("B1",enemy);
        Assert.assertEquals(true,engine.isBlocked(r.getMovement().movePath("A1","A8")));
        Assert.assertEquals(false,engine.isBlocked(r.getMovement().movePath("A1","B1")));
        Assert.assertEquals(true,engine.isEliminating("A1","B1"));

    }
    @Test
    public void checkBishopMoves(){
        b = new Board();
        BoardPiece bishop = new BoardPiece(ChessmanTypes.BISHOP,ChessmanColor.WHITE);
        b.getEngine().setPiece("D4",bishop);
        BoardPiece enemy = new BoardPiece(ChessmanTypes.BISHOP,ChessmanColor.BLACK);
        b.getEngine().setPiece("E5",enemy);
        Assert.assertEquals(false,bishop.getMovement().movePossible("D4","D5",false,false));
        Assert.assertEquals(true,bishop.getMovement().movePossible("D4","F6",false,false));
        Assert.assertEquals(true,bishop.getMovement().movePossible("D4","A1",false,false));
        Assert.assertEquals(false,bishop.getMovement().movePossible("D4","A8",false,false));
        Assert.assertEquals(true,b.getEngine().isBlocked(bishop.getMovement().movePath("D4","F6")));
    }
}
