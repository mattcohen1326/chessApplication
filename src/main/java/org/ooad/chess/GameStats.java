package org.ooad.chess;

import java.time.Duration;
import java.time.Instant;

public class GameStats {

    private int movesMade;
    private int eliminations;
    private int inCheck;
    private Instant start;
    private Instant end;
    private long total;

    public GameStats() {
        movesMade = 0;
        eliminations = 0;
        inCheck = 0;
        start = Instant.now();
    }

    public void iterateMoves() {
        movesMade++;
    }

    public void iterateEliminations() {
        eliminations++;
    }

    public void iterateInCheck() {
        inCheck++;
    }

    public void finishGame() {
        end = Instant.now();
        total = Duration.between(start, end).toMinutes();
    }

    public void display() {
        /*TODO
            implement GUI for displaying stats
         */
    }
}
