package Models.Juegos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarioCardsJuego implements Juego, Serializable {

    public enum Palo {
        CORAZONES(0),
        TREBOL(1),
        PICAS(2),
        ROMBO(3);

        private final int orden;

        Palo(int orden) {
            this.orden = orden;
        }

        public int getOrden() {
            return orden;
        }
    }

    public class Carta implements Serializable, Comparable<Carta> {
        private static final long serialVersionUID = 1L;
        private int valor; // 2-14 (14 = As)
        private Palo palo;

        public Carta(int valor, Palo palo) {
            this.valor = valor;
            this.palo = palo;
        }

        public int getValor() {
            return valor;
        }

        public Palo getPalo() {
            return palo;
        }

        public String getNombre() {
            String nombre = "";
            switch (valor) {
                case 11:
                    nombre = "J";
                    break;
                case 12:
                    nombre = "Q";
                    break;
                case 13:
                    nombre = "K";
                    break;
                case 14:
                    nombre = "As";
                    break;
                default:
                    nombre = String.valueOf(valor);
                    break;
            }
            return nombre;
        }

        public String getPaloNombre() {
            return palo.toString();
        }

        @Override
        public String toString() {
            return getNombre() + " de " + getPaloNombre();
        }

        @Override
        public int compareTo(Carta otra) {
            if (this.valor != otra.valor) {
                return Integer.compare(otra.valor, this.valor); // Mayor valor primero
            }
            return Integer.compare(otra.palo.getOrden(), this.palo.getOrden()); // Mayor orden de palo
        }
    }

    private List<Carta> baraja;
    private Map<String, Carta> cartasJugadores;
    private List<String> jugadores;

    public MarioCardsJuego() {
        this.cartasJugadores = new HashMap<>();
        this.jugadores = new ArrayList<>();
        generarBaraja();
    }

    private void generarBaraja() {
        baraja = new ArrayList<>();
        for (Palo palo : Palo.values()) {
            for (int valor = 2; valor <= 14; valor++) {
                baraja.add(new Carta(valor, palo));
            }
        }
        Collections.shuffle(baraja);
    }

    public void agregarJugador(String nombreJugador) {
        if (!jugadores.contains(nombreJugador)) {
            jugadores.add(nombreJugador);
        }
    }

    public void repartirCarta(String nombreJugador) {
        if (!baraja.isEmpty()) {
            Carta carta = baraja.remove(0);
            cartasJugadores.put(nombreJugador, carta);
        }
    }

    public Carta obtenerCartaJugador(String nombreJugador) {
        return cartasJugadores.get(nombreJugador);
    }

    public String determinarGanador() {
        if (cartasJugadores.isEmpty()) {

            return null;
        }

        List<Map.Entry<String, Carta>> listaCartas = new ArrayList<>(cartasJugadores.entrySet());
        Collections.sort(listaCartas, (a, b) -> a.getValue().compareTo(b.getValue()));

        return listaCartas.get(0).getKey();
    }

    public String obtenerResultadoDetallado() {
        if (cartasJugadores.isEmpty()) {
            return "No hay cartas repartidas.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== Resultado Mario Cards ===\n");

        List<Map.Entry<String, Carta>> listaCartas = new ArrayList<>(cartasJugadores.entrySet());
        Collections.sort(listaCartas, (a, b) -> a.getValue().compareTo(b.getValue()));

        for (int i = 0; i < listaCartas.size(); i++) {
            String nombre = listaCartas.get(i).getKey();
            Carta carta = listaCartas.get(i).getValue();
            String posicion = (i == 0) ? "🥇 Ganador: " : (i == 1 ? "🥈 Segundo: " : "🥉 Tercero: ");
            sb.append(posicion).append(nombre).append(" - ").append(carta.toString()).append("\n");
        }

        return sb.toString();
    }

    @Override
    public void iniciar() {
        // Este método no se usa directamente en el contexto multiplayer
        // La lógica se maneja desde Cliente
    }

    public void reset() {
        cartasJugadores.clear();
        jugadores.clear();
        generarBaraja();
    }

    @Override
    public void establecerGanador(boolean victoria) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'establecerGanador'");
    }
}
