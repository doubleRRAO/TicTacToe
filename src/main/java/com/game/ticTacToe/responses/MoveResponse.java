package com.game.ticTacToe.responses;

import com.game.ticTacToe.models.Game;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MoveResponse extends  Response{
    Game game;
}
