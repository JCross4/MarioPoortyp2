package Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

public class Board implements Serializable{
    //Atributos
    private ArrayList<Casilla> casillas;
    private int tamaño;

    public Board(){
        casillas = new ArrayList<>();
        tamaño = 26;
        inicializarTablero();
    }

    //Inicializar tablero
    private void inicializarTablero() {
        ArrayList<Casilla> casillasTempo = new ArrayList<>();
        int numero = 0;
        
        // Crear 9 juegos, 2 veces cada uno (18 casillas)
        Casilla.TipoJuego[] juegos = Casilla.TipoJuego.values();
        for (Casilla.TipoJuego juego : juegos) {
            casillasTempo.add(new Casilla(numero++, juego));
            casillasTempo.add(new Casilla(numero++, juego));
        }
        
        // Crear 8 comodines
        Casilla.TipoComodin[] comodines = Casilla.TipoComodin.values();
        for (Casilla.TipoComodin comodin : comodines) {
            casillasTempo.add(new Casilla(numero++, comodin));
        }
        
        // Mezclar aleatoriamente
        Collections.shuffle(casillasTempo);
        
        // Reasignar números en orden y agregar al tablero
        for (int i = 0; i < casillasTempo.size(); i++) {
            casillasTempo.get(i).setNumero(i);
            casillas.add(casillasTempo.get(i));
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
