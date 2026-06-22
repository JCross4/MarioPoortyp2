package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import Client.Cliente;

public class SopaLetrasPantalla extends JDialog {
    private static final long serialVersionUID = 1L;
    private Cliente cliente;
    private char[][] tablero;
    private String[] palabras;
    private JButton[][] botones;
    private JLabel tiempoLabel;
    private JLabel encontradasLabel;
    private JTextArea palabraActualArea;
    private JPanel gridPanel;
    private long tiempoInicio;
    private long tiempoLimite;
    private boolean[][] seleccionadas;
    private StringBuilder palabraActual;
    private Set<String> palabrasEncontradas;
    private Set<Integer> indicesSeleccionados;
    private Thread timerThread;
    private boolean juegoActivo;

    public SopaLetrasPantalla(Cliente cliente, char[][] tablero, String[] palabras) {
        super(cliente.getPantalla(), "Sopa de Letras", true);
        this.cliente = cliente;
        this.tablero = tablero;
        this.palabras = palabras;
        this.botones = new JButton[tablero.length][tablero[0].length];
        this.seleccionadas = new boolean[tablero.length][tablero[0].length];
        this.palabraActual = new StringBuilder();
        this.palabrasEncontradas = new HashSet<>();
        this.indicesSeleccionados = new HashSet<>();
        this.tiempoInicio = System.currentTimeMillis();
        this.tiempoLimite = 2 * 60 * 1000;
        this.juegoActivo = true;

        inicializarGUI();
        iniciarTimer();
    }

    private void inicializarGUI() {
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setSize(900, 800);
        setLocationRelativeTo(cliente.getPantalla());
        setLayout(new BorderLayout(10, 10));

        // Panel superior con información
        JPanel panelInfo = crearPanelInfo();
        add(panelInfo, BorderLayout.NORTH);

        // Panel central con la grilla
        gridPanel = new JPanel(new GridLayout(tablero.length, tablero[0].length, 3, 3));
        gridPanel.setBackground(Color.DARK_GRAY);
        gridPanel.setBorder(new TitledBorder("Tablero"));
        crearBotonesDeLaGrilla();
        add(gridPanel, BorderLayout.CENTER);

        // Panel sur con palabras encontradas y controles
        JPanel panelSur = crearPanelSur();
        add(panelSur, BorderLayout.SOUTH);
    }

    private JPanel crearPanelInfo() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(new TitledBorder("Información"));

        // Tiempo restante
        tiempoLabel = new JLabel("Tiempo: 120 segundos");
        tiempoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        tiempoLabel.setForeground(Color.BLUE);

        // Palabras encontradas
        encontradasLabel = new JLabel("Encontradas: 0/" + palabras.length);
        encontradasLabel.setFont(new Font("Arial", Font.BOLD, 14));
        encontradasLabel.setForeground(Color.GREEN);

        // Palabras a encontrar
        StringBuilder sb = new StringBuilder("<html>Palabras a encontrar: ");
        for (int i = 0; i < palabras.length; i++) {
            sb.append(palabras[i]);
            if (i < palabras.length - 1) sb.append(", ");
        }
        sb.append("</html>");
        JLabel palabrasLabel = new JLabel(sb.toString());
        palabrasLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBackground(new Color(240, 240, 240));
        panelTop.add(tiempoLabel, BorderLayout.WEST);
        panelTop.add(encontradasLabel, BorderLayout.EAST);

        panel.add(panelTop, BorderLayout.NORTH);
        panel.add(palabrasLabel, BorderLayout.CENTER);

