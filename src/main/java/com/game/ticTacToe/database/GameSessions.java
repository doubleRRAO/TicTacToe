package com.game.ticTacToe.database;

import com.game.ticTacToe.models.Game;

import java.util.HashMap;
import java.util.Map;

public class GameSessions {
    private static Map<Long, Game> gameMap;
    private static GameSessions instance;

    public GameSessions() {
        this.gameMap = new HashMap<>();
    }

    public static synchronized GameSessions getInstance(){
        if(instance == null){
            instance = new GameSessions();
        }
        return instance;
    }

    public static Map<Long, Game> getGameMap() {
        return gameMap;
    }

    public static void setGame(Game game) {
        gameMap.put(game.getGameId(), game);
    }

    public static int getGameId(){
        return gameMap.size()+1;
    }
}
