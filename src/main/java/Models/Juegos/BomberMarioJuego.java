package Models.Juegos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Client.Cliente;
import GUI.BomberMarioPantalla;

public class BomberMarioJuego implements Juego {
    private static final int[] BOARD_SIZES = {10, 15, 20};
    private static final int BOMBS = 7;

    public enum BombType {
        SIMPLE("Simple"),
        DOBLE("Doble"),
        CRUZ("Cruz"),
        LINEA("Línea");

        private final String nombre;

        BombType(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }
    }

    private Cliente cliente;
    private int size;
    private boolean[][] treasure;
    private boolean[][] discovered;
    private int bombsLeft;
    private int foundTreasureCells;
    private Random random;

    public BomberMarioJuego(Cliente cliente) {
        this.cliente = cliente;
        this.random = new Random();
        this.size = BOARD_SIZES[random.nextInt(BOARD_SIZES.length)];
        this.treasure = new boolean[size][size];
        this.discovered = new boolean[size][size];
        this.bombsLeft = BOMBS;
        this.foundTreasureCells = 0;
        generarTesoro();
    }

    @Override
    public void iniciar() {
        BomberMarioPantalla pantalla = new BomberMarioPantalla(cliente.getPantalla(), this);
        pantalla.setVisible(true);
    }

    public int getSize() {
        return size;
    }

    public boolean[][] getDiscovered() {
        return discovered;
    }

    public boolean[][] getTreasure() {
        return treasure;
    }

    public int getBombsLeft() {
        return bombsLeft;
    }

    public int getFoundTreasureCells() {
        return foundTreasureCells;
    }

    public String[] getBombTypeNames() {
        BombType[] types = BombType.values();
        String[] names = new String[types.length];
        for (int i = 0; i < types.length; i++) {
            names[i] = types[i].getNombre();
        }
        return names;
    }

    public BombType getBombTypeByName(String name) {
        for (BombType type : BombType.values()) {
            if (type.getNombre().equals(name)) {
                return type;
            }
        }
        return BombType.SIMPLE;
    }

    public void decrementBomb() {
        if (bombsLeft > 0) {
            bombsLeft--;
        }
    }

    public boolean isGameOver() {
        return bombsLeft <= 0 || foundTreasureCells >= 4;
    }

    public boolean isWin() {
        return foundTreasureCells >= 4;
    }

    public List<int[]> obtenerImpacto(BombType tipo, int fila, int col) {
        List<int[]> casillas = new ArrayList<>();
        casillas.add(new int[] {fila, col});

        switch (tipo) {
            case SIMPLE:
                break;
            case DOBLE:
                casillas.addAll(obtenerImpactoDoble(fila, col));
                break;
            case CRUZ:
                casillas.addAll(obtenerImpactoCruz(fila, col));
                break;
            case LINEA:
                casillas.addAll(obtenerImpactoLinea(fila, col));
                break;
        }
        return casillas;
    }

    private List<int[]> obtenerImpactoDoble(int fila, int col) {
        List<int[]> resultado = new ArrayList<>();
        int[][] offsets = {{0,0},{0,-1},{-1,0},{-1,-1}};
        List<int[]> orientaciones = new ArrayList<>();
        for (int[] off : offsets) {
            int baseR = fila + off[0];
            int baseC = col + off[1];
            if (baseR >= 0 && baseR < size - 1 && baseC >= 0 && baseC < size - 1) {
                orientaciones.add(new int[] {baseR, baseC});
            }
        }
        if (orientaciones.isEmpty()) {
            return resultado;
        }
        int[] seleccion = orientaciones.get(random.nextInt(orientaciones.size()));
        int row0 = seleccion[0];
        int col0 = seleccion[1];
        resultado.add(new int[] {row0, col0});
        resultado.add(new int[] {row0, col0 + 1});
        resultado.add(new int[] {row0 + 1, col0});
        resultado.add(new int[] {row0 + 1, col0 + 1});
        return resultado;
    }

    private List<int[]> obtenerImpactoCruz(int fila, int col) {
        List<int[]> resultado = new ArrayList<>();
        resultado.add(new int[] {fila, col});
        if (fila - 1 >= 0) resultado.add(new int[] {fila - 1, col});
        if (fila + 1 < size) resultado.add(new int[] {fila + 1, col});
        if (col - 1 >= 0) resultado.add(new int[] {fila, col - 1});
        if (col + 1 < size) resultado.add(new int[] {fila, col + 1});
        return resultado;
    }

    private List<int[]> obtenerImpactoLinea(int fila, int col) {
        List<int[]> resultado = new ArrayList<>();
        boolean horizontal = random.nextBoolean();
        if (horizontal) {
            int start = Math.max(0, Math.min(col - 1, size - 4));
            for (int c = start; c < start + 4; c++) {
                resultado.add(new int[] {fila, c});
            }
        } else {
            int start = Math.max(0, Math.min(fila - 1, size - 4));
            for (int r = start; r < start + 4; r++) {
                resultado.add(new int[] {r, col});
            }
        }
        return resultado;
    }

    public int procesarImpacto(List<int[]> casillas) {
        int hitsTesoro = 0;
        for (int[] pos : casillas) {
            int r = pos[0];
            int c = pos[1];
            if (r < 0 || r >= size || c < 0 || c >= size) {
                continue;
            }
            if (!discovered[r][c]) {
                discovered[r][c] = true;
                if (treasure[r][c]) {
                    foundTreasureCells++;
                    hitsTesoro++;
                }
            }
        }
        return hitsTesoro;
    }

    private String obtenerTableroTexto() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tablero:\n");
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (!discovered[r][c]) {
                    sb.append('.');
                } else if (treasure[r][c]) {
                    sb.append('T');
                } else {
                    sb.append('O');
                }
                sb.append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private void generarTesoro() {
        int forma = random.nextInt(3);
        switch (forma) {
            case 0:
                int row = random.nextInt(size - 1);
                int col = random.nextInt(size - 1);
                colocarBloque2x2(row, col);
                break;
            case 1:
                int rowH = random.nextInt(size);
                int colH = random.nextInt(size - 4 + 1);
                colocarLineaHorizontal(rowH, colH);
                break;
            default:
                int rowV = random.nextInt(size - 4 + 1);
                int colV = random.nextInt(size);
                colocarLineaVertical(rowV, colV);
                break;
        }
    }

    private void colocarBloque2x2(int row, int col) {
        treasure[row][col] = true;
        treasure[row][col + 1] = true;
        treasure[row + 1][col] = true;
        treasure[row + 1][col + 1] = true;
    }

    private void colocarLineaHorizontal(int row, int startCol) {
        for (int c = startCol; c < startCol + 4; c++) {
            treasure[row][c] = true;
        }
    }

    private void colocarLineaVertical(int startRow, int col) {
        for (int r = startRow; r < startRow + 4; r++) {
            treasure[r][col] = true;
        }
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public void establecerGanador(boolean victoria) {
        // TODO Auto-generated method stub
        cliente.setUltimoJuegoGanado(victoria);
        throw new UnsupportedOperationException("Unimplemented method 'establecerGanador'");
    }

    
}
