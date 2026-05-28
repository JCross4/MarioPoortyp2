package Client;

import java.util.Random;

public class Player {
    private int posicion;
    private String color;
    private int turnosPendientes;

    public Player(){
        posicion = 0;
        color = "";
        turnosPendientes = 0;
    }

    public int tirarDado(){
        return new Random().nextInt(6);
    }


    public int getTurnosPendientes() {
        return turnosPendientes;
    }


    public void setTurnosPendientes(int turnosPendientes) {
        this.turnosPendientes = turnosPendientes;
    }


    
}
