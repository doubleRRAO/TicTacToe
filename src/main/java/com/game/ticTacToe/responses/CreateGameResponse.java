package com.game.ticTacToe.responses;

import com.game.ticTacToe.models.Game;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateGameResponse extends  Response{
    Game game;
}
