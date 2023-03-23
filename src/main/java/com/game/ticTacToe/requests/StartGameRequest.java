package com.game.ticTacToe.requests;

import lombok.Data;

@Data
public class StartGameRequest {
    private int boardSize;
    private String player1Name;
    private String player2Name;
}
