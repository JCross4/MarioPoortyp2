# MarioPoorty System Architecture

## System Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                  MARIO POORTY GAME SYSTEM                       │
└─────────────────────────────────────────────────────────────────┘
                              │
                    ┌─────────┴─────────┐
                    │                   │
              ┌─────▼─────┐      ┌─────▼─────┐
              │   Server  │      │  Clients  │
              │  (Single) │      │ (Multiple)│
              └─────┬─────┘      └─────┬─────┘
                    │                   │
                    │    Socket Port    │
                    │      5000         │
                    └───────────────────┘
```

## Server Architecture

```
┌────────────────────────────────────────────────────────────┐
│                    GAME SERVER                             │
│                  (GameServer.java)                         │
├────────────────────────────────────────────────────────────┤
│                                                            │
│  ┌──────────────────────────────────────────────────────┐ │
│  │            Tablero (Game Board)                     │ │
│  │  ┌────────────────────────────────────────────────┐ │ │
│  │  │  ArrayList<Casilla> (40 Spaces)               │ │ │
│  │  │  ┌─────┬─────┬─────┬─────┬─────┐              │ │ │
│  │  │  │ C_0 │ C_1 │ C_2 │ C_3 │...40│ (Circular)  │ │ │
│  │  │  └─────┴─────┴─────┴─────┴─────┘              │ │ │
│  │  │                                                │ │ │
│  │  │  ArrayList<Jugador> (Connected Players)       │ │ │
│  │  │  ┌──────┬──────┬──────┬──────┐                │ │ │
│  │  │  │Mario │Luigi │Peach │Bowser│  (Max 4)     │ │ │
│  │  │  └──────┴──────┴──────┴──────┘                │ │ │
│  │  └────────────────────────────────────────────────┘ │ │
│  └──────────────────────────────────────────────────────┘ │
│                                                            │
│  ┌──────────────────────────────────────────────────────┐ │
│  │      PlayerConnection Threads (One per Client)       │ │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────┐  │ │
│  │  │ Player Conn  │  │ Player Conn  │  │Player... │  │ │
│  │  │  (Thread 1)  │  │  (Thread 2)  │  │(Thread N)│  │ │
│  │  └──────────────┘  └──────────────┘  └──────────┘  │ │
│  └──────────────────────────────────────────────────────┘ │
│                                                            │
│  Key Methods:                                             │
│  - agregarJugador()      → Add player to board           │
│  - moverJugador()        → Move and apply effects        │
│  - transmitirATodos()    → Broadcast to all clients      │
│  - procesarMensaje()     → Handle incoming messages      │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

## Client Architecture

```
┌────────────────────────────────────────────────────────────┐
│                   GAME CLIENT                              │
│                (GameClient.java)                           │
├────────────────────────────────────────────────────────────┤
│                                                            │
│  ┌──────────────────────────────────────────────────────┐ │
│  │         User Interface (Command-Line Menu)          │ │
│  │  ┌────────────────────────────────────────────────┐ │ │
│  │  │ 1. Roll Dice                                   │ │ │
│  │  │ 2. View Board                                  │ │ │
│  │  │ 3. View My Information                         │ │ │
│  │  │ 4. Disconnect                                  │ │ │
│  │  └────────────────────────────────────────────────┘ │ │
│  └──────────────────────────────────────────────────────┘ │
│                                                            │
│  ┌──────────────────────────────────────────────────────┐ │
│  │         Local Game State (Cache)                    │ │
│  │  - miJugador (Jugador)      → Player object         │ │
│  │  - tableroLocal (Tablero)   → Board state           │ │
│  └──────────────────────────────────────────────────────┘ │
│                                                            │
│  ┌──────────────────────────────────────────────────────┐ │
│  │      Network Communication                           │ │
│  │  ┌────────────────┐        ┌─────────────────────┐  │ │
│  │  │ ObjectOutput   │─send→  │   Server Socket     │  │ │
│  │  │  (Send)        │        │   Port 5000         │  │ │
│  │  └────────────────┘        └─────────────────────┘  │ │
│  │                                                      │ │
│  │  ┌────────────────┐        ┌─────────────────────┐  │ │
│  │  │ ObjectInput    │←receive│   Server Socket     │  │ │
│  │  │  (Receive)     │        │   Port 5000         │  │ │
│  │  └────────────────┘        └─────────────────────┘  │ │
│  │                                                      │ │
│  │  ┌────────────────────────────────────────────────┐  │ │
│  │  │ RecibidorMensajes (Message Receiver Thread)   │  │ │
│  │  │ - Runs in background                          │  │ │
│  │  │ - Receives and processes server messages      │  │ │
│  │  │ - Updates UI when board changes               │  │ │
│  │  └────────────────────────────────────────────────┘  │ │
│  └──────────────────────────────────────────────────────┘ │
│                                                            │
│  Key Methods:                                             │
│  - conectar()          → Connect to server               │
│  - desconectar()       → Disconnect gracefully           │
│  - tirarDado()         → Roll dice and move              │
│  - mostrarTablero()    → Display current board state     │
│  - procesarMensaje()   → Handle server messages          │
│  - mostrarMenu()       → Display interactive menu        │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

## Data Flow Diagram

```
┌──────────────────────────────────────────────────────────────────┐
│                      MESSAGE FLOW SEQUENCE                       │
└──────────────────────────────────────────────────────────────────┘

