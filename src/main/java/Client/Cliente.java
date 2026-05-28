package Client;

import GUI.ClientFrame;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

import javax.print.attribute.standard.PageRanges;

import Models.Message;

public class Cliente {
    //Cliente/Player
    //TODO: Conectar al server/board, dados, piezas
    private final int PORT = 35501;
    private final String IP = "localhost";
    String nombre;
    Socket socket;
    private Player player;
    ClientFrame pantalla;
    ThreadCliente threadCliente;
    private ObjectOutputStream writerStream;
    

    public Cliente(ClientFrame pantalla) {
        this.pantalla = pantalla;
        this.player = new Player();
        this.connect();
    }
    
    private void connect(){
        try {
            this.socket = new Socket(IP, PORT);
            writerStream = new ObjectOutputStream(socket.getOutputStream());
            writerStream.flush();
            threadCliente = new ThreadCliente(this, socket);
            threadCliente.start();
            this.nombre = pantalla.obtenerNombre();
            //escribirMensaje(new Message("Name", this.nombre, "", this.nombre));
        } catch (IOException ex) {
            
        }
    }

    public void escribirMensaje(Message msg){
        try {
            writerStream.writeObject(msg);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public int[] realizarTirada(){
        try {
            int turnosPerdidos = 0;
            int dado1 = new Random().nextInt(6);
            int dado2 = new Random().nextInt(6);
            if (dado1 == 5){
                turnosPerdidos+=1;
                dado1=0;
            }
            if (dado2 == 5){
                turnosPerdidos+=1;
                dado2=0;
            }
            player.setTurnosPendientes(player.getTurnosPendientes()+turnosPerdidos);
            int[] dados = {dado1, dado2};
            return dados;

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
            int[] dados = {-1, -1};
            return dados;
        }
    }

    public void enviarMensaje(Message msg){
        try {
            writerStream.writeObject(msg);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
}
