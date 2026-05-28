package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import Models.Message;

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
                }else{
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