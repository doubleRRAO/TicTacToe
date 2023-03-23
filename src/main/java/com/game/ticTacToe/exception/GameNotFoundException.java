package com.game.ticTacToe.exception;

public class GameNotFoundException extends Exception {
    private String message;
    private final int errorCode = 501;

    public GameNotFoundException(String message) {
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
