package Models.Juegos;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JOptionPane;

import Client.Cliente;

public class CatchTheCatJuego implements Juego {
    private static final int SIZE = 11;

    private Cliente cliente;
    private boolean[][] blocked;
    private int catRow;
    private int catCol;

    public CatchTheCatJuego(Cliente cliente) {
        this.cliente = cliente;
        this.blocked = new boolean[SIZE][SIZE];
        this.catRow = SIZE / 2; // center
        this.catCol = SIZE / 2;
    }

    @Override
    public void iniciar() {
        cliente.getPantalla().mostrarMensaje("Se inició Catch the Cat. Intenta encerrar al gato bloqueando casillas.");

        while (true) {
            mostrarTablero();
            List<int[]> path = findPathToBorder(catRow, catCol);
            if (path == null) {
                cliente.getPantalla().mostrarMensaje("¡Has encerrado al gato! Ganaste.");
                cliente.setUltimoJuegoGanado(true);
                return;
            }

            String input = JOptionPane.showInputDialog(cliente.getPantalla(), "Ingresa fila,col (1-11) para bloquear, o cancelar para salir:\nEjemplo: 3,5");
            if (input == null) {
                cliente.getPantalla().mostrarMensaje("Juego cancelado. Debes repetir la casilla.");
                cliente.setUltimoJuegoGanado(false);
                return;
            }
            input = input.trim();
            String[] parts = input.split("\\s*,\\s*");
            if (parts.length != 2) {
                cliente.getPantalla().mostrarMensaje("Entrada inválida. Usa el formato fila,col (por ejemplo: 4,7).");
                continue;
            }
            int r, c;
            try {
                r = Integer.parseInt(parts[0]) - 1;
                c = Integer.parseInt(parts[1]) - 1;
            } catch (NumberFormatException e) {
                cliente.getPantalla().mostrarMensaje("Valores inválidos. Deben ser números entre 1 y 11.");
                continue;
            }
            if (r < 0 || r >= SIZE || c < 0 || c >= SIZE) {
                cliente.getPantalla().mostrarMensaje("Coordenadas fuera de rango. Usar valores entre 1 y 11.");
                continue;
            }
            if (r == catRow && c == catCol) {
                cliente.getPantalla().mostrarMensaje("No puedes bloquear la casilla donde está el gato.");
                continue;
            }
            if (blocked[r][c]) {
                cliente.getPantalla().mostrarMensaje("Esa casilla ya está bloqueada.");
                continue;
            }

            blocked[r][c] = true;

            path = findPathToBorder(catRow, catCol);
            if (path == null) {
                mostrarTablero();
                cliente.getPantalla().mostrarMensaje("¡Has encerrado al gato! Ganaste.");
                cliente.setUltimoJuegoGanado(true);
                return;
            }

            if (path.size() >= 2) {
                int[] next = path.get(1);
                catRow = next[0];
                catCol = next[1];
            }

            if (isBorder(catRow, catCol)) {
                mostrarTablero();
                cliente.getPantalla().mostrarMensaje("El gato escapó por el borde. Perdiste.");
                cliente.setUltimoJuegoGanado(false);
                return;
            }
        }
    }

    private boolean isBorder(int r, int c) {
        return r == 0 || r == SIZE - 1 || c == 0 || c == SIZE - 1;
    }

    private void mostrarTablero() {
        StringBuilder sb = new StringBuilder();
        sb.append("Catch the Cat (" + SIZE + "x" + SIZE + ")\n");
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (i == catRow && j == catCol) sb.append('C');
                else if (blocked[i][j]) sb.append('X');
                else sb.append('.');
                sb.append(' ');
            }
            sb.append('\n');
        }
        cliente.getPantalla().mostrarMensaje(sb.toString());
    }

    private List<int[]> findPathToBorder(int sr, int sc) {
        int rows = SIZE, cols = SIZE;
        boolean[][] visited = new boolean[rows][cols];
        int[][] parent = new int[rows * cols][2];
        for (int i = 0; i < parent.length; i++) parent[i] = new int[] {-1, -1};

        Queue<int[]> q = new LinkedList<>();
        q.add(new int[] {sr, sc});
        visited[sr][sc] = true;

        int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}}; // 4-directional

        int[] dest = null;
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int r = cur[0], c = cur[1];
            if (isBorder(r,c)) { dest = cur; break; }
            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
                if (visited[nr][nc]) continue;
                if (blocked[nr][nc]) continue;
                visited[nr][nc] = true;
                parent[nr * cols + nc] = new int[] {r, c};
                q.add(new int[] {nr, nc});
            }
        }

        if (dest == null) return null;

        List<int[]> path = new ArrayList<>();
        int r = dest[0], c = dest[1];
        while (!(r == sr && c == sc)) {
            path.add(0, new int[] {r, c});
            int[] p = parent[r * cols + c];
            r = p[0]; c = p[1];
        }
        path.add(0, new int[] {sr, sc});
        return path;
    }

    @Override
    public void establecerGanador(boolean victoria) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'establecerGanador'");
    }
}
