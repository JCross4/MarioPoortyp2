# MarioPoorty - Multiplayer Board Game with Socket Communication

## Overview
MarioPoorty is a multiplayer board game where players connect through network sockets. The game features:
- A game board with 40 spaces (casillas)
- Multiple players moving around the board
- Different types of spaces (normal, traps, prizes, special events)
- Server-client architecture using Java sockets
- Real-time multiplayer gameplay

## Architecture

### Core Classes

#### 1. **Casilla (Space/Cell)**
Represents individual spaces on the board.
- Properties:
  - `id`: Unique identifier
  - `nombre`: Space name
  - `tipo`: Type of space (normal, especial, trampa, premio)
  - `valor`: Monetary value or effect
  - `jugadorActual`: Current player on this space

#### 2. **Jugador (Player)**
Represents a game player.
- Properties:
  - `id`: Player ID
  - `nombre`: Player name
  - `dinero`: Money amount
  - `casillaActual`: Current space
  - `posicionTablero`: Current board position (0-39)
  - `activo`: Whether player is still in game
- Methods:
  - `mover()`: Move player a certain number of spaces
  - `agregarDinero()`: Add money
  - `restarDinero()`: Subtract money

#### 3. **Tablero (Board)**
Manages the game board and all players.
- Properties:
  - `casillas`: List of spaces
  - `jugadores`: List of players
  - `tamano`: Board size (default: 40)
  - `nombre`: Board name
- Methods:
  - `agregarJugador()`: Add player to board
  - `removerJugador()`: Remove player from board
  - `moverJugador()`: Move a player and apply space effects

#### 4. **GameMessage**
Serializable message protocol for network communication.
- Message Types:
  - `CONNECT`: Player connection
  - `DISCONNECT`: Player disconnection
  - `DICE_ROLL`: Dice roll action
  - `PLAYER_MOVED`: Player movement
  - `BOARD_UPDATE`: Board state update
  - `PLAYER_JOIN`: New player joined
  - `GAME_START/END`: Game state changes

#### 5. **GameServer**
Main server managing the game.
- Responsibilities:
  - Accept client connections
  - Manage game board
  - Broadcast game state to all players
  - Handle player movements and turns
  - Process game messages

#### 6. **PlayerConnection**
Handles communication with a single connected player (runs on server).
- Responsibilities:
  - Manage individual client connection
  - Receive player actions
  - Send updates to that player
  - Handle disconnections

#### 7. **GameClient**
Client application that connects to the server.
- Responsibilities:
  - Connect to game server
  - Send player actions (dice rolls, movements)
  - Receive board updates
  - Display game state to player
  - Handle menu interactions

## How to Run

### Prerequisites
- Java 8 or higher
- Maven (for compilation)

### Compilation
```bash
# In the project root directory
mvn clean compile
```

### Running the Server
```bash
# Default port (5000)
mvn exec:java -Dexec.mainClass="Main.MarioPoorty" -Dexec.args="server"

# Custom port
mvn exec:java -Dexec.mainClass="Main.MarioPoorty" -Dexec.args="server 5001"
```

### Running a Client
```bash
# Connect to localhost on default port with name "Mario"
mvn exec:java -Dexec.mainClass="Main.MarioPoorty" -Dexec.args="client localhost 5000 Mario"

# Connect to remote server
mvn exec:java -Dexec.mainClass="Main.MarioPoorty" -Dexec.args="client 192.168.1.100 5000 Luigi"
```

### Running Demo (Local Mode - No Server)
```bash
mvn exec:java -Dexec.mainClass="Main.MarioPoorty" -Dexec.args="demo"
```

## Game Flow

### Server Initialization
1. Server starts and listens on specified port
2. Displays welcome message and port information
3. Waits for client connections

### Client Connection
1. Client connects to server with player name
2. Server creates PlayerConnection thread
3. Client receives acknowledgment
4. Board state is sent to new client
5. All other clients are notified

### Gameplay
1. Player selects "Roll Dice" from menu
2. Client generates random 1-6
3. Message sent to server
4. Server moves player on board
5. Server applies space effects (money, traps, etc.)
6. Updated board state sent to all clients
7. All players see current positions and money

### Space Effects
- **Normal Spaces**: No effect
- **Prize Spaces (every 5th)**: +$50
- **Trap Spaces (every 7th)**: -$100
- **Special Spaces (every 10th)**: +$200

## Client Menu Options
```
1. Roll Dice - Roll and move
2. Show Board - Display current game state
3. My Info - Display your player information
4. Disconnect - Leave the game
```

## Network Protocol

Messages are serialized Java objects containing:
- Message type (enum)
- Content (string)
- Player information (if applicable)
- Board state (if applicable)
- Timestamp

Example message flow:
```
Client                          Server
  |                              |
  |------ CONNECT (name) ------->|
  |                          [Add player]
  |<----- ACKNOWLEDGEMENT --------|
  |                      [Broadcast join]
  |<----- BOARD_UPDATE -----------|
  |                              |
  |------ DICE_ROLL (value) ----->|
  |                      [Move player]
  |<----- BOARD_UPDATE -----------|
```

## Example Usage Sequence

### Terminal 1 - Start Server
```bash
$ java -cp target/classes Main.MarioPoorty server
Servidor iniciado en puerto 5000
```

### Terminal 2 - Start Client 1
```bash
$ java -cp target/classes Main.MarioPoorty client localhost 5000 Mario
Conectado al servidor en localhost:5000
[SERVIDOR] Conectado al servidor
```

### Terminal 3 - Start Client 2
```bash
$ java -cp target/classes Main.MarioPoorty client localhost 5000 Luigi
Conectado al servidor en localhost:5000
[SERVIDOR] Conectado al servidor
[JUEGO] Mario se ha unido al juego
```

### In Terminal 2
```
--- MENÚ ---
1. Tirar dado
2. Ver tablero
3. Ver mi información
4. Desconectar
Selecciona una opción: 1
Tirada enviada: 4

=== ESTADO DEL TABLERO ===
Tablero: MarioPoorty Board
Jugadores conectados: 2

- Mario: $1050 (Casilla: Casilla_4)
- Luigi: $1000 (Casilla: Casilla_0)
===========================
```

## Features
- ✅ Multi-threaded server handling multiple simultaneous clients
- ✅ Serializable game objects for network transfer
- ✅ Real-time board state synchronization
- ✅ Dynamic space effects (money/penalties)
- ✅ Player join/leave notifications
- ✅ Graceful disconnection handling
- ✅ Interactive client menu
- ✅ Local demo mode for testing

## Testing
Run the demo to test the core game mechanics without networking:
```bash
java -cp target/classes Main.MarioPoorty demo
```

This will:
- Create a board with 40 spaces
- Add 3 players
- Simulate 3 turns with dice rolls
- Show movement and money changes

## Future Enhancements
- Turn management (enforce player order)
- Property purchasing system
- Trading between players
- Advanced UI with graphics
- Game persistence (save/load)
- Authentication system
- Rating and statistics tracking

## Troubleshooting

### Connection refused
- Ensure server is running
- Check port is correct
- Check firewall settings

### "Address already in use"
- Server is already running on that port
- Use different port: `server 5001`

### Serialization errors
- Ensure all game classes implement Serializable
- Check Java version compatibility

## License
Educational project - Feel free to modify and extend
