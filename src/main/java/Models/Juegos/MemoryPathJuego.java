package Models.Juegos;

import java.util.Random;

import javax.swing.JOptionPane;

import Client.Cliente;

public class MemoryPathJuego implements Juego {
    private static final int FILAS = 6;
    private static final int COLUMNAS = 3;
    private static final int INTENTOS = 3;

    private Cliente cliente;
    private int[] camino;

    public MemoryPathJuego(Cliente cliente) {
        this.cliente = cliente;
        this.camino = generarCaminoAleatorio();
    }

    @Override
    public void iniciar() {
        //mostrarSolucionEnConsola();
        cliente.getPantalla().mostrarMensaje("Se inició Memory path. Debes completar el camino correcto en 3 intentos.");

        boolean gano = false;
        for (int intento = 1; intento <= INTENTOS && !gano; intento++) {
            cliente.getPantalla().mostrarMensaje("Intento " + intento + " de " + INTENTOS + ".");
            gano = jugarIntento();
            if (!gano) {
                cliente.getPantalla().mostrarMensaje("Fallaste el intento " + intento + ". El juego se reinicia desde el inicio.");
            }
        }

        if (gano) {
            cliente.getPantalla().mostrarMensaje("¡Felicidades! Completaste Memory path. Podrás lanzar los dados en tu siguiente turno.");
            cliente.setUltimoJuegoGanado(true);
        } else {
            cliente.getPantalla().mostrarMensaje("No lograste completar el camino en 3 intentos. Debes repetir esta casilla la próxima vez.");
            cliente.setUltimoJuegoGanado(false);
        }
    }

    private int[] generarCaminoAleatorio() {
        Random random = new Random();
        int[] camino = new int[FILAS];
        for (int fila = 0; fila < FILAS; fila++) {
            camino[fila] = random.nextInt(COLUMNAS);
        }
        return camino;
    }

    private boolean jugarIntento() {
        for (int fila = 0; fila < FILAS; fila++) {
            int seleccion = solicitarSeleccionFila(fila + 1);
            if (seleccion < 0) {
                cliente.getPantalla().mostrarMensaje("Juego cancelado. Debes volver a jugar la misma casilla.");
                cliente.setUltimoJuegoGanado(false);
                return false;
            }
            if (seleccion != camino[fila]) {
                cliente.getPantalla().mostrarMensaje("Incorrecto en la fila " + (fila + 1) + ". La respuesta correcta era columna " + (camino[fila] + 1) + ".");
                return false;
            }
        }
        return true;
    }

    private int solicitarSeleccionFila(int fila) {
        String[] opciones = {"Columna 1", "Columna 2", "Columna 3"};
        StringBuilder sb = new StringBuilder();
        sb.append("Memory path - solución generada (fila x columna):\n");
        for (int filaPath = 0; filaPath < FILAS; filaPath++) {
            for (int columna = 0; columna < COLUMNAS; columna++) {
                sb.append(camino[filaPath] == columna ? "[X]" : "[ ]");
            }
            sb.append("  fila ").append(filaPath + 1).append("\n");
        }
        String mensaje = sb.append("Selecciona la casilla correcta para la fila " + fila + ".\n" +
                "Elige una columna entre 1 y 3.").toString();
        int seleccion = JOptionPane.showOptionDialog(
                cliente.getPantalla(),
                mensaje,
                "Memory path - Fila " + fila,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );
        return seleccion;
    }

    private void mostrarSolucionEnConsola() {
        StringBuilder sb = new StringBuilder();
        sb.append("Memory path - solución generada (fila x columna):\n");
        for (int fila = 0; fila < FILAS; fila++) {
            for (int columna = 0; columna < COLUMNAS; columna++) {
                sb.append(camino[fila] == columna ? "[X]" : "[ ]");
            }
            sb.append("  fila ").append(fila + 1).append("\n");
        }
        sb.append("Camino exacto: ");
        for (int fila = 0; fila < FILAS; fila++) {
            sb.append(camino[fila] + 1);
            if (fila < FILAS - 1) {
                sb.append(" -> ");
            }
        }
        System.out.println(sb.toString());
    }

    @Override
    public void establecerGanador(boolean victoria) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'establecerGanador'");
    }
}
