package Models.Tablero;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import Models.Tablero.Casilla.TipoComodin;
import Models.Tablero.Casilla.TipoJuego;


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
            System.out.println("Casilla n: " + i + "Tipo: " + casillasTempo.get(i).getTipoComodin() + " " + casillasTempo.get(i).getTipoJuego());
        }
    }

    public int obtenerPosicionSiguienteTubo(Casilla casillaActual){
        //TipoComodin objetivo = null;
        int tuboSiguiente = casillaActual.getNumero();
        /*switch (casillaActual.getTipoComodin()) {
        case TUBO_1:
            objetivo = TipoComodin.TUBO_2;
            break;
        case TUBO_2:
            objetivo = TipoComodin.TUBO_3;
            break;
        case TUBO_3:
            objetivo = TipoComodin.TUBO_1;
            break;
        default:
            break;
        }*/
        for (Casilla casilla : casillas){
            if (casilla.getTipo().equals("comodin") && casilla.getTipoComodin().getNombre().equals("Tubo")){
                if (casilla.getNumero() > casillaActual.getNumero()){
                    tuboSiguiente = casilla.getNumero();
                    break;
                } 
                else if (casilla.getNumero() < casillaActual.getNumero()){
                    tuboSiguiente = casilla.getNumero();
                }
            }
        }
        return tuboSiguiente + 1;
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
