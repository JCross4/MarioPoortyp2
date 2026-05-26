package Main;

import java.io.Serializable;

/**
 * GameMessage represents a message sent between server and clients
 */
public class GameMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum MessageType {
        // Connection messages
        CONNECT,
        DISCONNECT,
        PLAYER_JOIN,
        PLAYER_LEFT,
        
        // Game state messages
        BOARD_UPDATE,
        PLAYER_UPDATE,
        PLAYER_MOVED,
        DICE_ROLL,
        
        // Game control
        GAME_START,
        GAME_END,
        TURN_CHANGE,
        
        // General
        ACTION,
        ERROR,
        ACKNOWLEDGEMENT
    }
    
    private MessageType tipo;
    private String contenido;
    private Jugador jugador;
    private Tablero tablero;
    private int playerId;
    private int diceValue;
    private long timestamp;
    
    public GameMessage() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public GameMessage(MessageType tipo, String contenido) {
        this();
        this.tipo = tipo;
        this.contenido = contenido;
    }
    
    public GameMessage(MessageType tipo, Jugador jugador) {
        this();
        this.tipo = tipo;
        this.jugador = jugador;
    }
    
    public GameMessage(MessageType tipo, Tablero tablero) {
        this();
        this.tipo = tipo;
        this.tablero = tablero;
    }
    
    // Getters and Setters
    public MessageType getTipo() {
        return tipo;
    }
    
    public void setTipo(MessageType tipo) {
        this.tipo = tipo;
    }
    
    public String getContenido() {
        return contenido;
    }
    
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    public Jugador getJugador() {
        return jugador;
    }
    
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }
    
    public Tablero getTablero() {
        return tablero;
    }
    
    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }
    
    public int getPlayerId() {
        return playerId;
    }
    
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
    
    public int getDiceValue() {
        return diceValue;
    }
    
    public void setDiceValue(int diceValue) {
        this.diceValue = diceValue;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return "GameMessage{" +
                "tipo=" + tipo +
                ", contenido='" + contenido + '\'' +
                ", playerId=" + playerId +
                ", timestamp=" + timestamp +
                '}';
    }
}
