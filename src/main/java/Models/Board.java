package Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable{
    //Atributos
    //TODO: Crear tablero y dibujarlo en server y en cliente

    private ArrayList<Casilla> casillas;
    private int tamaño;

    public Board(){
        casillas = new ArrayList<>();
        tamaño = 40;
        inicializarTablero();
    }

    //Inicializar tablero
    private void inicializarTablero() {
        for (int i = 0; i < tamaño; i++) {
            String tipo = "normal";
            int valor = 0;
            
            // Define special spaces temp
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
            
            Casilla casilla = new Casilla(i, tipo, valor);
            casillas.add(casilla);
        }
    }

    public ArrayList<Casilla> getCasillas() {
        return casillas;
    }

    public void setCasillas(ArrayList<Casilla> casillas) {
        this.casillas = casillas;
    }

    public int getTamaño() {
        return tamaño;
    }

    public void setTamaño(int tamaño) {
        this.tamaño = tamaño;
    }


}
