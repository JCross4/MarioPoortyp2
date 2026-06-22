package GUI;

import Client.Cliente;
import Models.Juegos.SuperBrosMemoryJuego;
import Models.Messages.MessageMemory;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SuperBrosMemoryPantalla extends JDialog {
    private final Cliente cliente;
    private SuperBrosMemoryJuego juego;
    private final JButton[] buttons;
    private final JLabel starterScoreLabel = new JLabel("", JLabel.CENTER);
    private final JLabel opponentScoreLabel = new JLabel("", JLabel.CENTER);
    private final JLabel turnLabel = new JLabel("", JLabel.CENTER);
    private final JLabel statusLabel = new JLabel("", JLabel.CENTER);
    private int firstSelection = -1;

    public SuperBrosMemoryPantalla(ClientFrame parent, Cliente cliente, SuperBrosMemoryJuego juego) {
        super(parent, "Super Bros Memory vs " + juego.getOpponent(cliente.getNombre()), true);
        this.cliente = cliente;
        this.juego = juego;
        this.buttons = new JButton[juego.getRows() * juego.getColumns()];
        initComponents();
        setLocationRelativeTo(parent);
        setResizable(false);
        updateBoard();
        updateStatus();
        pack();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(8, 8));
        JPanel topPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        topPanel.add(new JLabel("Jugador inicio:", JLabel.CENTER));
        topPanel.add(new JLabel("Oponente:", JLabel.CENTER));
        topPanel.add(starterScoreLabel);
        topPanel.add(opponentScoreLabel);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(juego.getRows(), juego.getColumns(), 5, 5));
        for (int i = 0; i < buttons.length; i++) {
            JButton button = new JButton("?");
            button.setPreferredSize(new Dimension(60, 60));
            button.setBackground(Color.LIGHT_GRAY);
            button.setOpaque(true);
            final int index = i;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleCardClick(index);
                }
            });
            buttons[i] = button;
            gridPanel.add(button);
        }

        mainPanel.add(gridPanel, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 4, 4));
        bottomPanel.add(turnLabel);
        bottomPanel.add(statusLabel);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void handleCardClick(int index) {
        if (juego.isGameOver()) {
            return;
        }
        if (!juego.getCurrentPlayer().equals(cliente.getNombre())) {
            JOptionPane.showMessageDialog(this, "No es tu turno.");
            return;
        }
        if (juego.isMatched(index)) {
            return;
        }
        if (firstSelection == index) {
            return;
        }

        revealCard(index);

        if (firstSelection < 0) {
            firstSelection = index;
            statusLabel.setText("Selecciona otra carta para formar un par.");
            return;
        }

        int secondSelection = index;
        boolean isMatch = juego.getCardValue(firstSelection) == juego.getCardValue(secondSelection);
        String actionStatus;
        if (isMatch) {
            juego.setMatched(firstSelection, true);
            juego.setMatched(secondSelection, true);
            juego.addMatchForCurrentPlayer();
            actionStatus = "Par encontrado por " + juego.getCurrentPlayer() + "!";
            if (juego.isGameOver()) {
                String winner = juego.getWinnerName();
                if (winner.equals("Empate")) {
                    actionStatus = "El juego terminó en empate.";
                } else if (winner.equals(cliente.getNombre())) {
                    actionStatus = "¡Ganaste Super Bros Memory!";
                } else {
                    actionStatus = "Perdiste Super Bros Memory.";
                }
            }
            firstSelection = -1;
            updateBoard();
            updateStatus();
            sendGameUpdate(actionStatus, juego.isGameOver());
            if (juego.isGameOver()) {
                showFinalResult();
            }
            return;
        }

        actionStatus = "No hubo par. Turno de " + (juego.getOpponent(juego.getCurrentPlayer()));
        updateStatus();
        revealCard(secondSelection);
        int first = firstSelection;
        int second = secondSelection;
        firstSelection = -1;
        final String statusToSend = actionStatus;

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        hideCard(first);
        hideCard(second);
        juego.switchTurn();
        updateBoard();
        updateStatus();
        sendGameUpdate(statusToSend, juego.isGameOver());
    }

    private void revealCard(int index) {
        buttons[index].setText(juego.getLabelForCard(index));
        buttons[index].setBackground(Color.WHITE);
    }

    private void hideCard(int index) {
        if (!juego.isMatched(index)) {
            buttons[index].setText("?");
            buttons[index].setBackground(Color.LIGHT_GRAY);
        }
    }

    private void updateBoard() {
        for (int i = 0; i < buttons.length; i++) {
            JButton button = buttons[i];
            if (juego.isMatched(i)) {
                button.setText(juego.getLabelForCard(i));
                button.setBackground(Color.ORANGE);
                button.setEnabled(false);
            } else {
                button.setText("?");
                button.setBackground(Color.LIGHT_GRAY);
                button.setEnabled(!juego.isGameOver());
            }
        }
        starterScoreLabel.setText(juego.getStarter() + ": " + juego.getScoreStarter());
        opponentScoreLabel.setText(juego.getOpponent(juego.getStarter()) + ": " + juego.getScoreOpponent());
    }

    private void updateStatus() {
        turnLabel.setText("Turno actual: " + juego.getCurrentPlayer());
        if (juego.isGameOver()) {
            statusLabel.setText("Juego terminado.");
        }
    }

    private void showFinalResult() {
        String winner = juego.getWinnerName();
        if (winner.equals("Empate")) {
            JOptionPane.showMessageDialog(this, "El juego terminó en empate.");
        } else if (winner.equals(cliente.getNombre())) {
            JOptionPane.showMessageDialog(this, "¡Ganaste Super Bros Memory!");
        } else {
            JOptionPane.showMessageDialog(this, "Perdiste Super Bros Memory.");
        }
        dispose();
    }

    private void sendGameUpdate(String status, boolean gameOver) {
        MessageMemory message = new MessageMemory(
                "Memory",
                cliente.getNombre(),
                juego.getOpponent(cliente.getNombre()),
                "Actualización de Super Bros Memory",
                juego,
                "update",
                -1,
                status,
                gameOver
        );
        cliente.escribirMensaje(message);
    }

    public void receiveOpponentMessage(MessageMemory message) {
        if (message == null) {
            return;
        }
        if ("start".equals(message.getAction())) {
            this.juego = message.getJuego();
            updateBoard();
            updateStatus();
            this.setTitle("Super Bros Memory vs " + juego.getStarter());
            this.setVisible(true);
            return;
        }
        if ("update".equals(message.getAction())) {
            this.juego = message.getJuego();
            firstSelection = -1;
            updateBoard();
            updateStatus();
            if (message.isGameOver()) {
                showFinalResult();
            }
        }
    }
}
