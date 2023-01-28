# Pokemon game test

Test to simulate pokémon game with API

to get pokémen we use :  https://pokeapi.co/

This application use H2 memory database.
we added Postman collection to this project.

#  The steps below describe the nominal process for playing :

## 1-create your game 
    curl --location --request POST 'http://localhost:8080/game-management' \
    --header 'Content-Type: application/json' \
    --data-raw '{
    "name":"my Game"
    }'

## 2-Create players
  ### player 1
        curl --location --request POST 'http://localhost:8080/player-management' \
        --header 'Content-Type: application/json' \
        --data-raw '{
        "gameId":1,
        "name":"player 1"
        }'

  ### player 2
        curl --location --request POST 'http://localhost:8080/player-management' \
        --header 'Content-Type: application/json' \
        --data-raw '{
        "gameId":1,
        "name":"player 2"
        }'

## 3- Load pokemon list from https://pokeapi.co/ with:
    curl --location --request GET 'http://localhost:8080/pokemon-management/init'

## 4- Configure HP of pokemon
### pokemon 1
        curl --location --request PATCH 'http://localhost:8080/pokemon-management/1' \
        --header 'Content-Type: application/json' \
        --data-raw '{
        "pokemonId":1,
        "healthPoints":10
        }'
### pokemon 2
    curl --location --request PATCH 'http://localhost:8080/pokemon-management/2' \
    --header 'Content-Type: application/json' \
    --data-raw '{
    "pokemonId":2,
    "healthPoints":8
    }'

## 5- Select pokemon for each player:

### for player 1
    curl --location --request PATCH 'http://localhost:8080/player-management/1' \
    --header 'Content-Type: application/json' \
    --data-raw '{
    "playerId":1,
    "pokemonId":1
    }'
### for player 2

    curl --location --request PATCH 'http://localhost:8080/player-management/1' \
    --header 'Content-Type: application/json' \
    --data-raw '{
    "playerId":2,
    "pokemonId":2
    }'
## 6- Create battle 
    curl --location --request POST 'http://localhost:8080/battle-management' \
    --header 'Content-Type: application/json' \
    --data-raw '{
    "gameId":1,
    "name":"my battle"
    }'

## 7- Launch attack

### For normal attack
    curl --location --request PATCH 'http://localhost:8080/attack-management' \
    --header 'Content-Type: application/json' \
    --data-raw '{
    "battleId":1,
    "type":"NORMAL"
    }'
### For special attack
    curl --location --request PATCH 'http://localhost:8080/attack-management' \
    --header 'Content-Type: application/json' \
    --data-raw '{
    "battleId":1,
    "type":"SPECIAL"
    }'

## 8- Find winner player and pokemon
        curl --location --request GET 'http://localhost:8080/battle-management/1'

NB: "1" is battle id

## 9 - Try to relaunch other attack :
For example :

        curl --location --request PATCH 'http://localhost:8080/attack-management' \
        --header 'Content-Type: application/json' \
        --data-raw '{
        "battleId":1,
        "type":"SPECIAL"
        }'
It's impossible.
