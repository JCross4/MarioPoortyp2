package Models;

import java.util.ArrayList;

public class MessageNombres extends Message{

    private ArrayList<String> nombresPlayers;

    public MessageNombres(String tipo, String emisor, String receptor, String mensaje, ArrayList<String> nombres) {
        super(tipo, emisor, receptor, mensaje);
        setNombresPlayers(nombres);
        //TODO Auto-generated constructor stub
    }

    public ArrayList<String> getNombresPlayers() {
        return nombresPlayers;
    }

    public void setNombresPlayers(ArrayList<String> nombresPlayers) {
        this.nombresPlayers = nombresPlayers;
    }

    



}
