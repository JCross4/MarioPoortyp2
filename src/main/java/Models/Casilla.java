package Models;
import java.io.Serializable;

public class Casilla implements Serializable{
    
    // Enum para tipos de juegos
    public enum TipoJuego {
        GATO("Gato"),
        SOPA_LETRAS("Sopa de letras"),
        MEMORY_PATH("Memory path"),
        SUPER_BROS_MEMORY("Super Bro's Memory"),
        CATCH_THE_CAT("Catch the Cat"),
        BOMBER_MARIO("Bomber Mario"),
        GUESS_THE_WHO("Guess the Who"),
        COLLECT_THE_COINS("Collect the Coins"),
        MARIO_CARDS("Mario cards");
        
        private String nombre;
        
        TipoJuego(String nombre) {
            this.nombre = nombre;
        }
        
        public String getNombre() {
            return nombre;
        }
    }
    
    // Enum para comodines
    public enum TipoComodin {
        CARCEL("Carcel"),
        TUBO_1("Tubo"),
        TUBO_2("Tubo"),
        TUBO_3("Tubo"),
        ESTRELLA("Estrella"),
        FLOR_FUEGO("Flor de fuego"),
        FLOR_HIELO("Flor de hielo"),
        COLA("Cola");
        
        private String nombre;
        
        TipoComodin(String nombre) {
            this.nombre = nombre;
        }
        
        public String getNombre() {
            return nombre;
        }
    }
    
    //Atributos
    private int numero;
    private String tipo; // "juego", "comodin" o "normal"
    private TipoJuego tipoJuego;
    private TipoComodin tipoComodin;
    private int valor;

    // Constructor para casillas normales o juegos
    public Casilla(int numero, String tipo, int valor){
        this.numero = numero;
        this.tipo = tipo;
        this.valor = valor;
    }
    
    // Constructor para casillas de juego
    public Casilla(int numero, TipoJuego tipoJuego) {
        this.numero = numero;
        this.tipo = "juego";
        this.tipoJuego = tipoJuego;
        this.valor = 0;
    }
    
    // Constructor para casillas de comodin
    public Casilla(int numero, TipoComodin tipoComodin) {
        this.numero = numero;
        this.tipo = "comodin";
        this.tipoComodin = tipoComodin;
        this.valor = 0;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
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
    
    public TipoJuego getTipoJuego() {
        return tipoJuego;
    }
    
    public void setTipoJuego(TipoJuego tipoJuego) {
        this.tipoJuego = tipoJuego;
    }
    
    public TipoComodin getTipoComodin() {
        return tipoComodin;
    }
    
    public void setTipoComodin(TipoComodin tipoComodin) {
        this.tipoComodin = tipoComodin;
    }
}