Initial Connection:
────────────────────

  Client 1              GameMessage              Server
     │                     │                       │
     │  CONNECT            │                       │
     ├──────────────────────────────────────────→  │
     │                     │                  (create thread)
     │                     │                  (add to board)
     │                     │                       │
     │  ACKNOWLEDGEMENT    │                       │
     │  ← ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ │
     │                                            │
     │                PLAYER_JOIN broadcast       │
     │                (to all clients)            │
     │  ← ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ │
     │                BOARD_UPDATE broadcast      │
     │  ← ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ │


During Gameplay:
────────────────

  Client 1              GameMessage              Server
     │                     │                       │
     │  DICE_ROLL (4)      │                       │
     ├──────────────────────────────────────────→  │
     │                     │                  (move player)
     │                     │                  (apply effects)
     │                     │                       │
     │  BOARD_UPDATE       │                       │
     │  ← ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ │
     │  (to all clients)   │                       │


Disconnection:
──────────────

  Client 1              GameMessage              Server
     │                     │                       │
     │  DISCONNECT         │                       │
     ├──────────────────────────────────────────→  │
     │                     │                  (remove from board)
     │                     │                       │
     │  PLAYER_LEFT        │                       │
     │  (broadcast)        │                       │
     │  ← ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ │
     │                                            │
```

## Class Relationships

```
┌─────────────────────────────────────────────────────────────┐
│ CORE GAME OBJECTS (Serializable for network transfer)      │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌──────────────┐           ┌──────────────┐              │
│  │   Casilla    │           │   Jugador    │              │
│  │  (Space)     │           │   (Player)   │              │
│  ├──────────────┤           ├──────────────┤              │
│  │ - id: int    │           │ - id: int    │              │
│  │ - nombre     │           │ - nombre     │              │
│  │ - tipo       │◄──┐       │ - dinero     │              │
│  │ - valor      │   │       │ - posicion   │              │
│  │ - jugador    │   │       │ - casilla    │              │
│  │              │   │       │ - activo     │              │
│  └──────────────┘   │       │              │              │
│                     │       └──────────────┘              │
│                     │                                     │
│                     │ (Reference)                        │
│                     │                                     │
│  ┌──────────────────┴─────────────────┐                │
│  │         Tablero (Board)            │                │
│  ├────────────────────────────────────┤                │
│  │ - casillas: ArrayList<Casilla>     │                │
│  │ - jugadores: ArrayList<Jugador>    │                │
│  │ - tamano: int                      │                │
│  │ - nombre: String                   │                │
│  │                                    │                │
│  │ Methods:                           │                │
│  │ + agregarJugador()                 │                │
│  │ + moverJugador()                   │                │
│  │ + aplicarEfectoCasilla()           │                │
│  └────────────────────────────────────┘                │
│                                                         │
└─────────────────────────────────────────────────────────┘


