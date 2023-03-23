package com.game.ticTacToe.responses;

import com.game.ticTacToe.models.Game;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MatchHistoryResponse extends  Response{
    List<Game> games;
}
