package Models;

import java.awt.Color;
import java.io.Serializable;

public class Pieza implements Serializable{
    private Color color;
    private String figura;
    private String nombre;

    public Pieza(Color color, String figura){
        this.color = color;
        this.figura = figura;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFigura() {
        return figura;
    }

    public void setFigura(String figura) {
        this.figura = figura;
    }
    
}
