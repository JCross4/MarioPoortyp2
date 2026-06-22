package Models.Juegos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Client.Cliente;
import GUI.SopaLetrasPantalla;

public class SopaLetrasJuego implements Juego{
    private Cliente cliente;
    //Modificar: mostrar tiempo/mostrar sopa/ordenar sopa/ no mostrar palabras


    private static final String[] SOPA_LETRAS_5 = {
        "sobre", "parar", "puede", "forma", "todos", "menos", "hacer", "estar",
        "poder", "antes", "tarde", "calle", "fuera", "vista", "comer", "hecho",
        "libre", "ayuda", "sigue", "cinco", "cerca", "cosas", "mayor", "media",
        "deber", "doble", "abajo", "abril", "acero", "barra", "bomba", "bueno",
        "campo", "carne", "carro", "cueva", "curso", "disco", "dulce", "enero",
        "espia", "etapa", "final", "fresa", "fuego", "kayak", "leche", "nadie",
        "oasis", "radio"
    };

    private static final String[] SOPA_LETRAS_10 = {
        "significar", "especiales", "esperanzar", "trabajando", "desarrolla", "recomienda",
        "ciudadanos", "comunicado", "superiores", "disciplina", "comprender", "manifestar",
        "asistentes", "dirigentes", "anteriores", "diferencia", "presidente", "transporte",
        "resultados", "entrevista", "fotografia", "encuentran", "estructura", "representa",
        "importante", "influencia", "cualquiera", "siguientes", "referencia", "nacionales",
        "materiales", "constitute", "personales", "residencia", "considerar", "particular",
        "familiares", "habitantes", "abecedario", "acantilado", "alienigena", "continente",
        "daltonismo", "definitivo", "individual", "invencible", "septiembre", "voluntario",
        "matematico", "inventario"
    };

    

    public SopaLetrasJuego(Cliente cliente) {
        this.cliente = cliente;
    }


    @Override
    public void iniciar() {
        int[] tamanos = {10, 15, 20};
        int tamaño = tamanos[new Random().nextInt(tamanos.length)];
        String[] palabras = seleccionarPalabrasSopaLetras();
        char[][] tableroSopa = generarTableroSopaLetras(tamaño, palabras);
        if (tableroSopa == null) {
            cliente.getPantalla().mostrarMensaje("No se pudo generar la sopa de letras. Intenta de nuevo más tarde.");
            return;
        }

        SopaLetrasPantalla pantalla = new SopaLetrasPantalla(cliente, tableroSopa, palabras);
        pantalla.setVisible(true);

        if (pantalla.esGanador()) {
            cliente.getPantalla().mostrarMensaje("¡Ganaste la sopa de letras! Puedes lanzar los dados en tu siguiente turno.");
            cliente.setUltimoJuegoGanado(true);
        } else {
            cliente.getPantalla().mostrarMensaje("No encontraste las 4 palabras en 2 minutos. Debes volver a jugar la misma casilla.");
            cliente.setUltimoJuegoGanado(false);
        }
    }


    public String[] seleccionarPalabrasSopaLetras() {
        ArrayList<String> seleccionadas = new ArrayList<>();
        Random rand = new Random();
        while (seleccionadas.size() < 2) {
            String palabra = SOPA_LETRAS_5[rand.nextInt(SOPA_LETRAS_5.length)];
            if (!seleccionadas.contains(palabra)) {
                seleccionadas.add(palabra);
            }
        }
        while (seleccionadas.size() < 4) {
            String palabra = SOPA_LETRAS_10[rand.nextInt(SOPA_LETRAS_10.length)];
            if (!seleccionadas.contains(palabra)) {
                seleccionadas.add(palabra);
            }
        }
        return seleccionadas.toArray(new String[0]);
    }

    public char[][] generarTableroSopaLetras(int tamaño, String[] palabras) {
        char[][] tableroSopa = new char[tamaño][tamaño];
        int[] orientaciones = {0, 1, 2, 3};
        for (int i = 0; i < palabras.length; i++) {
            boolean colocado = colocarPalabra(tableroSopa, palabras[i], orientaciones[i]);
            if (!colocado) {
                for (int reintento = 0; reintento < 3 && !colocado; reintento++) {
                    colocado = colocarPalabra(tableroSopa, palabras[i], orientaciones[i]);
                }
                if (!colocado) {
                    return null;
                }
            }
        }
        llenarTableroAleatorio(tableroSopa);
        return tableroSopa;
    }

    public boolean colocarPalabra(char[][] tablero, String palabra, int orientacion) {
        Random rand = new Random();
        int tamaño = tablero.length;
        int longitud = palabra.length();
        for (int intento = 0; intento < 200; intento++) {
            int fila = 0;
            int columna = 0;
            switch (orientacion) {
                case 0: // horizontal
                    fila = rand.nextInt(tamaño);
                    columna = rand.nextInt(tamaño - longitud + 1);
                    break;
                case 1: // vertical
                    fila = rand.nextInt(tamaño - longitud + 1);
                    columna = rand.nextInt(tamaño);
                    break;
                case 2: // diagonal descendente derecha
                    fila = rand.nextInt(tamaño - longitud + 1);
                    columna = rand.nextInt(tamaño - longitud + 1);
                    break;
                default: // diagonal descendente izquierda
                    fila = rand.nextInt(tamaño - longitud + 1);
                    columna = rand.nextInt(tamaño - longitud + 1) + longitud - 1;
                    break;
            }
            if (puedeColocar(tablero, palabra, fila, columna, orientacion)) {
                colocarEnTablero(tablero, palabra, fila, columna, orientacion);
                return true;
            }
        }
        return false;
    }

