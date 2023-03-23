package com.game.ticTacToe.requests;

import lombok.Data;

@Data
public class MoveRequest {

    private Long gameId;
    private int positionX;
    private int positionY;
}
