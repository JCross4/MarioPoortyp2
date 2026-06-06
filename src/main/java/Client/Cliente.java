package Client;

import GUI.ClientFrame;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.JLabel;

import Models.Board;
import Models.Casilla;
import Models.Message;
import Models.MessageBoard;
import Models.MessagePieza;
import Models.MessagePiezasDisponibles;
import Models.MessageRequest;
import Models.Pieza;

public class Cliente {
    //Cliente/Player
    //TODO: piezas, dibujar piezas,-- mover piezas, casillas especiales
    private final int PORT = 35501;
    private final String IP = "localhost";
    private String nombre;
    private Socket socket;
    private Player player;
    private Pieza[] piezasTotales = new Pieza[10];
    ClientFrame pantalla;
    ThreadCliente threadCliente;
    private ObjectOutputStream writerStream;
    private Board tablero;
    private JLabel labelPiezaPlayer;
    private ArrayList<JLabel> labelsOtrosPlayers = new ArrayList<>();
    private ArrayList<String> nombresOtrosPlayers = new ArrayList<>();
    private boolean turnoActual = false;
    private String currentPlayer = "";
    

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
            //solicitarPiezasDisponiblesServer();
        } catch (IOException ex) {
            
        }
    }

    public void enviarMensajeATodos(String texto){
        Message mensaje = new Message("broadcast", getNombre(), "todos", texto);
        escribirMensaje(mensaje);
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

    public void enviarPiezasDisponiblesActualizadas(){
        MessagePiezasDisponibles piezasActualizadas = new MessagePiezasDisponibles("piezas", this.nombre, "server", "piezas", piezasTotales);
        escribirMensaje(piezasActualizadas);
    }

    public void enviarMovimientoPieza(int nuevaPosicion, Pieza pieza){
        MessagePieza piezaMovimiento = new MessagePieza(this.getNombre(),"todos", nuevaPosicion, pieza, getNombre());
        escribirMensaje(piezaMovimiento);
    }

    public void habilitarPiezaSeleccionada(){
        for (int i = 0; i<piezasTotales.length; i++){
            if (piezasTotales[i].getNombre().equals(player.getPieza().getNombre())){
                setPiezasTotales(habilitarPieza(i));
                break;
            }
        }
    }

    public void deshabilitarPiezaSeleccionada(){
        for (int i = 0; i<piezasTotales.length; i++){
            if (piezasTotales[i].getNombre() == player.getPieza().getNombre()){
                setPiezasTotales(eliminarPieza(i));
                break;
            }
        }
    }

    public String[] obtenerStringPiezasDisponibles(){
        String[] stringPiezasDisponibles = new String[cantidadPiezasDisponibles()];
        Pieza[] piezasDisponibles = obtenerPiezasDisponibles();
        for (int i = 0; i<cantidadPiezasDisponibles(); i++){
            stringPiezasDisponibles[i] = piezasDisponibles[i].getNombre();
        }
        return stringPiezasDisponibles;
    }

    public int cantidadPiezasDisponibles(){
        int cantidad = 0;
        for (int i = 0; i<piezasTotales.length; i++){
            if (piezasTotales[i].isDisponible()){
                cantidad+=1;
            }
        }
        return cantidad;
    }

    public Pieza[] obtenerPiezasDisponibles(){
        ArrayList<Pieza> piezasDisponibles = new ArrayList<>();
        for (int i = 0; i<piezasTotales.length; i++){
            if (piezasTotales[i].isDisponible()){
                piezasDisponibles.add(piezasTotales[i]);
            }
        }
        return piezasDisponibles.toArray(new Pieza[0]);
    } 

    public Pieza[] habilitarPieza(int indice){
        Pieza[] piezasActualizado = piezasTotales;
        for (int i = 0; i<piezasTotales.length; i++){
            if (i == indice){
                piezasActualizado[i].setDisponible(true);
                break;
            }
        }
        return piezasActualizado;
    }

    public Pieza[] eliminarPieza(int indice){
        Pieza[] piezasActualizado = piezasTotales;
        for (int i = 0; i<piezasTotales.length; i++){
            if (i == indice){
                piezasActualizado[i].setDisponible(false);
            }
        }
        return piezasActualizado;
    }

    public Pieza obtenerPiezaDesdeString(String piezaString){
        Pieza piezaSeleccionada = null;
        for (int i = 0; i<piezasTotales.length; i++){
            if (piezasTotales[i].getNombre() == piezaString){
                piezaSeleccionada = piezasTotales[i];
            }
        }
        return piezaSeleccionada;
    }


    //Player
    public void realizarLanzamientoDeDados(){
        int[] resultTirada = getPlayer().realizarTirada();
        String tiradaString = getPlayer().obtenerStringTirada(resultTirada);
        int tiradaInt = getPlayer().obtenerMovimientosTirada(resultTirada);
        moverPieza(tiradaInt);
        Message m = new Message("Broadcast", getNombre(), "", "La tirada resulta en: " + tiradaString);
        escribirMensaje(m);
        //Enviar movimiento al server ?
        MessagePieza movimiento = new MessagePieza(this.nombre, "todos", getPlayer().getPosicion(), getPlayer().getPieza(), getNombre());
        System.out.println("El jugador " + this.nombre + "envia su lanzamiento con nombre " + getNombre());
        escribirMensaje(movimiento);
    }

    public void moverPieza(int movimiento){
        if (getPlayer().getPosicion()+movimiento>=getTablero().getTamaño()){
            getPlayer().setPosicion(getTablero().getTamaño());
        }
        else{
            getPlayer().setPosicion(getPlayer().getPosicion()+movimiento);
        }
    }

    public void dibujarPiezaRecibida(String nombre, Pieza pieza, int posicion){
        JLabel label = obtenerLabel(nombre);
        if (label == null){
            getPantalla().dibujarPiezaOtroCliente(posicion, pieza, nombre);
            System.out.println("Primera vez, label no existente, crear y agregar al array");
        }
        else{
            getPantalla().moverPiezaExistenteOtroCliente(posicion, pieza, nombre);
            System.out.println("Label sí existe, solo mover");
        }
    }

    public JLabel obtenerLabel(String nombreRecibido){
        JLabel label = null;
        for (String nombre : nombresOtrosPlayers){
            if (nombre.equals(nombreRecibido)){
                try {
                    int indice = nombresOtrosPlayers.indexOf(nombre);
                    label = labelsOtrosPlayers.get(indice);
                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println(e.getLocalizedMessage());
                }
            }
        }
        return label;
    }

    public void checkCasillaActual(){
        Casilla casillaActual = getTablero().getCasillas().get(getPlayer().getPosicion()-1);
        System.out.println("El tipo de la casilla actual es: " + casillaActual.getTipo());
        switch (casillaActual.getTipo().toLowerCase()) {
            case "juego":
                System.out.println("El tipo del juego es: " + casillaActual.getTipoJuego().getNombre());
                break;
            case "comodin":
                System.out.println("El tipo del comodin es: " + casillaActual.getTipoComodin().getNombre());
                break;
            default:
                System.out.println("Ningun tipo");
                break;
        }
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

    public Pieza[] getPiezasTotales() {
        return piezasTotales;
    }

    public void setPiezasTotales(Pieza[] piezasTotales) {
        this.piezasTotales = piezasTotales;
    }

    public ArrayList<JLabel> getLabelsOtrosPlayers() {
        return labelsOtrosPlayers;
    }

    public void setLabelsOtrosPlayers(ArrayList<JLabel> labelsOtrosPlayers) {
        this.labelsOtrosPlayers = labelsOtrosPlayers;
    }

    public ArrayList<String> getNombresOtrosPlayers() {
        return nombresOtrosPlayers;
    }

    public void setNombresOtrosPlayers(ArrayList<String> nombresOtrosPlayers) {
        this.nombresOtrosPlayers = nombresOtrosPlayers;
    }
    
    public boolean isTurnoActual() {
        return turnoActual;
    }

    public void setTurnoActual(boolean turnoActual) {
        this.turnoActual = turnoActual;
        pantalla.actualizarEstadoBotonTirada(turnoActual);
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
        if (currentPlayer.equals(getNombre())) {
            setTurnoActual(true);
        } else {
            setTurnoActual(false);
        }
    }
    
    
}
