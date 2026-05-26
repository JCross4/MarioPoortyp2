# Quick Start Guide - MarioPoorty

## 5-Minute Setup

### Step 1: Compile (One Time)
Open PowerShell/Command Prompt in project directory:
```powershell
cd "c:\Users\23jic\OneDrive\Documentos\POO2026\MarioPoortyp2"
cd src\main\java
javac Main/*.java -d "..\..\..\target\classes"
cd ..\..\..\..
```

### Step 2: Run Server
Open a new terminal and run:
```powershell
cd "c:\Users\23jic\OneDrive\Documentos\POO2026\MarioPoortyp2"
java -cp target\classes Main.MarioPoorty server
```
You should see: `Servidor iniciado en puerto 5000`

### Step 3: Run Client (Open Multiple Terminals)
For each player, open a new terminal:
```powershell
cd "c:\Users\23jic\OneDrive\Documentos\POO2026\MarioPoortyp2"
java -cp target\classes Main.MarioPoorty client localhost 5000 Mario
```

Replace "Mario" with player name: Luigi, Peach, Bowser, etc.

### Step 4: Play!
In each client terminal:
1. Press `1` then Enter to roll dice
2. Press `2` then Enter to see board state
3. Press `3` then Enter to see your info
4. Press `4` then Enter to quit

## Try the Demo First
To test without running server/client:
```powershell
java -cp target\classes Main.MarioPoorty demo
```

## Files Created

### Core Game Classes
- **Casilla.java** - Board spaces with types and effects
- **Jugador.java** - Player with money, position, and actions
- **Tablero.java** - Game board managing spaces and players

### Network Classes
- **GameMessage.java** - Message protocol for socket communication
- **GameServer.java** - Server managing game and connections
- **PlayerConnection.java** - Individual player connection handler
- **GameClient.java** - Client connecting to server

### Documentation
- **README.md** - Full documentation
- **QUICKSTART.md** - This guide

## System Architecture

```
┌─────────────────────────────────────────┐
│         Game Server (Port 5000)         │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │       Tablero (Game Board)      │   │
│  │ - 40 Casillas (Spaces)          │   │
│  │ - Connected Jugadores (Players) │   │
│  └─────────────────────────────────┘   │
│                                         │
│  ┌──────────────┐  ┌──────────────┐   │
│  │PlayerConn-1  │  │PlayerConn-2  │   │
│  │(Mario)       │  │(Luigi)       │   │
│  └──────────────┘  └──────────────┘   │
└─────────────────────────────────────────┘
         │                  │
    SocketMessage      SocketMessage
         │                  │
┌─────────────────┐ ┌─────────────────┐
│  Game Client 1  │ │  Game Client 2  │
│  (Mario)        │ │  (Luigi)        │
│  - Roll Dice    │ │  - Roll Dice    │
│  - View Board   │ │  - View Board   │
│  - Move Player  │ │  - Move Player  │
└─────────────────┘ └─────────────────┘
```

## Space Types and Effects

| Type | Example | Effect |
|------|---------|--------|
| Normal | Casilla_1, _2, _3 | No effect |
| Prize | Every 5th space | +$50 |
| Trap | Every 7th space | -$100 |
| Special | Every 10th space | +$200 |

## Troubleshooting

### "Port 5000 already in use"
```powershell
# Use different port
java -cp target\classes Main.MarioPoorty server 5001

# Then clients connect to port 5001
java -cp target\classes Main.MarioPoorty client localhost 5001 Mario
```

### "Connection refused"
- Check server is running
- Check correct host/port in client command
- Check firewall settings

### Files not found during compilation
Make sure you're in the correct directory: `MarioPoortyp2`

## Game Rules

1. **Start**: All players begin at Casilla_0 with $1000
2. **Turn**: Select "Roll Dice" to move 1-6 spaces forward
3. **Effects**: Landing on spaces gives/removes money:
   - Prize spaces: Gain $50
   - Trap spaces: Lose $100
   - Special spaces: Gain $200
4. **Goal**: Accumulate the most money
5. **End**: Game ends when a player runs out of money or quits

## Advanced Usage

### Custom Board Size
Edit Tablero.java constructor:
```java
new Tablero(60, "Large Board")  // 60 spaces instead of 40
```

### Custom Port
Start server on port 8080:
```powershell
java -cp target\classes Main.MarioPoorty server 8080
```

Connect client to port 8080:
```powershell
java -cp target\classes Main.MarioPoorty client localhost 8080 Mario
```

## Next Steps

1. ✅ Compile the code
2. ✅ Run the demo
3. ✅ Start server and multiple clients
4. ✅ Play a game!
5. 📚 Read README.md for full documentation
6. 🔧 Modify code to add features:
   - Turn management
   - Property buying
   - Player trading
   - Win conditions

## Contact
For questions or issues, refer to the README.md for detailed documentation.

---
**Happy Gaming!** 🎮
