package Models.Juegos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Client.Cliente;

public class GuessWhoJuego implements Juego {
    private static final int SIZE = 10;
    private static final String[] CHARACTERS = {
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
        "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
        "AA", "BB", "CC", "DD", "EE", "FF", "GG", "HH", "II", "JJ",
        "KK", "LL", "MM", "NN", "OO", "PP", "QQ", "RR", "SS", "TT", "UU", "VV", "WW", "XX", "YY", "ZZ",
        "AAA", "BBB", "CCC", "DDD", "EEE", "FFF", "GGG", "HHH", "III", "JJJ", "KKK", "LLL", "MMM", "NNN",
        "OOO", "PPP", "QQQ", "RRR", "SSS", "TTT", "UUU", "VVV", "WWW", "XXX", "YYY", "ZZZ",
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
        "21", "22"
    };

    private Cliente cliente;
    private String targetCharacter;
    private int targetRow;
    private int targetCol;
    private String[][] board;
    private boolean[][] revealed;
    private int revealCount;

    public GuessWhoJuego(Cliente cliente) {
        this.cliente = cliente;
        this.board = new String[SIZE][SIZE];
        this.revealed = new boolean[SIZE][SIZE];
        this.revealCount = 4 + new Random().nextInt(5); // 4 a 8
        generarTablero();
    }

    @Override
    public void iniciar() {
        cliente.getPantalla().mostrarMensaje("Iniciando Guess Who. Se mostrarán " + revealCount + " casillas aleatorias.");
        revelarCasillas();
        mostrarTablero();
        int[] coordenadas = solicitarCoordenadas();
        if (coordenadas == null) {
            cliente.getPantalla().mostrarMensaje("Juego cancelado. Debes volver a jugar la misma casilla.");
            cliente.setUltimoJuegoGanado(false);
            return;
        }
        int row = coordenadas[0];
        int col = coordenadas[1];
        if (row == targetRow && col == targetCol) {
            cliente.getPantalla().mostrarMensaje("¡Correcto! El personaje escondido era " + targetCharacter + " en la casilla (" + (row + 1) + "," + (col + 1) + ").");
            cliente.getPantalla().mostrarMensaje("¡Ganaste Guess Who! Puedes lanzar los dados en tu siguiente turno.");
            cliente.setUltimoJuegoGanado(true);
        } else {
            cliente.getPantalla().mostrarMensaje("Incorrecto. El personaje estaba en la casilla (" + (targetRow + 1) + "," + (targetCol + 1) + ").");
            cliente.getPantalla().mostrarMensaje("Perdiste Guess Who. Debes volver a jugar esta casilla.");
            cliente.setUltimoJuegoGanado(false);
        }
    }

    private void generarTablero() {
        Random random = new Random();
        this.targetCharacter = CHARACTERS[random.nextInt(CHARACTERS.length)];
        this.targetRow = random.nextInt(SIZE);
        this.targetCol = random.nextInt(SIZE);
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (row == targetRow && col == targetCol) {
                    board[row][col] = targetCharacter;
                } else {
                    board[row][col] = CHARACTERS[random.nextInt(CHARACTERS.length)];
                }
            }
        }
    }

    private void revelarCasillas() {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < SIZE * SIZE; i++) {
            indices.add(i);
        }
        java.util.Collections.shuffle(indices);
        for (int i = 0; i < revealCount; i++) {
            int index = indices.get(i);
            int row = index / SIZE;
            int col = index % SIZE;
            revealed[row][col] = true;
        }
    }

    private void mostrarTablero() {
        StringBuilder sb = new StringBuilder();
        sb.append("Guess Who - Personaje escondido: ").append(targetCharacter).append("\n");
        sb.append("Matriz 10x10: ? = oculto, nombre = casilla revelada\n");
        sb.append("------------------------------------------------\n");
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (revealed[row][col]) {
                    String nombre = board[row][col];
                    sb.append(String.format("%-10s", nombre));
                } else {
                    sb.append(String.format("%-10s", "?"));
                }
            }
            sb.append("\n");
        }
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(cliente.getPantalla(), new JScrollPane(textArea), "Guess Who", JOptionPane.INFORMATION_MESSAGE);
    }

    private int[] solicitarCoordenadas() {
        while (true) {
            String input = JOptionPane.showInputDialog(cliente.getPantalla(), "Ingresa la fila y columna donde crees que está el personaje escondido (1-10)\nEjemplo: 4,7", "Guess Who", JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                return null;
            }
            input = input.trim();
            String[] partes = input.split("\\s*,\\s*");
            if (partes.length != 2) {
                cliente.getPantalla().mostrarMensaje("Formato inválido. Usa fila,col.");
                continue;
            }
            try {
                int fila = Integer.parseInt(partes[0]) - 1;
                int col = Integer.parseInt(partes[1]) - 1;
                if (fila < 0 || fila >= SIZE || col < 0 || col >= SIZE) {
                    cliente.getPantalla().mostrarMensaje("Coordenadas fuera de rango. Deben estar entre 1 y 10.");
                    continue;
                }
                return new int[] {fila, col};
            } catch (NumberFormatException ex) {
                cliente.getPantalla().mostrarMensaje("Valores inválidos. Deben ser números entre 1 y 10.");
            }
        }
    }

    @Override
    public void establecerGanador(boolean victoria) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'establecerGanador'");
    }
}
