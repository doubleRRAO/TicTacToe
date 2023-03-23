package com.game.ticTacToe.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Piece {
    EMPTY(0),
    O(1),
    X(2);

    private int value;

    public int getValue() {
        return value;
    }
}
