package Main;

import java.io.*;
import java.net.Socket;

/**
 * PlayerConnection handles communication with a single connected player
 */
public class PlayerConnection extends Thread {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Jugador jugador;
    private GameServer server;
    private boolean conectado;
    
    public PlayerConnection(Socket socket, GameServer server) {
        this.socket = socket;
        this.server = server;
        this.conectado = true;
        
        try {
            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.output.flush();
            this.input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Error initializing streams: " + e.getMessage());
            conectado = false;
        }
    }
    
    @Override
    public void run() {
        try {
            while (conectado) {
                GameMessage mensaje = (GameMessage) input.readObject();
                procesarMensaje(mensaje);
            }
        } catch (EOFException e) {
            System.out.println("Player disconnected");
            desconectar();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error in player connection: " + e.getMessage());
            desconectar();
        }
    }
    
    /**
     * Process incoming messages from the client
     */
    private void procesarMensaje(GameMessage mensaje) {
        switch (mensaje.getTipo()) {
            case CONNECT:
                manejarConexion(mensaje);
                break;
            case DICE_ROLL:
                manejarTirada(mensaje);
                break;
            case PLAYER_MOVED:
                manejarMovimiento(mensaje);
                break;
            case DISCONNECT:
                desconectar();
                break;
            default:
                server.procesarMensajeGlobal(mensaje);
        }
    }
    
    /**
     * Handle player connection
     */
    private void manejarConexion(GameMessage mensaje) {
        this.jugador = mensaje.getJugador();
        System.out.println("Jugador conectado: " + jugador.getNombre());
        
        // Acknowledge connection
        GameMessage ack = new GameMessage(GameMessage.MessageType.ACKNOWLEDGEMENT, "Conectado al servidor");
        ack.setPlayerId(jugador.getId());
        enviarMensaje(ack);
        
        // Notify other players
        server.notificarJugadorConectado(jugador);
    }
    
    /**
     * Handle dice roll
     */
    private void manejarTirada(GameMessage mensaje) {
        int tirada = mensaje.getDiceValue();
        System.out.println(jugador.getNombre() + " tiró: " + tirada);
        
        // Broadcast to all players
        GameMessage broadcast = new GameMessage(GameMessage.MessageType.DICE_ROLL, 
            jugador.getNombre() + " tiró " + tirada);
        broadcast.setPlayerId(jugador.getId());
        broadcast.setDiceValue(tirada);
        
        server.transmitirATodos(broadcast);
    }
    
    /**
     * Handle player movement
     */
    private void manejarMovimiento(GameMessage mensaje) {
        if (jugador != null) {
            int pasos = mensaje.getDiceValue();
            server.moverJugador(jugador, pasos);
            
            // Send updated board state
            GameMessage update = new GameMessage(GameMessage.MessageType.BOARD_UPDATE, 
                server.getTablero());
            server.transmitirATodos(update);
        }
    }
    
    /**
     * Send a message to this player
     */
    public synchronized void enviarMensaje(GameMessage mensaje) {
        try {
            output.writeObject(mensaje);
            output.flush();
        } catch (IOException e) {
            System.err.println("Error sending message: " + e.getMessage());
            desconectar();
        }
    }
    
    /**
     * Disconnect this player
     */
    public void desconectar() {
        conectado = false;
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (jugador != null) {
                server.removerJugador(jugador);
            }
        } catch (IOException e) {
            System.err.println("Error disconnecting: " + e.getMessage());
        }
    }
    
    public Jugador getJugador() {
        return jugador;
    }
    
    public boolean estaConectado() {
        return conectado && socket != null && !socket.isClosed();
    }
}