┌─────────────────────────────────────────────────────────────┐
│ NETWORK OBJECTS                                             │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌────────────────────┐      ┌──────────────────┐          │
│  │  GameMessage       │      │   MessageType    │          │
│  │  (Protocol)        │      │   (Enum)         │          │
│  ├────────────────────┤      ├──────────────────┤          │
│  │ - tipo             │──→   │ - CONNECT        │          │
│  │ - contenido        │      │ - DISCONNECT     │          │
│  │ - jugador          │      │ - DICE_ROLL      │          │
│  │ - tablero          │      │ - PLAYER_MOVED   │          │
│  │ - playerId         │      │ - BOARD_UPDATE   │          │
│  │ - diceValue        │      │ - PLAYER_JOIN    │          │
│  │ - timestamp        │      │ - PLAYER_LEFT    │          │
│  └────────────────────┘      │ - GAME_START     │          │
│                              │ - GAME_END       │          │
│                              │ - ACKNOWLEDGEMENT│          │
│                              │ - ERROR          │          │
│                              └──────────────────┘          │
│                                                             │
└─────────────────────────────────────────────────────────────┘


┌─────────────────────────────────────────────────────────────┐
│ SERVER & CLIENT CLASSES                                     │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌──────────────────────┐    ┌──────────────────────┐      │
│  │   GameServer         │    │   GameClient         │      │
│  │   (Main Server)      │    │   (Main Client)      │      │
│  ├──────────────────────┤    ├──────────────────────┤      │
│  │ - puerto             │    │ - host               │      │
│  │ - serverSocket       │    │ - puerto             │      │
│  │ - tablero: Tablero   │    │ - socket             │      │
│  │ - conexiones: List   │    │ - miJugador: Jugador │      │
│  │                      │    │ - tableroLocal       │      │
│  │ Methods:             │    │ - recibidor (Thread) │      │
│  │ + iniciar()          │    │                      │      │
│  │ + detener()          │    │ Methods:             │      │
│  │ + agregarJugador()   │    │ + conectar()         │      │
│  │ + moverJugador()     │    │ + desconectar()      │      │
│  │ + transmitirATodos() │    │ + tirarDado()        │      │
│  └──────────┬───────────┘    │ + mostrarTablero()   │      │
│             │                 │ + mostrarMenu()      │      │
│             │ (Contains)       └──────────────────────┘      │
│             │                                               │
│  ┌──────────▼─────────────────┐                            │
│  │ PlayerConnection (Thread)  │                            │
│  │ (Server-side client handler)                            │
│  ├────────────────────────────┤                            │
│  │ - socket: Socket           │                            │
│  │ - jugador: Jugador         │                            │
│  │ - input/output streams     │                            │
│  │                            │                            │
│  │ Methods:                   │                            │
│  │ + run()                    │                            │
│  │ + procesarMensaje()        │                            │
│  │ + enviarMensaje()          │                            │
│  │ + desconectar()            │                            │
│  └────────────────────────────┘                            │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

## Game Flow Diagram

```
┌──────────────────────────────────────────────────────────────┐
│                    GAME LIFECYCLE                            │
└──────────────────────────────────────────────────────────────┘

1. STARTUP PHASE
   ┌─────────────────────┐
   │ Server Starts       │
   │ - Listens on :5000  │
   │ - Creates Board     │
   │ - Waits for clients │
   └──────────┬──────────┘
              │
              ├─ Client 1 connects
              │   └─ Added to Board
              │
              ├─ Client 2 connects
              │   └─ Added to Board
              │
              └─ Client 3 connects
                  └─ Added to Board

2. GAMEPLAY PHASE (Repetitive Loop)
   ┌──────────────────────────────────┐
   │ Player's Turn                    │
   ├──────────────────────────────────┤
   │ 1. Player selects "Roll Dice"    │
   │ 2. Client sends DICE_ROLL msg    │
   │ 3. Server processes:             │
   │    - Moves player                │
   │    - Applies space effects       │
   │    - Updates board state         │
   │ 4. Server broadcasts board to    │
   │    all connected clients         │
   │ 5. All clients display update    │
   │ 6. Loop continues with next turn │
   └──────────────────────────────────┘

3. SHUTDOWN PHASE
   ┌─────────────────────┐
   │ Player Disconnects  │
   ├─────────────────────┤
   │ Client closes conn  │
   │ Server removes      │
   │ player from board   │
   │ Board update sent   │
   │ to other players    │
   └─────────────────────┘

   When all players gone:
   └─ Server waits for new connections
```

