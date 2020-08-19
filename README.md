# Tic Tac Toe

This REST API designed to act as a server part of the "Tic Tac Toe" game.

Implemented using **Java 11 and Spring Boot 2**.

End-points served by this API can be easily watched by navigating following url: http://localhost:9090/swagger-ui.html

This is java backend part of the `tic-tac-toe` project. You can compile it via:

```bash
./mvnw -U -DskipTests clean compile package
```

It creates a `tic-tac-toe.jar` file in the "target" directory. You can run it directly:

```bash
java -jar target/tic-tac-toe.jar
```

If you want to create a docker image:

```bash
docker build . -t ozgurclub/tic-tac-toe
```

then run with docker: 

```bash
docker run -p 9090:9090 ozgurclub/tic-tac-toe
```

# Improvement areas: 
  - Client side development
  - Code coverage 
  - Documentation
  
# Sample Curl Requests

- to start new game:
```curl  -X POST  'http://localhost:9090/startNewGame?userName=42'```

- to update row and column sample:
```curl -X PATCH http://localhost:9090/executeTurn/42/8796c5e2-beb9-4b57-8e09-88996fd72bf3 -d "row=2&column=0"```

- to get top 10 request ;
``` curl -X GET localhost:9090/scoreBoard```

# Sample End Of Game JSON

`Game ended with : ENDED_WITH_WINNER`
`winner is current user`
```json
{
  "type": "INFO",
  "msgId": "moveExecuted",
  "data": {
    "gameId": "8796c5e2-beb9-4b57-8e09-88996fd72bf3",
    "board": [
      [
        "O",
        "O",
        "-"
      ],
      [
        "-",
        "-",
        "-"
      ],
      [
        "X",
        "X",
        "X"
      ]
    ],
    "player": {
      "username": "42",
      "marker": "X"
    },
    "currentPlayer": {
      "username": "42",
      "marker": "X"
    },
    "status": "ENDED_WITH_WINNER",
    "gameStartedTime": "2020-06-04T13:41:01.369297",
    "gameEndedTime": "2020-06-04T13:46:12.268946",
    "score": -10,
    "boardFull": false
  },
  "text": "moveExecuted"
} 
```



