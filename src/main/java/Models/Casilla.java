package Models;
import java.io.Serializable;

public class Casilla implements Serializable{
    private int numero;
    private String tipo;
    private int valor;

    public Casilla(int numero, String tipo, int valor){
        this.numero = numero;
        this.tipo = tipo;
        this.valor = valor;
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


    
}
