package Main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * GameServer manages the game, board, and all connected players
 */
public class GameServer {
    private int puerto;
    private ServerSocket serverSocket;
    private Tablero tablero;
    private List<PlayerConnection> conexiones;
    private boolean ejecutando;
    private int jugadorActual;
    
    public GameServer(int puerto) {
        this.puerto = puerto;
        this.conexiones = new ArrayList<>();
        this.tablero = new Tablero(40, "MarioPoorty Board");
        this.ejecutando = false;
        this.jugadorActual = 0;
    }
    
    /**
     * Start the game server
     */
    public void iniciar() {
        try {
            serverSocket = new ServerSocket(puerto);
            ejecutando = true;
            System.out.println("Servidor iniciado en puerto " + puerto);
            
            // Accept client connections
            while (ejecutando) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Nueva conexión desde: " + socket.getInetAddress());
                    
                    PlayerConnection conexion = new PlayerConnection(socket, this);
                    conexiones.add(conexion);
                    conexion.start();
                    
                } catch (IOException e) {
                    if (ejecutando) {
                        System.err.println("Error accepting connection: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        } finally {
            detener();
        }
    }
    
    /**
     * Stop the game server
     */
    public void detener() {
        ejecutando = false;
        try {
            // Disconnect all players
            for (PlayerConnection conexion : conexiones) {
                conexion.desconectar();
            }
            conexiones.clear();
            
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            System.out.println("Servidor detenido");
        } catch (IOException e) {
            System.err.println("Error stopping server: " + e.getMessage());
        }
    }
    
    /**
     * Add player to the board
     */
    public void agregarJugador(Jugador jugador) {
        tablero.agregarJugador(jugador);
        transmitirEstadoTablero();
    }
    
    /**
     * Remove player from the board
     */
    public void removerJugador(Jugador jugador) {
        tablero.removerJugador(jugador);
        
        // Remove connection
        conexiones.removeIf(c -> c.getJugador() != null && c.getJugador().getId() == jugador.getId());
        
        transmitirEstadoTablero();
    }
    
    /**
     * Move a player
     */
    public void moverJugador(Jugador jugador, int pasos) {
        if (tablero.getJugador(jugador.getId()) != null) {
            tablero.moverJugador(jugador, pasos);
        }
    }
    
    /**
     * Notify all players that a new player connected
     */
    public void notificarJugadorConectado(Jugador jugador) {
        GameMessage mensaje = new GameMessage(GameMessage.MessageType.PLAYER_JOIN,
            jugador.getNombre() + " se ha unido al juego");
        mensaje.setJugador(jugador);
        transmitirATodos(mensaje);
        
        agregarJugador(jugador);
    }
    
    /**
     * Send board state to all players
     */
    public void transmitirEstadoTablero() {
        GameMessage mensaje = new GameMessage(GameMessage.MessageType.BOARD_UPDATE, tablero);
        transmitirATodos(mensaje);
    }
    
    /**
     * Send a message to all connected players
     */
    public synchronized void transmitirATodos(GameMessage mensaje) {
        for (PlayerConnection conexion : conexiones) {
            if (conexion.estaConectado()) {
                conexion.enviarMensaje(mensaje);
            }
        }
    }
    
    /**
     * Process a global message (broadcast to all)
     */
    public void procesarMensajeGlobal(GameMessage mensaje) {
        transmitirATodos(mensaje);
    }
    
    /**
     * Get the game board
     */
    public Tablero getTablero() {
        return tablero;
    }
    
    /**
     * Get number of connected players
     */
    public int getNumJugadoresConectados() {
        return (int) conexiones.stream().filter(PlayerConnection::estaConectado).count();
    }
    
    /**
     * Get all connected players
     */
    public List<Jugador> getJugadoresConectados() {
        List<Jugador> jugadores = new ArrayList<>();
        for (PlayerConnection conexion : conexiones) {
            if (conexion.estaConectado() && conexion.getJugador() != null) {
                jugadores.add(conexion.getJugador());
            }
        }
        return jugadores;
    }
    
    /**
     * Main method to run the server
     */
    public static void main(String[] args) {
        int puerto = 5000;
        if (args.length > 0) {
            try {
                puerto = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Using default port 5000");
            }
        }
        
        GameServer servidor = new GameServer(puerto);
        servidor.iniciar();
    }
}
