package Models.Tablero;

import java.awt.Color;
import java.io.Serializable;

import javax.swing.JLabel;

public class Pieza implements Serializable{
    private Color color;
    private String figura;
    private String nombre;
    private boolean disponible;
    private JLabel label;


    public Pieza(Color color, String figura){
        this.color = color;
        this.figura = figura;
        this.disponible = true;
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

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public JLabel getLabel() {
        return label;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
}
