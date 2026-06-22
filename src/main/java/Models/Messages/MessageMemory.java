package Models.Messages;

import Models.Juegos.SuperBrosMemoryJuego;

public class MessageMemory extends Message {
    private String action;
    private SuperBrosMemoryJuego juego;
    private int selectedIndex;
    private String gameStatus;
    private boolean gameOver;

    public MessageMemory(String tipo, String emisor, String receptor, String mensaje,
            SuperBrosMemoryJuego juego, String action, int selectedIndex, String gameStatus, boolean gameOver) {
        super(tipo, emisor, receptor, mensaje);
        this.juego = juego;
        this.action = action;
        this.selectedIndex = selectedIndex;
        this.gameStatus = gameStatus;
        this.gameOver = gameOver;
    }

    public String getAction() {
        return action;
    }

    public SuperBrosMemoryJuego getJuego() {
        return juego;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
