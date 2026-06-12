package Models;

import javax.swing.JFrame;

import GUI.GatoPantalla;

public class GatoJuego implements Juego{
    private String jugador1;
    private String jugador2;

    

    public GatoJuego(String jugador1, String jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
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



    
}
