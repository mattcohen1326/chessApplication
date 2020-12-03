package org.ooad.chess.model.player;

import org.ooad.chess.model.ChessmanColor;

public abstract class Player {

    protected ChessmanColor color;

    public Player(ChessmanColor color) {
        this.color = color;
    }

    public ChessmanColor getColor() {
        return color;
    }
}
