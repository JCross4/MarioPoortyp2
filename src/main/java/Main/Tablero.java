package Main;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Tablero (Board) manages the game board with all spaces and players
 */
public class Tablero implements Serializable {
    private static final long serialVersionUID = 1L;
    
    //Atributos
    private ArrayList<Casilla> casillas;
    private ArrayList<Jugador> jugadores;
    private int tamano;
    private String nombre;
    
    public Tablero() {
        this.casillas = new ArrayList<>();
        this.jugadores = new ArrayList<>();
        this.tamano = 40; // default board size
        this.nombre = "Tablero Default";
        inicializarTablero();
    }
    
    public Tablero(int tamano, String nombre) {
        this.casillas = new ArrayList<>();
        this.jugadores = new ArrayList<>();
        this.tamano = tamano;
        this.nombre = nombre;
        inicializarTablero();
    }
    
    /**
     * Initialize the board with default spaces
     */
    private void inicializarTablero() {
        for (int i = 0; i < tamano; i++) {
            String tipo = "normal";
            int valor = 0;
            
            // Define special spaces
            if (i % 10 == 0) {
                tipo = "especial";
                valor = 200;
            } else if (i % 7 == 0) {
                tipo = "trampa";
                valor = -100;
            } else if (i % 5 == 0 && i % 10 != 0) {
                tipo = "premio";
                valor = 50;
            }
            
            Casilla casilla = new Casilla(i, "Casilla_" + i, tipo, valor);
            casillas.add(casilla);
        }
    }
    
    //Métodos
    public void agregarJugador(Jugador jugador) {
        if (jugadores.size() < 4) { // max 4 players
            jugador.setId(jugadores.size() + 1);
            jugador.setPosicionTablero(0);
            jugador.setCasillaActual(casillas.get(0));
            casillas.get(0).setJugadorActual(jugador);
            jugadores.add(jugador);
        }
    }
    
    public void removerJugador(Jugador jugador) {
        if (casillas.get(jugador.getPosicionTablero()).getJugadorActual() == jugador) {
            casillas.get(jugador.getPosicionTablero()).setJugadorActual(null);
        }
        jugadores.remove(jugador);
    }
    
    public void moverJugador(Jugador jugador, int pasos) {
        // Remove from current space
        Casilla casillaActual = jugador.getCasillaActual();
        if (casillaActual != null && casillaActual.getJugadorActual() == jugador) {
            casillaActual.setJugadorActual(null);
        }
        
        // Calculate new position
        int nuevaPosicion = (jugador.getPosicionTablero() + pasos) % tamano;
        jugador.setPosicionTablero(nuevaPosicion);
        
        // Add to new space
        Casilla nuevaCasilla = casillas.get(nuevaPosicion);
        jugador.setCasillaActual(nuevaCasilla);
        nuevaCasilla.setJugadorActual(jugador);
        
        // Apply space effects
        aplicarEfectoCasilla(jugador, nuevaCasilla);
    }
    
    /**
     * Apply effects when a player lands on a space
     */
    private void aplicarEfectoCasilla(Jugador jugador, Casilla casilla) {
        switch (casilla.getTipo()) {
            case "premio":
                jugador.agregarDinero(casilla.getValor());
                System.out.println(jugador.getNombre() + " ganó $" + casilla.getValor());
                break;
            case "trampa":
                jugador.restarDinero(Math.abs(casilla.getValor()));
                System.out.println(jugador.getNombre() + " perdió $" + Math.abs(casilla.getValor()));
                break;
            case "especial":
                jugador.agregarDinero(casilla.getValor());
                System.out.println(jugador.getNombre() + " encontró un evento especial y ganó $" + casilla.getValor());
                break;
        }
    }
    
    public Casilla getCasilla(int posicion) {
        if (posicion >= 0 && posicion < casillas.size()) {
            return casillas.get(posicion);
        }
        return null;
    }
    
    public ArrayList<Casilla> getCasillas() {
        return casillas;
    }
    
    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }
    
    public Jugador getJugador(int id) {
        for (Jugador j : jugadores) {
            if (j.getId() == id) {
                return j;
            }
        }
        return null;
    }
    
    public int getTamano() {
        return tamano;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public int getNumJugadores() {
        return jugadores.size();
    }
    
    @Override
    public String toString() {
        return "Tablero{" +
                "nombre='" + nombre + '\'' +
                ", tamano=" + tamano +
                ", jugadores=" + jugadores.size() +
                ", casillas=" + casillas.size() +
                '}';
    }
}
