package com.game.ticTacToe.service;

import com.game.ticTacToe.database.GameSessions;
import com.game.ticTacToe.enums.GameStatus;
import com.game.ticTacToe.enums.Piece;
import com.game.ticTacToe.exception.GameFinishedException;
import com.game.ticTacToe.exception.GameNotFoundException;
import com.game.ticTacToe.exception.InvalidInputException;
import com.game.ticTacToe.models.Game;
import com.game.ticTacToe.requests.MatchHistoryRequest;
import com.game.ticTacToe.requests.MoveRequest;
import com.game.ticTacToe.requests.StartGameRequest;
import com.game.ticTacToe.responses.CreateGameResponse;
import com.game.ticTacToe.responses.MatchHistoryResponse;
import com.game.ticTacToe.responses.MoveResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
public class GameService {

    private final String DEFAULT_PLAYER_1_NAME = "Player1";
    private final String DEFAULT_PLAYER_2_NAME = "Player2";

    public CreateGameResponse createGame(StartGameRequest request){
        CreateGameResponse response = new CreateGameResponse();
        Game game = new Game();

        game.setGameBoard(new int [3][3]);
        game.setStatus(GameStatus.IN_PROGRESS.name());
        game.setPlayer1Name(DEFAULT_PLAYER_1_NAME);
        game.setPlayer2Name(DEFAULT_PLAYER_2_NAME);
        game.setMoveIndex(0);
        game.setTurn(Piece.O.getValue());
        game.setGameId(Long.valueOf(GameSessions.getInstance().getGameId()));

        if(request.getBoardSize() > 0){
            game.setGameBoard(new int [request.getBoardSize()][request.getBoardSize()]);
        }

        if(request.getPlayer1Name()!=null && !request.getPlayer1Name().isEmpty()){
            game.setPlayer1Name(request.getPlayer1Name());
        }

        if(request.getPlayer2Name()!=null && !request.getPlayer2Name().isEmpty()){
            game.setPlayer2Name(request.getPlayer2Name());
        }

        GameSessions.getInstance().setGame(game);

        response.setGame(game);
        response.setStatusCode(200);
        response.setStatusMessage("Success");

        return response;
    }

    public MoveResponse move(MoveRequest moveRequest) {
        MoveResponse response = new MoveResponse();
        try{
            if(!GameSessions.getInstance().getGameMap().containsKey(moveRequest.getGameId())){
                throw new GameNotFoundException("Invalid gameId, game not found.");
            }

            Game game = GameSessions.getInstance().getGameMap().get(moveRequest.getGameId());

            int boardSize = game.getGameBoard().length;

            if(!game.getStatus().equalsIgnoreCase(GameStatus.IN_PROGRESS.name())){
                throw new GameFinishedException("Game is already finished, no more moves will be accepted");
            }


            if(game.getGameBoard()[moveRequest.getPositionX()][moveRequest.getPositionY()] == 0){
                game.getGameBoard()[moveRequest.getPositionX()][moveRequest.getPositionY()] = game.getTurn();
            } else {
                response.setGame(game);
                throw new InvalidInputException("Cell has been filled, please choose an empty cell");
            }

            if(moveRequest.getPositionX() > boardSize-1 || moveRequest.getPositionY() > boardSize-1) {
                response.setGame(game);
                throw new InvalidInputException("Invalid coordinates detected, please input valid coordinates only");
            }

            checkWinner(boardSize, game, moveRequest);

            game.setMoveIndex(game.getMoveIndex()+1);

            if(game.getTurn()==1)
                game.setTurn(2);
            else
                game.setTurn(1);

            GameSessions.getInstance().setGame(game);

            response.setGame(game);

        } catch(GameNotFoundException e){
            response.setStatusCode(e.getErrorCode());
            response.setStatusMessage(e.getMessage());
            return response;
        } catch(GameFinishedException e){
            response.setStatusCode(e.getErrorCode());
            response.setStatusMessage(e.getMessage());
            return response;
        } catch(InvalidInputException e){
            response.setStatusCode(e.getErrorCode());
            response.setStatusMessage(e.getMessage());
            return response;
        } catch (Exception e){
            response.setStatusCode(500);
            response.setStatusMessage("An error occured");
            return response;
        }

        response.setStatusCode(200);
        response.setStatusMessage("Success");

        return response;
    }

    public Boolean checkWinner(int boardSize, Game game, MoveRequest moveRequest) {
        System.out.println("move index isinya " + game.getMoveIndex());
        if(game.getMoveIndex()+1 == (boardSize*boardSize)) {
            game.setStatus(GameStatus.DRAW.name());
            return true;
        } else if (game.getMoveIndex() < boardSize){
            return false;
        }
        Set<Integer> rowValues = new HashSet();
        Set<Integer> columnValues = new HashSet();
        Set<Integer> diagonal1Values = new HashSet();
        Set<Integer> diagonal2Values = new HashSet();

        for(int index = 0; index < boardSize; index++){
            rowValues.add(game.getGameBoard()[moveRequest.getPositionX()][index]);
            columnValues.add(game.getGameBoard()[index][moveRequest.getPositionY()]);
            diagonal1Values.add(game.getGameBoard()[index][index]);
            diagonal2Values.add(game.getGameBoard()[index][boardSize-index-1]);
        }
        if((rowValues.size() == 1 && !rowValues.contains(Piece.EMPTY.getValue()))
                || (columnValues.size() == 1 && !columnValues.contains(Piece.EMPTY.getValue()))
                || (diagonal1Values.size() == 1 && !diagonal1Values.contains(Piece.EMPTY.getValue()))
                || (diagonal2Values.size() == 1 && !diagonal2Values.contains(Piece.EMPTY.getValue()))) {
            if(game.getTurn() == 1){
                game.setStatus(GameStatus.O_WON.name());
            } else if(game.getTurn() == 2){
                game.setStatus(GameStatus.X_WON.name());
            }

            return true;
        }

        return false;

    }

    public MatchHistoryResponse matchHistory(MatchHistoryRequest request) {
        MatchHistoryResponse response = new MatchHistoryResponse();
        try{
            Map<Long, Game> map = GameSessions.getInstance().getGameMap();
            List<Game> gameList = new ArrayList<>(map.values());

            response.setGames(gameList);
        } catch (Exception e){
            response.setStatusCode(500);
            response.setStatusMessage("An error occured");
            return response;
        }

        response.setStatusCode(200);
        response.setStatusMessage("Success");

        return response;
    }

}
