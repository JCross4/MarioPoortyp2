package Server;

import GUI.ServerFrame;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {
    private final int PORT = 35501;
    ServerSocket serverSocket;
    ServerFrame pantalla;
    ThreadConexiones threadConexiones;
    //arrayList de clientes conectados: ThreadServidor
    ArrayList<ThreadServer> connectClientsThreads = new ArrayList<ThreadServer>();
    
    public Server(ServerFrame pantalla){
        this.pantalla = pantalla;
        this.connect();
        this.threadConexiones =  new ThreadConexiones(this);
    }
    
    private void connect(){
        try {
            serverSocket = new ServerSocket(PORT);
            getPantalla().agregarMensaje("Servidor escuchando puerto: " + PORT);
        } catch (IOException ex) {
            getPantalla().agregarMensaje("Error levantando el server: "+ex.getMessage());
        }
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
    
    
}
