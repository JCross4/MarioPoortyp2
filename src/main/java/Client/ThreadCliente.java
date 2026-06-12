package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JLabel;

import Models.Message;
import Models.MessageBoard;
import Models.MessageComodin;
import Models.MessageNombres;
import Models.MessagePieza;
import Models.MessagePiezasDisponibles;
import Models.MessageGato;
import Models.MessageTurn;

public class ThreadCliente extends Thread{
    private Cliente cliente;
    private Socket socket; //el socket espejo del cliente recibido en el accept
    private ObjectInputStream readerStream;
    private boolean isRunning = true;

    public ThreadCliente(Cliente cliente, Socket socket) {
            this.cliente = cliente;
            this.socket = socket;
        try {
            // Luego ObjectInputStream
            readerStream = new ObjectInputStream(socket.getInputStream());
            
        } catch (IOException ex) {
            
        }
    }

    public void run (){
        Message mensajeRecibido;
        while(isRunning){
            //System.out.println("running");
            try {
                mensajeRecibido = (Message)readerStream.readObject();
                if (mensajeRecibido.tipo.equals( "Tirada") ){
                    cliente.pantalla.agregarMensaje(mensajeRecibido.emisor+ ": "+ mensajeRecibido.mensaje);
                } else if (mensajeRecibido.tipo.equals("mensajeChat")) {
                    cliente.pantalla.agregarMensajeChat(mensajeRecibido);
                } else if (mensajeRecibido.tipo.equals( "Name")){
                    ArrayList<String> nombresPlayers = cliente.getNombresOtrosPlayers();
                    nombresPlayers.add(mensajeRecibido.mensaje);
                    ArrayList<JLabel> labelsPlayers = cliente.getLabelsOtrosPlayers();
                    labelsPlayers.add(null);
                    cliente.setNombresOtrosPlayers(nombresPlayers);
                    cliente.setLabelsOtrosPlayers(labelsPlayers);
                    System.out.println("Agregado otro cliente " + mensajeRecibido.mensaje);
                } else if(mensajeRecibido instanceof MessageNombres){
                    MessageNombres nombres = (MessageNombres) mensajeRecibido;
                    cliente.setNombresOtrosPlayers(nombres.getNombresPlayers());
                    ArrayList<JLabel> labels = new ArrayList<>();
                    for (int i = 0; i<nombres.getNombresPlayers().size(); i++){
                        labels.add(null);
                    }
                    cliente.setLabelsOtrosPlayers(labels);
                    System.out.println("Recibidos nombres desde server");
                } else if (mensajeRecibido.tipo.equals( "board")){
                    MessageBoard boardRecibido = (MessageBoard) mensajeRecibido;
                    cliente.crearTecladoRecibido(boardRecibido);
                } else if (mensajeRecibido.tipo.equals( "piezas")){
                    MessagePiezasDisponibles piezasDisponiblesServer = (MessagePiezasDisponibles) mensajeRecibido;
                    cliente.setPiezasTotales(piezasDisponiblesServer.getPiezasDisponibles());
                } else if(mensajeRecibido instanceof MessagePieza){
                    MessagePieza mensajeMovimiento = (MessagePieza) mensajeRecibido;
                    cliente.dibujarPiezaRecibida(mensajeMovimiento.getNombre(), mensajeMovimiento.getPieza(), mensajeMovimiento.getNuevaPosicion());
                } else if(mensajeRecibido instanceof MessageGato){
                    MessageGato mensajeGato = (MessageGato) mensajeRecibido;
                    cliente.recibirMensajeGato(mensajeGato);
                } else if(mensajeRecibido instanceof MessageTurn){
                    MessageTurn turnMessage = (MessageTurn) mensajeRecibido;
                    cliente.setCurrentPlayer(turnMessage.getPlayerActual());
                    cliente.pantalla.agregarMensaje("Turno de: " + turnMessage.getPlayerActual() + "\n");
                } else if(mensajeRecibido instanceof MessageComodin){
                    MessageComodin comodin = (MessageComodin) mensajeRecibido;
                    switch (comodin.getTipoComodin()) {
                        case "Flor de fuego":
                            cliente.getPantalla().agregarMensaje("Ha sido atacado por una flor de fuego, vuelve a la primer casilla");
                            cliente.actualizarPosicion(-(cliente.getPlayer().getPosicion()-1));
                            cliente.moverPieza();
                            cliente.getPantalla().moverPiezaPropiaExistente(1, cliente.getPlayer().getPieza(), cliente.getNombre());
                            cliente.enviarMovimientoPieza(cliente.getPlayer());
                            break;
                        case "Flor de hielo":
                            cliente.getPantalla().agregarMensaje("Ha sido atacado por una flor de hielo, pierde dos turnos");
                            cliente.getPlayer().setTurnosPendientes(cliente.getPlayer().getTurnosPendientes()+2);
                            break;
                        default:
                            break;
                    }
                } else{
                    cliente.pantalla.agregarMensaje(mensajeRecibido.emisor+ ": "+ mensajeRecibido.mensaje);
                }
                //System.out.println("Out if: " );
                
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                isRunning = false;
            }
        }
    }
}