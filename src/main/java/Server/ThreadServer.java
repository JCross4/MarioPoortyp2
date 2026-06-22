package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.annotation.processing.Messager;
import javax.swing.JLabel;

import Models.Messages.Message;
import Models.Messages.MessageComodin;
import Models.Messages.MessageGato;
import Models.Messages.MessageMarioCards;
import Models.Messages.MessageMemory;
import Models.Messages.MessageNombres;
import Models.Messages.MessagePieza;
import Models.Messages.MessagePiezasDisponibles;
import Models.Messages.MessageRequest;

public class ThreadServer extends Thread{
    private Server servidor;
    private Socket socket; //el socket espeejo del cliente recibido en el accept
    private ObjectInputStream readerStream;
    private ObjectOutputStream writerStream;
    private boolean isRunning = true;
    private String nombre;

       //1: servidor, 2: socket
    public ThreadServer(Server servidor, Socket socket) {
            this.servidor = servidor;
            this.socket = socket;
        try {
            // IMPORTANTE: primero ObjectOutputStream
            writerStream = new ObjectOutputStream(socket.getOutputStream());
            writerStream.flush();

            // Luego ObjectInputStream
            readerStream = new ObjectInputStream(socket.getInputStream());
            
        } catch (IOException ex) {
            //servidor.serverForm.escribirMensaje(ex.getMessage());
        }
    }
    
    
    public void run (){
        Message mensajeRecibido;
        while(isRunning){
            try {
                mensajeRecibido = (Message)readerStream.readObject();
                servidor.getPantalla().agregarMensaje("Msj de client: " +
                        mensajeRecibido + "\n" );
                if (mensajeRecibido.tipo.equals("Name")){
                    this.nombre = mensajeRecibido.mensaje;
                    //Enviarle al nuevo cliente el array de nombres existente, enviarle a los demás el nombre del nuevo cliente
                    MessageNombres nombres = new MessageNombres("nombres", "server", getNombre(), "nombres", servidor.getNombresPlayers());
                    servidor.sendPrivateMessage(nombres);

                    //Añadir el nuevo nombre a los nombres y labels del server
                    ArrayList<String> nombresPlayers = servidor.getNombresPlayers();
                    nombresPlayers.add(mensajeRecibido.mensaje);
                    ArrayList<JLabel> labelsPlayers = servidor.getLabelsPlayers();
                    labelsPlayers.add(null);
                    servidor.setNombresPlayers(nombresPlayers);
                    servidor.setLabelsPlayers(labelsPlayers);
                    
                    servidor.broadcastExcept(mensajeRecibido, this.nombre);
                } 
                else if(mensajeRecibido.tipo.equals("mensajeChat")){
                    servidor.broadcast(mensajeRecibido);

                } 
                else if (mensajeRecibido instanceof MessageRequest){
                    MessageRequest request = (MessageRequest) mensajeRecibido;
                    //System.out.println(request.getTipoRequest());
                    switch (request.getTipoRequest()) {
                        case "piezasDisponibles":
                            MessagePiezasDisponibles piezasDisponibles = new MessagePiezasDisponibles("piezas", "Server", this.nombre, "", servidor.getPiezasDisponibles());
                            servidor.sendPrivateMessage(piezasDisponibles);
                            System.out.println("Piezas enviadas a " + this.nombre);
                            break;
                    
                        default:
                            break;
                    }
                    writerStream.writeObject(new Message("NameConfirmation", "Servidor", nombre, "Tu TS recibió tu nombre"));
                }
                else if (mensajeRecibido instanceof MessagePiezasDisponibles){
                    MessagePiezasDisponibles piezasDisponibles = (MessagePiezasDisponibles) mensajeRecibido;
                    servidor.setPiezasDisponibles(piezasDisponibles.getPiezasDisponibles());
                    piezasDisponibles = new MessagePiezasDisponibles("piezas", "Server", this.nombre, "", servidor.getPiezasDisponibles());
                    servidor.broadcast(piezasDisponibles);
                    System.out.println("Piezas servidor actualizadas");
                }
                else if (mensajeRecibido.tipo.equals("broadcast")){
                    servidor.broadcast(mensajeRecibido);
                }
                else if (mensajeRecibido instanceof MessagePieza){
                    MessagePieza mensajePieza = (MessagePieza) mensajeRecibido;
                    //Enviar el movimiento a los otros clientes y dibujarlo en serverside
                    servidor.broadcastExcept(mensajePieza, nombre);
                    servidor.dibujarPieza(this.nombre, mensajePieza.getPieza(), mensajePieza.getNuevaPosicion());
                } 
                else if (mensajeRecibido instanceof MessageGato) {
                    servidor.sendPrivateMessage(mensajeRecibido);
                } 
                else if (mensajeRecibido instanceof MessageMemory) {
                    servidor.sendPrivateMessage(mensajeRecibido);
                } 
                else if (mensajeRecibido instanceof MessageMarioCards) {
                    servidor.broadcastExcept(mensajeRecibido, nombre);
                } 
                else if (mensajeRecibido instanceof MessageComodin){
                    servidor.sendPrivateMessage(mensajeRecibido);
                }
            } catch (Exception ex) {
                servidor.getPantalla().agregarMensaje(ex.getMessage());
                isRunning = false;
            }
    }
}


    public ObjectInputStream getReaderStream() {
        return readerStream;
    }


    public void setReaderStream(ObjectInputStream readerStream) {
        this.readerStream = readerStream;
    }


    public ObjectOutputStream getWriterStream() {
        return writerStream;
    }


    public void setWriterStream(ObjectOutputStream writerStream) {
        this.writerStream = writerStream;
    }


    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


}
