package GUI;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import Models.Juegos.BomberMarioJuego;
import Models.Juegos.BomberMarioJuego.BombType;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BomberMarioPantalla extends JDialog {
    private final BomberMarioJuego juego;
    private final JButton[][] buttons;
    private final JLabel statusLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel bombsLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel foundLabel = new JLabel("", SwingConstants.CENTER);
    private final JComboBox<String> bombTypeCombo;

    public BomberMarioPantalla(JFrame parent, BomberMarioJuego juego) {
        super(parent, "Bomber Mario", true);
        this.juego = juego;
        this.buttons = new JButton[juego.getSize()][juego.getSize()];
        this.bombTypeCombo = new JComboBox<>(juego.getBombTypeNames());

        initComponents();
        setLocationRelativeTo(parent);
        setResizable(false);
        updateStatus();
        pack();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(8, 8));
        JPanel topPanel = new JPanel(new GridLayout(1, 3, 8, 8));
        topPanel.add(bombsLabel);
        topPanel.add(foundLabel);
        topPanel.add(bombTypeCombo);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(juego.getSize(), juego.getSize(), 2, 2));
        for (int r = 0; r < juego.getSize(); r++) {
            for (int c = 0; c < juego.getSize(); c++) {
                JButton button = new JButton(" ");
                button.setPreferredSize(new Dimension(35, 35));
                button.setBackground(Color.LIGHT_GRAY);
                button.setOpaque(true);
                final int fila = r;
                final int col = c;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleCellClick(fila, col);
                    }
                });
                buttons[r][c] = button;
                gridPanel.add(button);
            }
        }

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setPreferredSize(new Dimension(Math.min(700, juego.getSize() * 40), Math.min(700, juego.getSize() * 40)));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        statusLabel.setPreferredSize(new Dimension(300, 30));
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void handleCellClick(int fila, int col) {
        if (juego.isGameOver()) {
            juego.getCliente().setUltimoJuegoGanado(false);
            return;
        }

        BombType tipo = juego.getBombTypeByName((String) bombTypeCombo.getSelectedItem());
        List<int[]> impacto = juego.obtenerImpacto(tipo, fila, col);
        int hits = juego.procesarImpacto(impacto);
        applyImpact(impacto);
        juego.decrementBomb();
        updateStatus();

        String result = "Bomba " + tipo.getNombre() + " explotó en " + impacto.size() + " casillas.";
        if (hits > 0) {
            result += " Tesoro encontrado en " + hits + " casillas.";
        }
        statusLabel.setText(result);

        if (juego.isWin()) {
            updateStatus();
            updateBoard();
            javax.swing.JOptionPane.showMessageDialog(this, "¡Ganaste Bomber Mario! Encontraste todas las casillas del tesoro.");
            dispose();
            juego.establecerGanador(true);
            return;
        }
        if (juego.isGameOver()) {
            updateBoard();
            javax.swing.JOptionPane.showMessageDialog(this, "No encontraste todas las casillas del tesoro. Perdiste Bomber Mario.");
            dispose();
            juego.establecerGanador(false);
            return;
        }
    }

    private void applyImpact(List<int[]> impacto) {
        for (int[] pos : impacto) {
            int r = pos[0];
            int c = pos[1];
            if (r < 0 || r >= juego.getSize() || c < 0 || c >= juego.getSize()) {
                continue;
            }
            JButton button = buttons[r][c];
            if (juego.getDiscovered()[r][c]) {
                if (juego.getTreasure()[r][c]) {
                    button.setText("T");
                    button.setBackground(Color.ORANGE);
                } else {
                    button.setText("O");
                    button.setBackground(Color.WHITE);
                }
                button.setEnabled(false);
            }
        }
    }

    private void updateBoard() {
        for (int r = 0; r < juego.getSize(); r++) {
            for (int c = 0; c < juego.getSize(); c++) {
                JButton button = buttons[r][c];
                if (juego.getDiscovered()[r][c]) {
                    if (juego.getTreasure()[r][c]) {
                        button.setText("T");
                        button.setBackground(Color.ORANGE);
                    } else {
                        button.setText("O");
                        button.setBackground(Color.WHITE);
                    }
                    button.setEnabled(false);
                }
            }
        }
    }

    private void updateStatus() {
        bombsLabel.setText("Bombas restantes: " + juego.getBombsLeft());
        foundLabel.setText("Tesoro descubierto: " + juego.getFoundTreasureCells() + "/4");
        if (juego.isGameOver()) {
            statusLabel.setText(juego.isWin() ? "¡Has ganado!" : "Se acabaron las bombas.");
            disableBoard();
        }
    }

    private void disableBoard() {
        for (int r = 0; r < juego.getSize(); r++) {
            for (int c = 0; c < juego.getSize(); c++) {
                buttons[r][c].setEnabled(false);
            }
        }
    }
}