        return panel;
    }

    private void crearBotonesDeLaGrilla() {
        for (int fila = 0; fila < tablero.length; fila++) {
            for (int col = 0; col < tablero[fila].length; col++) {
                JButton btn = new JButton(String.valueOf(tablero[fila][col]));
                btn.setFont(new Font("Arial", Font.BOLD, 18));
                btn.setBackground(new Color(220, 220, 220));
                btn.setFocusPainted(false);
                btn.setBorder(null);

                final int f = fila;
                final int c = col;

                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        seleccionarLetra(f, c, btn);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (seleccionadas[f][c]) {
                            btn.setBackground(new Color(100, 200, 100));
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (seleccionadas[f][c]) {
                            btn.setBackground(new Color(100, 180, 100));
                        } else {
                            btn.setBackground(new Color(220, 220, 220));
                        }
                    }
                });

                botones[fila][col] = btn;
                gridPanel.add(btn);
            }
        }
    }

    private void seleccionarLetra(int fila, int col, JButton btn) {
        if (seleccionadas[fila][col]) {
            // Deseleccionar
            seleccionadas[fila][col] = false;
            btn.setBackground(new Color(220, 220, 220));
            indicesSeleccionados.remove(fila * tablero[0].length + col);
            palabraActual.setLength(0);
            reconstruirPalabra();
        } else {
            // Seleccionar
            seleccionadas[fila][col] = true;
            btn.setBackground(new Color(100, 180, 100));
            indicesSeleccionados.add(fila * tablero[0].length + col);
            palabraActual.append(tablero[fila][col]);
        }
        palabraActualArea.setText(palabraActual.toString());
    }

    private void reconstruirPalabra() {
        palabraActual.setLength(0);
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                if (seleccionadas[i][j]) {
                    palabraActual.append(tablero[i][j]);
                }
            }
        }
    }

    private JPanel crearPanelSur() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Controles"));
        panel.setBackground(new Color(240, 240, 240));

        // Palabra actual
        JPanel panelPalabra = new JPanel(new BorderLayout());
        panelPalabra.setBackground(new Color(240, 240, 240));
        JLabel labelPalabra = new JLabel("Palabra: ");
        labelPalabra.setFont(new Font("Arial", Font.BOLD, 12));
        palabraActualArea = new JTextArea(1, 15);
        palabraActualArea.setFont(new Font("Arial", Font.BOLD, 14));
        palabraActualArea.setEditable(false);
        palabraActualArea.setBackground(new Color(255, 255, 200));
        panelPalabra.add(labelPalabra, BorderLayout.WEST);
        panelPalabra.add(palabraActualArea, BorderLayout.CENTER);

        // Botones de acción
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(240, 240, 240));

        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnConfirmar.addActionListener(e -> confirmarPalabra());

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnLimpiar.addActionListener(e -> limpiarSeleccion());

        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Arial", Font.PLAIN, 12));
        btnSalir.addActionListener(e -> terminarJuego());

        panelBotones.add(btnConfirmar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnSalir);

        panel.add(panelPalabra, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.EAST);

        return panel;
    }

    private void iniciarTimer() {
        timerThread = new Thread(() -> {
            while (juegoActivo) {
                long ahora = System.currentTimeMillis();
                long transcurrido = ahora - tiempoInicio;
                long restante = (tiempoLimite - transcurrido) / 1000;

                if (restante <= 0) {
                    juegoActivo = false;
                    tiempoLabel.setText("Tiempo: 0 segundos");
                    terminarJuegoAuto();
                    break;
                }

                tiempoLabel.setText("Tiempo: " + restante + " segundos");
                tiempoLabel.setForeground(restante < 30 ? Color.RED : Color.BLUE);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        timerThread.setDaemon(true);
        timerThread.start();
    }

    private void confirmarPalabra() {
        String palabra = palabraActual.toString().toLowerCase();
        if (palabra.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar una palabra", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean valida = false;
        for (String p : palabras) {
            if (p.equalsIgnoreCase(palabra)) {
                valida = true;
                break;
            }
        }

        if (!valida) {
            JOptionPane.showMessageDialog(this, "Palabra no encontrada en la lista", "Incorrecto", JOptionPane.WARNING_MESSAGE);
            limpiarSeleccion();
            return;
        }

        if (palabrasEncontradas.contains(palabra)) {
            JOptionPane.showMessageDialog(this, "Ya encontraste esa palabra", "Información", JOptionPane.INFORMATION_MESSAGE);
            limpiarSeleccion();
            return;
        }

        palabrasEncontradas.add(palabra);
        encontradasLabel.setText("Encontradas: " + palabrasEncontradas.size() + "/" + palabras.length);
        encontradasLabel.setForeground(palabrasEncontradas.size() == palabras.length ? Color.GREEN : new Color(0, 150, 0));

        JOptionPane.showMessageDialog(this, "¡Correcto! Encontraste: " + palabra + " (" + palabrasEncontradas.size() + "/" + palabras.length + ")", "¡Bien!", JOptionPane.INFORMATION_MESSAGE);

        limpiarSeleccion();

        if (palabrasEncontradas.size() == palabras.length) {
            juegoActivo = false;
            terminarJuegoGanador();
        }
    }

    private void limpiarSeleccion() {
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                if (seleccionadas[i][j]) {
                    seleccionadas[i][j] = false;
                    botones[i][j].setBackground(new Color(220, 220, 220));
                }
            }
        }
        indicesSeleccionados.clear();
        palabraActual.setLength(0);
        palabraActualArea.setText("");
    }

    private void terminarJuegoAuto() {
        juegoActivo = false;
        cliente.setUltimoJuegoGanado(false);
        dispose();
    }

    private void terminarJuego() {
        juegoActivo = false;
        cliente.setUltimoJuegoGanado(false);
        dispose();
    }

    private void terminarJuegoGanador() {
        juegoActivo = false;
        cliente.setUltimoJuegoGanado(true);
        dispose();
    }

    public boolean esGanador() {
        return palabrasEncontradas.size() == palabras.length;
    }
}
