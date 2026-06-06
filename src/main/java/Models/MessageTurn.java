package Models;

import java.io.Serializable;

public class MessageTurn extends Message implements Serializable {
    private String playerActual;
    private int turnNumber;
    
    public MessageTurn(String tipo, String emisor, String receptor, String mensaje, String playerActual, int turnNumber) {
        super(tipo, emisor, receptor, mensaje);
        this.playerActual = playerActual;
        this.turnNumber = turnNumber;
    }

    public String getPlayerActual() {
        return playerActual;
    }

    public void setPlayerActual(String PlayerActual) {
        this.playerActual = PlayerActual;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }
}
