package Server;

import java.io.IOException;
import java.net.Socket;

public class ThreadConexiones extends Thread{
    private Server servidor;
    private boolean isRunning = true;
    private int counter = 0;

    public ThreadConexiones(Server servidor) {
        this.servidor = servidor;
    }
    
    
    
    public void run(){
        servidor.getPantalla().agregarMensaje("Ahora escuchando conexiones \n" );
        while (isRunning){
            try {
                //se queda esperando que alguien en la red se conecte a la IP y port
                //cuando alguien se conecta obtiene el socket y crea la conexion
                //en el server con el cliente
                Socket socketCliente = servidor.getServerSocket().accept();
                servidor.getPantalla().agregarMensaje("Conexión recibida: #" + (++counter) + "\n");
                //crear todo el threadServidor
                ThreadServer newClient = new ThreadServer(servidor, socketCliente);
                servidor.connectClientsThreads.add(newClient);
                newClient.start();
                servidor.getPantalla().agregarMensaje("Cliente agregado al arreglo\n");
                servidor.getPantalla().actualizarLabelClientes(servidor.connectClientsThreads.size() + "");
            } catch (IOException ex) {
                servidor.getPantalla().agregarMensaje("Error en accept: " + ex.getMessage() + "\n");
            }
            
        }
    }

}
