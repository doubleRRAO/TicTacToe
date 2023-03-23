package com.game.ticTacToe.exception;

public class GameFinishedException extends Exception{
    private String message;
    private final int errorCode = 502;

    public GameFinishedException(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
