package Models.Juegos;

import java.io.Serializable;
import java.util.Arrays;

import javax.swing.JFrame;

import Client.Cliente;
import GUI.GatoPantalla;
import Models.Messages.MessageGato;

public class GatoJuego implements Juego, Serializable{
    private String jugador1;
    private String jugador2;

    private final String symbol1;
    private final String symbol2;
    public char[] board = new char[9];

    

    public GatoJuego(String jugador1, String jugador2, String symbol) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.symbol1 = symbol;
        if (symbol.equals("X")){
            this.symbol2 = "O";
        }
        else{
            this.symbol2 = "X";
        }
        Arrays.fill(board, ' ');
        }



    @Override
    public void iniciar() {
        // TODO Auto-generated method stub
        if (jugador1 == null || jugador1.isEmpty()) {
            return;
        }
        MessageGato inicioGato = new MessageGato(
                "Gato",
                jugador1,
                jugador2,
                "Inicio del juego Gato",
                this,
                "start",
                -1,
                "O",
                "         ",
                "started"
        );
        throw new UnsupportedOperationException("Unimplemented method 'iniciar'");
    }

    public String getOponente(String jugador){
        if (jugador.equals(jugador1)){
            return jugador2;
        }
        else{
            return jugador1;
        }
    }


    public String getJugador1() {
        return jugador1;
    }



    public void setJugador1(String jugador1) {
        this.jugador1 = jugador1;
    }



    public String getJugador2() {
        return jugador2;
    }



    public void setJugador2(String jugador2) {
        this.jugador2 = jugador2;
    }

    public char[] getBoard() {
        return board;
    }



    public void setBoard(char[] board) {
        this.board = board;
    }



    @Override
    public void establecerGanador(boolean victoria) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'establecerGanador'");
    }



    
}
