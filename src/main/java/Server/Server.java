package Server;

import GUI.ServerFrame;
import Models.Board;
import Models.Message;
import Models.MessageTurn;
import Models.Pieza;

import java.awt.Color;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import javax.swing.JLabel;

public class Server {
    private final int PORT = 35501;
    private ServerSocket serverSocket;
    private ServerFrame pantalla;
    private ThreadConexiones threadConexiones;
    private Board tablero;
    private Pieza[] piezasDisponibles = new Pieza[10];
    private Color[] coloresDisponibles = {Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.WHITE};
    private String[] nombresColores = {"Negro", "Azul", "Cian", "Gris oscuro", "Verde", "Magenta", "Naranja", "Rosado", "Rojo", "Blanco"};
    private ArrayList<JLabel> labelsPlayers = new ArrayList<>();
    private ArrayList<String> nombresPlayers = new ArrayList<>();
    private int currentTurnIndex = 0;
    private int turnNumber = 0;

    //arrayList de clientes conectados: ThreadServidor
    ArrayList<ThreadServer> connectClientsThreads = new ArrayList<ThreadServer>();
    
    public Server(ServerFrame pantalla){
        this.pantalla = pantalla;
        this.connect();
        this.threadConexiones =  new ThreadConexiones(this);
        inicializarPiezas();
    }
    
    private void connect(){
        try {
            serverSocket = new ServerSocket(PORT);
            getPantalla().agregarMensaje("Servidor escuchando puerto: " + PORT);
        } catch (IOException ex) {
            getPantalla().agregarMensaje("Error levantando el server: "+ex.getMessage());
        }
    }

    public void broadcast(Message msg){
        
        for (ThreadServer client : connectClientsThreads) {
            try {
                client.getWriterStream().writeObject(msg);
            } catch (IOException ex) {
                pantalla.agregarMensaje("Error levantando el server: "+ex.getMessage());
            }
        }
    }

    public void broadcastExcept(Message msg, String nombre){
        for (ThreadServer client : connectClientsThreads) {
            try {
                if (!client.getNombre().equals(nombre)){
                    client.getWriterStream().writeObject(msg);
                }
            } catch (IOException ex) {
                pantalla.agregarMensaje("Error levantando el server: "+ex.getMessage());
            }
        }
    }

    public void sendPrivateMessage(Message msg){
        for (ThreadServer client : connectClientsThreads) {
            try {
                if (msg.receptor.equals(client.getNombre())){
                    client.getWriterStream().writeObject(msg);
                    break;
                }
            } catch (IOException ex) {
                pantalla.agregarMensaje("Error levantando el server: "+ex.getMessage());
            }
        }
    }

    public void inicializarPiezas(){
        for (int i = 0; i<10; i++){
            Pieza nuevaPieza = new Pieza(coloresDisponibles[i], "Pieza");
            nuevaPieza.setNombre(nuevaPieza.getFigura() + " Color " + nombresColores[i]);
            piezasDisponibles[i] = nuevaPieza;
        }
        System.out.println("Piezas inicializadas server");
    }

    public void dibujarPieza(String nombre, Pieza pieza, int posicion){
        JLabel label = obtenerLabel(nombre);
        if (label == null){
            getPantalla().dibujarPieza(posicion, pieza, nombre);
            System.out.println("Primera vez, label no existente, crear y agregar al array");
        }
        else{
            getPantalla().moverPiezaExistente(posicion, pieza, nombre);
            System.out.println("Label sí existe, solo mover");
        }
    }

    public JLabel obtenerLabel(String nombreRecibido){
        JLabel label = null;
        for (String nombre : nombresPlayers){
            if (nombre.equals(nombreRecibido)){
                try {
                    int indice = nombresPlayers.indexOf(nombre);
                    label = labelsPlayers.get(indice);
                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println(e.getLocalizedMessage());
                }
                
            }
        }
        return label;
    }

    public void inicializarTurno() {
        currentTurnIndex = 0;
        turnNumber = 0;
        if (!nombresPlayers.isEmpty()) {
            notifyCurrentPlayer();
        }
    }
    
    public void siguienteTurno() {
        if (nombresPlayers.isEmpty()) {
            return;
        }
        currentTurnIndex = (currentTurnIndex + 1) % nombresPlayers.size();
        turnNumber++;
        notifyCurrentPlayer();
    }
    
    public String getCurrentPlayer() {
        if (nombresPlayers.isEmpty()) {
            return "";
        }
        return nombresPlayers.get(currentTurnIndex);
    }
    
    private void notifyCurrentPlayer() {
        String currentPlayer = getCurrentPlayer();
        MessageTurn turnMessage = new MessageTurn("turn", "server", "all", "Turno de " + currentPlayer, currentPlayer, turnNumber);
        broadcast(turnMessage);
        getPantalla().agregarMensaje("Turno " + turnNumber + ": " + currentPlayer + "\n");
    }

    public ServerFrame getPantalla() {
        return pantalla;
    }

    public void setPantalla(ServerFrame pantalla) {
        this.pantalla = pantalla;
    }

    public ThreadConexiones getThreadConexiones() {
        return threadConexiones;
    }

    public void setThreadConexiones(ThreadConexiones threadConexiones) {
        this.threadConexiones = threadConexiones;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
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

    public ArrayList<JLabel> getLabelsPlayers() {
        return labelsPlayers;
    }

    public void setLabelsPlayers(ArrayList<JLabel> labelsPlayers) {
        this.labelsPlayers = labelsPlayers;
    }

    public ArrayList<String> getNombresPlayers() {
        return nombresPlayers;
    }

    public void setNombresPlayers(ArrayList<String> nombresPlayers) {
        this.nombresPlayers = nombresPlayers;
    }
    
    public int getCurrentTurnIndex() {
        return currentTurnIndex;
    }
    
    public int getTurnNumber() {
        return turnNumber;
    }
    
}
