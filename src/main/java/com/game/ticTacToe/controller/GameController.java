package com.game.ticTacToe.controller;

import com.game.ticTacToe.exception.GameFinishedException;
import com.game.ticTacToe.exception.GameNotFoundException;
import com.game.ticTacToe.models.Game;
import com.game.ticTacToe.requests.MatchHistoryRequest;
import com.game.ticTacToe.requests.MoveRequest;
import com.game.ticTacToe.requests.StartGameRequest;
import com.game.ticTacToe.responses.CreateGameResponse;
import com.game.ticTacToe.responses.MatchHistoryResponse;
import com.game.ticTacToe.responses.MoveResponse;
import com.game.ticTacToe.service.GameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    @PostMapping("/start")
    public ResponseEntity<CreateGameResponse> start(@RequestBody StartGameRequest request) {
        log.info("start game request: {}", request);
        return ResponseEntity.ok(gameService.createGame(request));
    }
    @PostMapping("/move")
    public ResponseEntity<MoveResponse> gamePlay(@RequestBody MoveRequest request) {
        log.info("move request: {}", request);
        MoveResponse game = gameService.move(request);
        return ResponseEntity.ok(game);
    }

    @GetMapping("/match-history")
    public ResponseEntity<MatchHistoryResponse> matchHistory(MatchHistoryRequest request) {
        log.info("match-history request: {}", request);
        MatchHistoryResponse matchHistory = gameService.matchHistory(request);
        log.info("match-history response: {}", matchHistory);
        return ResponseEntity.ok(matchHistory);
    }
}