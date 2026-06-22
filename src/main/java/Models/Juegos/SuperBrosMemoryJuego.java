package Models.Juegos;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

import Client.Cliente;

public class SuperBrosMemoryJuego implements Juego, Serializable {
    private static final int ROWS = 3;
    private static final int COLUMNS = 6;
    private static final int TOTAL_CARDS = ROWS * COLUMNS;
    private static final int PAIR_COUNT = 9;

    private String starter;
    private String opponent;
    private String currentPlayer;
    private int[] cardValues;
    private boolean[] matched;
    private int scoreStarter;
    private int scoreOpponent;
    private int pairsFound;
    private int firstSelection;


    public SuperBrosMemoryJuego(String starter, String opponent) {
        this.starter = starter;
        this.opponent = opponent;
        this.currentPlayer = starter;
        this.cardValues = new int[TOTAL_CARDS];
        this.matched = new boolean[TOTAL_CARDS];
        this.scoreStarter = 0;
        this.scoreOpponent = 0;
        this.pairsFound = 0;
        this.firstSelection = -1;
        generarCartas();
    }

    @Override
    public void iniciar() {
        // La vista se crea desde Cliente, no desde el juego directamente.
    }

    private void generarCartas() {
        int[] values = new int[TOTAL_CARDS];
        for (int i = 0; i < PAIR_COUNT; i++) {
            values[2 * i] = i;
            values[2 * i + 1] = i;
        }
        Random random = new Random();
        for (int i = 0; i < TOTAL_CARDS; i++) {
            int j = random.nextInt(TOTAL_CARDS);
            int temp = values[i];
            values[i] = values[j];
            values[j] = temp;
        }
        this.cardValues = values;
    }

    public String getStarter() {
        return starter;
    }

    public String getOpponent(String nombre) {
        if (nombre.equals(starter)) {
            return opponent;
        } else {
            return starter;
        }
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getScoreStarter() {
        return scoreStarter;
    }

    public int getScoreOpponent() {
        return scoreOpponent;
    }

    public int getPairsFound() {
        return pairsFound;
    }

    public int getCardValue(int index) {
        return cardValues[index];
    }

    public boolean isMatched(int index) {
        return matched[index];
    }

    public void setMatched(int index, boolean value) {
        matched[index] = value;
    }

    public int getFirstSelection() {
        return firstSelection;
    }

    public void setFirstSelection(int firstSelection) {
        this.firstSelection = firstSelection;
    }

    public void resetFirstSelection() {
        this.firstSelection = -1;
    }

    public void addMatchForCurrentPlayer() {
        if (currentPlayer.equals(starter)) {
            scoreStarter++;
        } else {
            scoreOpponent++;
        }
        pairsFound++;
    }

    public void switchTurn() {
        currentPlayer = getOpponent(currentPlayer);
        firstSelection = -1;
    }

    public boolean isGameOver() {
        return pairsFound >= PAIR_COUNT;
    }

    public String getWinnerName() {
        if (scoreStarter > scoreOpponent) {
            return starter;
        } else if (scoreOpponent > scoreStarter) {
            return opponent;
        } else {
            return "Empate";
        }
    }

    public boolean isStarterWinner() {
        return scoreStarter > scoreOpponent;
    }

    public String getLabelForCard(int index) {
        int value = cardValues[index];
        return String.valueOf((char) ('A' + value));
    }


    public int getRows() {
        return ROWS;
    }

    public int getColumns() {
        return COLUMNS;
    }

    @Override
    public void establecerGanador(boolean victoria) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'establecerGanador'");
    }
}
