package Models.Juegos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import Client.Cliente;

public class CollectTheCoinsJuego implements Juego {
    private static final int SIZE = 25;
    private Cliente cliente;
    private int[][] coins; 
    private long timeLimitMs;

    public CollectTheCoinsJuego(Cliente cliente) {
        this.cliente = cliente;
        this.coins = new int[SIZE][SIZE];
        generarMonedas();
        seleccionarTiempo();
    }

    @Override
    public void iniciar() {
        cliente.getPantalla().mostrarMensaje("Se inició Collect the Coins. Tienes " + (timeLimitMs/1000) + " segundos para recolectar monedas.");

        long start = System.currentTimeMillis();
        long end = start + timeLimitMs;
        int total = 0;

        while (System.currentTimeMillis() < end) {
            long restante = (end - System.currentTimeMillis()) / 1000;
            String prompt = "Tiempo restante: " + restante + "s\nIngresa fila,col para recolectar (1-25), o Cancelar para terminar:\nEjemplo: 3,5";
            String input = JOptionPane.showInputDialog(cliente.getPantalla(), prompt);
            if (input == null) break;
            input = input.trim();
            String[] parts = input.split("\\s*,\\s*");
            if (parts.length != 2) {
                cliente.getPantalla().mostrarMensaje("Entrada inválida. Usa formato fila,col (por ejemplo: 4,7).");
                continue;
            }
            int r,c;
            try {
                r = Integer.parseInt(parts[0]) - 1;
                c = Integer.parseInt(parts[1]) - 1;
            } catch (NumberFormatException ex) {
                cliente.getPantalla().mostrarMensaje("Valores inválidos. Deben ser números entre 1 y 25.");
                continue;
            }
            if (r < 0 || r >= SIZE || c < 0 || c >= SIZE) {
                cliente.getPantalla().mostrarMensaje("Coordenadas fuera de rango. Usa valores entre 1 y 25.");
                continue;
            }
            int val = coins[r][c];
            total += val;
            coins[r][c] = 0; // collected
            cliente.getPantalla().mostrarMensaje("Recolectaste: " + val + ". Total parcial: " + total);
        }

        // Resultado
        cliente.getPantalla().mostrarMensaje("Tiempo finalizado o usuario terminó. Total recolectado: " + total);
        if (total > 0) {
            cliente.getPantalla().mostrarMensaje("¡Ganaste Collect the Coins! Puedes lanzar los dados en tu siguiente turno.");
            cliente.setUltimoJuegoGanado(true);
        } else {
            cliente.getPantalla().mostrarMensaje("No obtuviste un valor positivo. Debes volver a jugar la misma casilla.");
            cliente.setUltimoJuegoGanado(false);
        }
    }

    private void seleccionarTiempo() {
        int[] opciones = {30, 45, 60};
        Random rand = new Random();
        int elegido = opciones[rand.nextInt(opciones.length)];
        this.timeLimitMs = elegido * 1000L;
    }

    private void generarMonedas() {
        int total = SIZE * SIZE;
        int half = total / 2;
        int[] values = {5,10};
        List<Integer> lista = new ArrayList<>(total);
        Random rand = new Random();
        for (int i = 0; i < half; i++) {
            lista.add(values[rand.nextInt(2)]); // 1..10
        }
        for (int i = half; i < total; i++) {
            lista.add(- values[rand.nextInt(2)]); // -1..-10
        }
        Collections.shuffle(lista, rand);
        int idx = 0;
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                coins[r][c] = lista.get(idx++);
            }
        }
    }

    @Override
    public void establecerGanador(boolean victoria) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'establecerGanador'");
    }
}
