package GUI;

import Client.Cliente;
import Models.Juegos.GatoJuego;
import Models.Messages.MessageGato;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GatoPantalla extends JDialog {
//Mover lógica a gatoJuego
    
    private final JButton[] buttons = new JButton[9];
    private final JLabel statusLabel = new JLabel();
    private GatoJuego gatoJuego;
    private boolean myTurn;
    private String oponente;
    private String mySymbol;
    private String oponenteSymbol;
    private Cliente player;

    public GatoPantalla(ClientFrame parent, Cliente cliente, boolean myTurn, String mySymbol, GatoJuego juego) {
        super(parent, "Gato / Tic Tac Toe contra " + juego.getOponente(cliente.getNombre()), false);
        this.gatoJuego = juego;
        this.oponente = juego.getOponente(cliente.getNombre());
        this.myTurn = myTurn;
        this.mySymbol = mySymbol;
        this.player = cliente;
        if (mySymbol.equals("X")){
            this.oponenteSymbol = "O";
        }
        else{
            this.oponenteSymbol = "X";
        }
        
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
        if (gatoJuego.getBoard()[position] != ' ') {
            return;
        }
        gatoJuego.getBoard()[position] = mySymbol.charAt(0);
        buttons[position].setText(mySymbol);
        buttons[position].setEnabled(false);
        sendMove(position);
        if (checkWinner(mySymbol)) {
            statusLabel.setText("¡Ganaste!");
            JOptionPane.showMessageDialog(this, "Ganaste el juego de Gato contra " + oponente + "!");
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
        String boardState = new String(gatoJuego.getBoard());
        MessageGato moveMessage = new MessageGato(
                "Gato",
                player.getNombre(),
                oponente,
                "Gato move",
                gatoJuego,
                "move",
                position,
                mySymbol,
                boardState,
                "inprogress"
        );
        player.escribirMensaje(moveMessage);
    }

    public void receiveOpponentMessage(MessageGato message) {
        if ("start".equals(message.getAction())) {
            return;
        }
        if ("move".equals(message.getAction())) {
            String boardState = message.getBoardState();
            for (int i = 0; i < 9 && i < boardState.length(); i++) {
                gatoJuego.getBoard()[i] = boardState.charAt(i);
                String text = gatoJuego.getBoard()[i] == ' ' ? " " : String.valueOf(gatoJuego.getBoard()[i]);
                buttons[i].setText(text);
                buttons[i].setEnabled(gatoJuego.getBoard()[i] == ' ');
            }
            if (checkWinner(oponenteSymbol)) {
                statusLabel.setText("Perdiste.");
                JOptionPane.showMessageDialog(this, "Perdiste el juego de Gato contra " + oponente + ".");
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
            if (gatoJuego.getBoard()[combo[0]] == mark && gatoJuego.getBoard()[combo[1]] == mark && gatoJuego.getBoard()[combo[2]] == mark) {
                return true;
            }
        }
        return false;
    }

    private boolean isBoardFull() {
        for (char c : gatoJuego.getBoard()) {
            if (c == ' ') {
                return false;
            }
        }
        return true;
    }

    private void updateStatusLabel() {
        String turnText = myTurn ? "Tu turno" : "Turno de " + oponente;
        statusLabel.setText("Juego Gato contra " + oponente + " - Tú eres " + mySymbol + ". " + turnText);
    }
}
