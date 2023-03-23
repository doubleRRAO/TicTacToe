package com.game.ticTacToe.models;

import lombok.Data;

@Data
public class Game {
    private Long gameId;
    private String status;
    private int moveIndex;
    private String player1Name;
    private String player2Name;
    private int[][] gameBoard;
    private int turn;

}
