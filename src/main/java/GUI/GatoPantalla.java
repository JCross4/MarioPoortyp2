package GUI;

import Client.Cliente;
import Models.GatoJuego;
import Models.MessageGato;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class GatoPantalla extends JDialog {

    private final Cliente cliente;
    private final String opponentName;
    private final String mySymbol;
    private final String opponentSymbol;
    private boolean myTurn;
    private final char[] board = new char[9];
    private final JButton[] buttons = new JButton[9];
    private final JLabel statusLabel = new JLabel();
    private GatoJuego gatoJuego;

    public GatoPantalla(ClientFrame parent, Cliente cliente, String opponentName, boolean myTurn, String mySymbol, GatoJuego juego) {
        super(parent, "Gato / Tic Tac Toe contra " + opponentName, false);
        this.cliente = cliente;
        this.gatoJuego = juego;
        this.opponentName = opponentName;
        this.mySymbol = mySymbol;
        if (mySymbol.equals("X")){
            this.opponentSymbol = "O";
        }
        else{
            this.opponentSymbol = "X";
        }
        this.myTurn = myTurn;
        Arrays.fill(board, ' ');
        initComponents();
        setLocationRelativeTo(parent);
        setResizable(false);
        updateStatusLabel();
        pack();
    }

    private void initComponents() {
        JPanel gridPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        for (int i = 0; i < 9; i++) {
            JButton button = new JButton(" ");
            final int position = i;
            button.setFont(button.getFont().deriveFont(24f));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleCellClick(position);
                }
            });
            buttons[i] = button;
            gridPanel.add(button);
        }
        add(gridPanel, BorderLayout.CENTER);
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void handleCellClick(int position) {
        if (!myTurn) {
            JOptionPane.showMessageDialog(this, "No es tu turno.");
            return;
        }
        if (board[position] != ' ') {
            return;
        }
        board[position] = mySymbol.charAt(0);
        buttons[position].setText(mySymbol);
        buttons[position].setEnabled(false);
        sendMove(position);
        if (checkWinner(mySymbol)) {
            statusLabel.setText("¡Ganaste!");
            JOptionPane.showMessageDialog(this, "Ganaste el juego de Gato contra " + opponentName + "!");
            dispose();
            return;
        }
        if (isBoardFull()) {
            statusLabel.setText("Empate.");
            JOptionPane.showMessageDialog(this, "El juego de Gato terminó en empate.");
            dispose();
            return;
        }
        myTurn = false;
        updateStatusLabel();
    }

    private void sendMove(int position) {
        String boardState = new String(board);
        MessageGato moveMessage = new MessageGato(
                "Gato",
                cliente.getNombre(),
                opponentName,
                "Gato move",
                gatoJuego,
                "move",
                position,
                mySymbol,
                boardState,
                "inprogress"
        );
        cliente.escribirMensaje(moveMessage);
    }

    public void receiveOpponentMessage(MessageGato message) {
        if ("start".equals(message.getAction())) {
            return;
        }
        if ("move".equals(message.getAction())) {
            String boardState = message.getBoardState();
            for (int i = 0; i < 9 && i < boardState.length(); i++) {
                board[i] = boardState.charAt(i);
                String text = board[i] == ' ' ? " " : String.valueOf(board[i]);
                buttons[i].setText(text);
                buttons[i].setEnabled(board[i] == ' ');
            }
            if (checkWinner(opponentSymbol)) {
                statusLabel.setText("Perdiste.");
                JOptionPane.showMessageDialog(this, "Perdiste el juego de Gato contra " + opponentName + ".");
                dispose();
                return;
            }
            if (isBoardFull()) {
                statusLabel.setText("Empate.");
                JOptionPane.showMessageDialog(this, "El juego de Gato terminó en empate.");
                dispose();
                return;
            }
            myTurn = true;
            updateStatusLabel();
        }
    }

    private boolean checkWinner(String symbol) {
        char mark = symbol.charAt(0);
        int[][] wins = {
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8},
                {0, 3, 6},
                {1, 4, 7},
                {2, 5, 8},
                {0, 4, 8},
                {2, 4, 6}
        };
        for (int[] combo : wins) {
            if (board[combo[0]] == mark && board[combo[1]] == mark && board[combo[2]] == mark) {
                return true;
            }
        }
        return false;
    }

    private boolean isBoardFull() {
        for (char c : board) {
            if (c == ' ') {
                return false;
            }
        }
        return true;
    }

    private void updateStatusLabel() {
        String turnText = myTurn ? "Tu turno" : "Turno de " + opponentName;
        statusLabel.setText("Juego Gato contra " + opponentName + " - Tú eres " + mySymbol + ". " + turnText);
    }
}
