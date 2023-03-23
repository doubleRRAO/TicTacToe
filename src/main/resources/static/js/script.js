const url = 'http://localhost:8080';
let gameId;
let boardSize;
let player1Name;
let player2Name;
var gameOver = true;
var turn = "";

function create_game() {
    if(!gameOver){
        alert("The current game is not over yet");
    }else{
       boardSize = document.getElementById("boardSize").value;
       player1Name = document.getElementById("player1Name").value;
       player2Name = document.getElementById("player2Name").value;
        $.ajax({
            url: url + "/game/start",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "boardSize": boardSize,
                "player1Name": player1Name,
                "player2Name": player2Name
            }),
            success: function (data) {
                gameId = data.game.gameId;
                turn = data.game.turn;
                player1Name = data.game.player1Name;
                player2Name = data.game.player2Name;
                prepareBoard(data.game.gameBoard);
//                alert("Your created a game. Game id is: " + data.game.gameId);

                whoseTurn(turn);
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
    gameOver = false;
}

function prepareBoard(gameboard){
    const tbody = document.getElementById("gameBoard");

    document.getElementById("matchup").innerHTML = player1Name + " VS " + player2Name;
    document.getElementById("matchup").color = "#fff";

    if(tbody.hasChildNodes()){
        tbody.innerHTML='';
    }

    boardSize = gameboard.length;

    for(let i = 0; i<boardSize; i++){
        const tr = document.createElement("tr");

        for(let j = 0; j<boardSize; j++){
            const td = document.createElement("td");

            var val = gameboard[j][i];

            if(val == 0){
                var btn = document.createElement('input');
                btn.type = "button";
                btn.className = "btn";
                td.id = i+","+j;
                td.align = "center";
                td.textAlign = "center";
                btn.onclick = (function(td) {return function() {move(td);}})(td);

                td.appendChild(btn);
            } else {
                if(val == 1){
                    td.innerHTML = "O";
                } else if (val == 2){
                    td.innerHTML = "X";
                }
            }

            tr.appendChild(td);
        }
        tbody.appendChild(tr);
    }
}

function move(coordinate) {
      const arr = coordinate.id.split(",");
      let positionY = arr[0];
      let positionX = arr[1];

        $.ajax({
            url: url + "/game/move",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "gameId": gameId,
                "positionX": positionX,
                "positionY": positionY
            }),
            success: function (data) {
                gameId = data.game.gameId;
                updateBoard(positionX,positionY, turn);
                turn = data.game.turn;
                if(data.game.status == "IN_PROGRESS"){
                    whoseTurn(turn);
                } else {
                    getResult(data.game.status);
                }
            },
            error: function (error) {
                console.log(error);
            }
        })
}

function updateBoard(j,i,turn){
    const td = document.getElementById(i+","+j);
    if(turn == 1){
        td.innerHTML = "O";
    } else if (turn == 2) {
        td.innerHTML = "X";
    }
}

function whoseTurn(turn){
    const message = document.getElementById("message");

    if(turn == 1){
        message.innerHTML = "O(" + player1Name +") Turn";
    } else if(turn == 2){
        message.innerHTML = "X(" + player2Name +") Turn";
   }

}

function getResult(result){
    gameOver = true;
    if(result == "O_WON"){
        message.innerHTML = "O(" + player1Name +") Won!!!";
    } else if(result == "X_WON"){
        message.innerHTML = "X(" + player2Name +") Won!!!";
    } else if(result == "DRAW"){
        message.innerHTML = "DRAW";
    }
}

function match_history(){
    console.log("requesting match-history");
    $.ajax({
                url: url + "/game/match-history",
                type: 'GET',
                contentType: "application/json",
                success: function (data) {
                    console.log(data);
                    match_history_table(data.games);
                },
                error: function (error) {
                    console.log(error);
                }
            })
}

function match_history_table(games){
    console.log("setting up table for match history");
    if(games != null){

        const tbody = document.getElementById("matchHistory");

        if(tbody.hasChildNodes()){
            tbody.innerHTML='';
        }

        const trHeader = document.createElement("tr");

//        const tdh1 = document.createElement("td");
//        tdh1.innerHTML = "No.";
//        trHeader.appendChild(tdh1);

        const tdh2 = document.createElement("td");
        tdh2.innerHTML = "Game Id";
        trHeader.appendChild(tdh2);

        const tdh3 = document.createElement("td");
        tdh3.innerHTML = "Player 1";
        trHeader.appendChild(tdh3);

        const tdh4 = document.createElement("td");
        tdh4.innerHTML = "Player 2";
        trHeader.appendChild(tdh4);

        const tdh5 = document.createElement("td");
        tdh5.innerHTML = "Result";
        trHeader.appendChild(tdh5);

        tbody.appendChild(trHeader);

        for(let i = 0; i<games.length; i++){
            const tr = document.createElement("tr");
            var game = games[i];
            var no = i+1;

//            const td1 = document.createElement("td");
//            td1.innerHTML = no;
//            tr.appendChild(td1);

            const td2 = document.createElement("td");
            td2.innerHTML = game.gameId;
            tr.appendChild(td2);

            const td3 = document.createElement("td");
            td3.innerHTML = game.player1Name;
            tr.appendChild(td3);

            const td4 = document.createElement("td");
            td4.innerHTML = game.player2Name;
            tr.appendChild(td4);

            const td5 = document.createElement("td");
            var gameStat = game.status;

            if(gameStat == "O_WON"){
                td5.innerHTML = "Player1(" + player1Name +") Won!!!";
                message.innerHTML = "O(" + player1Name +") Won!!!";
            } else if(gameStat == "X_WON"){
                td5.innerHTML = "Player2(" + player2Name +") Won!!!";
            } else {
                td5.innerHTML = gameStat;
            }

            tr.appendChild(td5);

            tbody.appendChild(tr);
        }
    }
}

function back_to_game(){

    window.location.assign("index.html");
}