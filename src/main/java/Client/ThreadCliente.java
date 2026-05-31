package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import Models.Message;
import Models.MessageBoard;
import Models.MessagePiezasDisponibles;

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
                } else if (mensajeRecibido.tipo.equals( "board")){
                    MessageBoard boardRecibido = (MessageBoard) mensajeRecibido;
                    cliente.crearTecladoRecibido(boardRecibido);
                } else if (mensajeRecibido.tipo.equals( "piezas")){
                    MessagePiezasDisponibles piezasDisponiblesServer = (MessagePiezasDisponibles) mensajeRecibido;
                    cliente.setPiezasDisponibles(piezasDisponiblesServer.getPiezasDisponibles());
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