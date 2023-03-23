package com.game.ticTacToe.exception;

public class InvalidInputException extends Exception {
    private String message;
    private final int errorCode = 503;

    public InvalidInputException(String message) {
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
