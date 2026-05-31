package Client;

import java.util.Random;

import Models.Pieza;

public class Player {
    private int posicion;
    private Pieza pieza;
    private int turnosPendientes;

    public Player(){
        posicion = 0;
        turnosPendientes = 0;
    }

    public int tirarDado(){
        return new Random().nextInt(6) + 1;
    }

    public int[] realizarTirada(){
        try {
            int turnosPerdidos = 0;
            int dado1 = tirarDado();
            int dado2 = tirarDado();
            int dados[] = {dado1,dado2};
            for (int i : dados){
                if (i == 6){
                    turnosPerdidos+=1;
                }
            }
            setTurnosPendientes(getTurnosPendientes()+turnosPerdidos);
            return dados;

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
            int[] dados = {-1, -1};
            return dados;
        }
    }

    public String obtenerStringTirada(int[] tiradaDados){
        String tiradaString = "";
        for (int i = 0; i<2; i++){
            if (tiradaDados[i] == 6){
                tiradaString += "Dado " + (i + 1) + ": " + "Castigo! ";
            }
            else{
                tiradaString += "Dado " + (i + 1) + ": " + tiradaDados[i] + " ";
            }
        }
        return tiradaString;
    }

    public int getTurnosPendientes() {
        return turnosPendientes;
    }


    public void setTurnosPendientes(int turnosPendientes) {
        this.turnosPendientes = turnosPendientes;
    }

    public Pieza getPieza() {
        return pieza;
    }

    public void setPieza(Pieza pieza) {
        this.pieza = pieza;
    }

    


    
}