## Space Types Distribution

```
Board Layout (40 Spaces - Circular):

    0 ← Special Space (+$200)
    1 ← Normal
    2 ← Normal
    3 ← Normal
    4 ← Normal
    5 ← Prize Space (+$50)
    6 ← Normal
    7 ← Trap Space (-$100)
    8 ← Normal
    9 ← Normal
   10 ← Special Space (+$200)
   ...continues pattern...
   40 → Wraps to 0 (Circular)

Pattern Summary:
- Every 10th (10, 20, 30): SPECIAL (+$200)
- Every 5th (5, 15, 25, 35): PRIZE (+$50)
- Every 7th (7, 14, 21, 28, 35): TRAP (-$100)
- All others: NORMAL (no effect)
```

## Threading Model

```
┌────────────────────────────────────────────────────────────┐
│                   THREADING MODEL                          │
├────────────────────────────────────────────────────────────┤
│                                                            │
│  SERVER SIDE:                                             │
│  ┌─────────────────────────────────────────────────────┐ │
│  │ Main Thread                                         │ │
│  │ - Listens for connections                          │ │
│  │ - Accepts sockets                                  │ │
│  │ - Creates PlayerConnection threads                 │ │
│  └─────────────────────────────────────────────────────┘ │
│                         │                                 │
│         ┌───────────────┼───────────────┐                │
│         │               │               │                │
│    ┌────▼──────┐   ┌────▼──────┐   ┌────▼──────┐        │
│    │ PlayerConn│   │ PlayerConn│   │ PlayerConn│        │
│    │ Thread 1  │   │ Thread 2  │   │ Thread N  │        │
│    │ (Mario)   │   │ (Luigi)   │   │ (Peach)   │        │
│    │           │   │           │   │           │        │
│    │ Receives  │   │ Receives  │   │ Receives  │        │
│    │ messages  │   │ messages  │   │ messages  │        │
│    │ from      │   │ from      │   │ from      │        │
│    │ clients   │   │ clients   │   │ clients   │        │
│    └─────────────────────────────────────────────        │
│                      (All access shared Tablero)         │
│                                                            │
│  CLIENT SIDE:                                             │
│  ┌─────────────────────────────────────────────────────┐ │
│  │ Main Thread                                         │ │
│  │ - Shows interactive menu                           │ │
│  │ - Processes user input                             │ │
│  │ - Sends commands to server                         │ │
│  └─────────────────────────────────────────────────────┘ │
│                         │                                 │
│                    ┌────┴────┐                           │
│                    │          │                          │
│              ┌─────▼──┐   ┌───▼─────┐                   │
│              │Receiver│   │  Menu   │                   │
│              │ Thread │   │ Thread  │                   │
│              │        │   │         │                   │
│              │Receives│   │Displays │                   │
│              │updates │   │UI and   │                   │
│              │from    │   │accepts  │                   │
│              │server  │   │input    │                   │
│              └────────┘   └─────────┘                   │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

## Serialization Details

```
All Network Objects Implement Serializable:

Casilla          ──┐
  ├ id            │
  ├ nombre        │
  ├ tipo          │
  ├ valor         │  → Serialized
  └ jugador       │   to bytes
                  │   over socket
Jugador          ──┤
  ├ id            │
  ├ nombre        │
  ├ dinero        │
  ├ casilla       │
  ├ posicion      │
  └ activo        │
                  │
Tablero          ──┤
  ├ casillas[]    │
  ├ jugadores[]   │
  ├ tamano        │
  └ nombre        │
                  │
GameMessage      ──┘
  ├ tipo
  ├ contenido
  ├ jugador
  ├ tablero
  ├ playerId
  ├ diceValue
  └ timestamp
```

This architecture ensures:
- ✅ Thread-safe game state management
- ✅ Real-time synchronization across all clients
- ✅ Scalable connection handling
- ✅ Clean separation of concerns
- ✅ Extensible message protocol
