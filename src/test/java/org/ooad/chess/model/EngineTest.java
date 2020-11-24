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
}
