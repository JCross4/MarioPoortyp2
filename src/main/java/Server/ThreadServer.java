package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.annotation.processing.Messager;

import Models.Message;
import Models.MessagePiezasDisponibles;
import Models.MessageRequest;

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
                } else if (mensajeRecibido instanceof MessageRequest){
                    MessageRequest request = (MessageRequest) mensajeRecibido;
                    System.out.println(request.getTipoRequest());
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
                }else if (mensajeRecibido instanceof MessagePiezasDisponibles){
                    MessagePiezasDisponibles piezasDisponibles = (MessagePiezasDisponibles) mensajeRecibido;
                    servidor.setPiezasDisponibles(piezasDisponibles.getPiezasDisponibles());
                    piezasDisponibles = new MessagePiezasDisponibles("piezas", "Server", this.nombre, "", servidor.getPiezasDisponibles());
                    servidor.broadcast(piezasDisponibles);
                    System.out.println("Piezas servidor actualizadas");
                }else if (mensajeRecibido.tipo.equals("broadcast")){
                    servidor.broadcast(mensajeRecibido);
                }/*else if (mensajeRecibido.tipo.equals("Disparo")){
                    servidor.sendPrivateMessage(mensajeRecibido);
                }*/
                
                //TODO: procesar el mensaje recibido
                // if tipo == ... then haga x
                //broadcast: repartir el mensaje a todos
                //mensaje individual
                
            } catch (Exception ex) {
                //servidor.serverForm.escribirMensaje(ex.getMessage());
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
