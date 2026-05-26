package Main;

import java.io.Serializable;

/**
 * Casilla (Space/Cell) represents a single space on the game board
 */
public class Casilla implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String nombre;
    private String tipo; // normal, especial, trampa, premio
    private int valor; // monetary value or effect
    private Jugador jugadorActual;
    
    public Casilla() {
    }
    
    public Casilla(int id, String nombre, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = 0;
        this.jugadorActual = null;
    }
    
    public Casilla(int id, String nombre, String tipo, int valor) {
        this(id, nombre, tipo);
        this.valor = valor;
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
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public int getValor() {
        return valor;
    }
    
    public void setValor(int valor) {
        this.valor = valor;
    }
    
    public Jugador getJugadorActual() {
        return jugadorActual;
    }
    
    public void setJugadorActual(Jugador jugador) {
        this.jugadorActual = jugador;
    }
    
    public boolean estaOcupada() {
        return jugadorActual != null;
    }
    
    @Override
    public String toString() {
        return "Casilla{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", valor=" + valor +
                '}';
    }
}
