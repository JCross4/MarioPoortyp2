package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Models.Message;

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
                /*if (mensajeRecibido.tipo.equals("Name")){
                    this.nombre = mensajeRecibido.mensaje;
                    writerStream.writeObject(new Message("NameConfirmation", "Servidor", nombre, "Tu TS recibió tu nombre"));
                }else if (mensajeRecibido.tipo.equals("Broadcast")){
                    servidor.broadcast(mensajeRecibido);
                }else if (mensajeRecibido.tipo.equals("Private")){
                    servidor.sendPrivateMessage(mensajeRecibido);
                }else if (mensajeRecibido.tipo.equals("Disparo")){
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
}
