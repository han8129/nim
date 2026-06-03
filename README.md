# Misère Nim

Nim game where the player remove the last match lose

Rules
- Game is played with 1 heap - 1 singe collection of object
- One single shared game at any time
- Computer always take turn after human
- 1-3 can be taken each turn
- Player make move that make the heap reduce to 0 loses

## Build & Run

### Env
Using guix
```shell
guix shell --pure -m manifest.scm
```

Using Docker
```shell
docker compose up -d app
```
### Build

```sh
./gradlew build
```

### Run

```sh
./gradlew bootRun
```

### Sanity check
```shell
curl -X GET http://localhost:8080/actuator/health
```

## Gameplay
A game with heap of 0 is initialized with the app

1. Start new game with a heap size greater than 0
```sh
curl -X POST http://localhost:8080/api/games \
  -H "Content-Type: application/json" \
  -d '{"heap": 12}'
```
2. Make move with number of match to take from heap (1-3)
```sh
curl -X POST http://localhost:8080/api/games/move \
  -H "Content-Type: application/json" \
  -d '{"take": 2}'
```
   - Computer automatically takes its turn after your move
   - Response includes updated game state and computer's move
3. Get current game state
```sh
curl http://localhost:8080/api/games
```
   - Returns heap size, whose turn it is, and winner if game is over
4. Set computer strategy
```sh
curl -X PUT http://localhost:8080/api/computer/strategy \
  -H "Content-Type: application/json" \
  -d '{"strategy": "optimal"}'
```
   - `optimal`: Computer plays the winning strategy (takes `heap mod 4` matches)
   - `random`: Computer takes a random valid number of matches (1-3)