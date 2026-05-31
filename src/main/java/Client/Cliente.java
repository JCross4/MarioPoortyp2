package Client;

import GUI.ClientFrame;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;


import Models.Board;
import Models.Message;
import Models.MessageBoard;
import Models.MessageRequest;
import Models.Pieza;

public class Cliente {
    //Cliente/Player
    //TODO: piezas
    private final int PORT = 35501;
    private final String IP = "localhost";
    String nombre;
    Socket socket;
    private Player player;
    private Pieza[] piezasDisponibles = new Pieza[10];
    ClientFrame pantalla;
    ThreadCliente threadCliente;
    private ObjectOutputStream writerStream;
    private Board tablero;
    

    public Cliente(ClientFrame pantalla) {
        this.pantalla = pantalla;
        this.player = new Player();
        this.connect();
        solicitarPiezasDisponiblesServer();
    }
    
    private void connect(){
        try {
            this.socket = new Socket(IP, PORT);
            writerStream = new ObjectOutputStream(socket.getOutputStream());
            writerStream.flush();
            threadCliente = new ThreadCliente(this, socket);
            threadCliente.start();
            this.nombre = pantalla.obtenerNombre();
            escribirMensaje(new Message("Name", this.nombre, "", this.nombre));
            
        } catch (IOException ex) {
            
        }
    }

    public void escribirMensaje(Message msg){
        try {
            writerStream.writeObject(msg);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void crearTecladoRecibido(MessageBoard messageTeclado){
        setTablero(messageTeclado.getTablero());
        getPantalla().dibujarTablero(getTablero());
    }

    public void solicitarPiezasDisponiblesServer(){
        Message request = new MessageRequest("request", "", "", "", "piezasDisponibles");
        escribirMensaje(request);
        System.out.println("Piezas solicitadas");
    }

    public String[] obtenerStringPiezasDisponibles(){
        String[] stringPiezasDisponibles = new String[piezasDisponibles.length];
        for (int i = 0; i<piezasDisponibles.length; i++){
            stringPiezasDisponibles[i] = piezasDisponibles[i].getNombre();
        }
        return stringPiezasDisponibles;
    }

    public Pieza obtenerPiezaDesdeString(String piezaString){
        Pieza piezaSeleccionada = null;
        for (int i = 0; i<piezasDisponibles.length; i++){
            if (piezasDisponibles[i].getNombre() == piezaString){
                piezaSeleccionada = piezasDisponibles[i];
            }
        }
        return piezaSeleccionada;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ClientFrame getPantalla() {
        return pantalla;
    }

    public void setPantalla(ClientFrame pantalla) {
        this.pantalla = pantalla;
    }

    public Board getTablero() {
        return tablero;
    }

    public void setTablero(Board tablero) {
        this.tablero = tablero;
    }

    public Pieza[] getPiezasDisponibles() {
        return piezasDisponibles;
    }

    public void setPiezasDisponibles(Pieza[] piezasDisponibles) {
        this.piezasDisponibles = piezasDisponibles;
    }
    
    
}