    public boolean puedeColocar(char[][] tablero, String palabra, int fila, int columna, int orientacion) {
        int tamaño = tablero.length;
        for (int i = 0; i < palabra.length(); i++) {
            int filaActual = fila;
            int columnaActual = columna;
            switch (orientacion) {
                case 0:
                    columnaActual += i;
                    break;
                case 1:
                    filaActual += i;
                    break;
                case 2:
                    filaActual += i;
                    columnaActual += i;
                    break;
                default:
                    filaActual += i;
                    columnaActual -= i;
                    break;
            }
            if (filaActual < 0 || filaActual >= tamaño || columnaActual < 0 || columnaActual >= tamaño) {
                return false;
            }
            char actual = tablero[filaActual][columnaActual];
            if (actual != '\u0000' && actual != palabra.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public void colocarEnTablero(char[][] tablero, String palabra, int fila, int columna, int orientacion) {
        for (int i = 0; i < palabra.length(); i++) {
            int filaActual = fila;
            int columnaActual = columna;
            switch (orientacion) {
                case 0:
                    columnaActual += i;
                    break;
                case 1:
                    filaActual += i;
                    break;
                case 2:
                    filaActual += i;
                    columnaActual += i;
                    break;
                default:
                    filaActual += i;
                    columnaActual -= i;
                    break;
            }
            tablero[filaActual][columnaActual] = palabra.charAt(i);
        }
    }

    public void llenarTableroAleatorio(char[][] tablero) {
        Random rand = new Random();
        for (int fila = 0; fila < tablero.length; fila++) {
            for (int columna = 0; columna < tablero[fila].length; columna++) {
                if (tablero[fila][columna] == '\u0000') {
                    tablero[fila][columna] = (char) ('A' + rand.nextInt(26));
                } else {
                    tablero[fila][columna] = Character.toUpperCase(tablero[fila][columna]);
                }
            }
        }
    }

    public void mostrarTableroSopaLetras(char[][] tablero, String[] palabras) {
        StringBuilder sb = new StringBuilder();
        sb.append("Sopa de letras (" + tablero.length + "x" + tablero.length + ")\n");
        sb.append("Palabras a encontrar: ");
        sb.append(String.join(", ", palabras));
        sb.append("\n\n");
        for (int fila = 0; fila < tablero.length; fila++) {
            for (int columna = 0; columna < tablero[fila].length; columna++) {
                sb.append(tablero[fila][columna]);
                sb.append(' ');
            }
            sb.append("\n");
        }
        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setRows(Math.min(tablero.length + 5, 30));
        area.setColumns(Math.min(tablero.length * 2, 60));
        JScrollPane scroll = new JScrollPane(area);
        JOptionPane.showMessageDialog(cliente.getPantalla(), scroll, "Sopa de letras", JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean jugarSopaLetras(String[] palabras) {
        long inicio = System.currentTimeMillis();
        long limite = 2 * 60 * 1000;
        Set<String> encontradas = new HashSet<>();
        Set<String> palabrasLower = new HashSet<>();
        for (String palabra : palabras) {
            palabrasLower.add(palabra.toLowerCase());
        }

        while (System.currentTimeMillis() - inicio < limite && encontradas.size() < palabras.length) {
            long restante = (limite - (System.currentTimeMillis() - inicio)) / 1000;
            String mensaje = "Tiempo restante: " + restante + " segundos\n" +
                    "Palabras a encontrar: " + String.join(", ", palabras) + "\n" +
                    "Encontradas: " + (encontradas.isEmpty() ? "ninguna" : String.join(", ", encontradas)) + "\n\n" +
                    "Ingresa una palabra encontrada:";
            String respuesta = JOptionPane.showInputDialog(cliente.getPantalla(), mensaje);
            if (respuesta == null) {
                break;
            }
            respuesta = respuesta.trim().toLowerCase();
            if (respuesta.isEmpty()) {
                continue;
            }
            if (encontradas.contains(respuesta)) {
                cliente.getPantalla().mostrarMensaje("Ya encontraste esa palabra: " + respuesta);
            } else if (palabrasLower.contains(respuesta)) {
                encontradas.add(respuesta);
                cliente.getPantalla().mostrarMensaje("¡Correcto! Encontraste: " + respuesta + " (" + encontradas.size() + "/" + palabras.length + ")");
            } else {
                cliente.getPantalla().mostrarMensaje("Palabra no válida o incorrecta: " + respuesta);
            }
        }

        return encontradas.size() == palabras.length;
    }


    @Override
    public void establecerGanador(boolean victoria) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'establecerGanador'");
    }


}
