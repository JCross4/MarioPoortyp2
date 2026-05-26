package Main;

import java.io.Serializable;

/**
 * Jugador (Player) represents a player in the game
 */
public class Jugador implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String nombre;
    private int dinero;
    private Casilla casillaActual;
    private int posicionTablero; // position index on board
    private boolean activo;
    
    public Jugador() {
    }
    
    public Jugador(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.dinero = 1000; // initial money
        this.posicionTablero = 0;
        this.activo = true;
        this.casillaActual = null;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public int getDinero() {
        return dinero;
    }
    
    public void setDinero(int dinero) {
        this.dinero = dinero;
    }
    
    public void agregarDinero(int cantidad) {
        this.dinero += cantidad;
    }
    
    public void restarDinero(int cantidad) {
        this.dinero -= cantidad;
    }
    
    public Casilla getCasillaActual() {
        return casillaActual;
    }
    
    public void setCasillaActual(Casilla casilla) {
        this.casillaActual = casilla;
    }
    
    public int getPosicionTablero() {
        return posicionTablero;
    }
    
    public void setPosicionTablero(int posicion) {
        this.posicionTablero = posicion;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public void mover(int pasos, Tablero tablero) {
        int nuevaPosicion = (posicionTablero + pasos) % tablero.getCasillas().size();
        setPosicionTablero(nuevaPosicion);
        setCasillaActual(tablero.getCasilla(nuevaPosicion));
    }
    
    @Override
    public String toString() {
        return "Jugador{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", dinero=" + dinero +
                ", posicion=" + posicionTablero +
                ", activo=" + activo +
                '}';
    }
}
