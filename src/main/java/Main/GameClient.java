package Main;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * GameClient connects to the game server and handles player interactions
 */
public class GameClient {
    private String host;
    private int puerto;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Jugador miJugador;
    private Tablero tableroLocal;
    private boolean conectado;
    private RecibidorMensajes recibidor;
    
    public GameClient(String host, int puerto, String nombreJugador) {
        this.host = host;
        this.puerto = puerto;
        this.miJugador = new Jugador(0, nombreJugador);
        this.conectado = false;
    }
    
    /**
     * Connect to the game server
     */
    public boolean conectar() {
        try {
            socket = new Socket(host, puerto);
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(socket.getInputStream());
            conectado = true;
            
            System.out.println("Conectado al servidor en " + host + ":" + puerto);
            
            // Send connection message
            GameMessage mensaje = new GameMessage(GameMessage.MessageType.CONNECT, miJugador);
            enviarMensaje(mensaje);
            
            // Start message receiver thread
            recibidor = new RecibidorMensajes(this);
            recibidor.start();
            
            return true;
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
            conectado = false;
            return false;
        }
    }
    
    /**
     * Disconnect from server
     */
    public void desconectar() {
        try {
            if (conectado) {
                GameMessage mensaje = new GameMessage(GameMessage.MessageType.DISCONNECT, "Desconectando");
                enviarMensaje(mensaje);
            }
            
            conectado = false;
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println("Desconectado del servidor");
        } catch (IOException e) {
            System.err.println("Error disconnecting: " + e.getMessage());
        }
    }
    
    /**
     * Roll dice and move
     */
    public void tirarDado() {
        if (conectado) {
            int tirada = (int) (Math.random() * 6) + 1;
            
            GameMessage mensaje = new GameMessage(GameMessage.MessageType.DICE_ROLL, "Tirada: " + tirada);
            mensaje.setPlayerId(miJugador.getId());
            mensaje.setDiceValue(tirada);
            
            enviarMensaje(mensaje);
            System.out.println("Tirada enviada: " + tirada);
        }
    }
    
    /**
     * Send a message to the server
     */
    public synchronized void enviarMensaje(GameMessage mensaje) {
        try {
            output.writeObject(mensaje);
            output.flush();
        } catch (IOException e) {
            System.err.println("Error sending message: " + e.getMessage());
            desconectar();
        }
    }
    
    /**
     * Receive and process messages from server
     */
    public void procesarMensaje(GameMessage mensaje) {
        switch (mensaje.getTipo()) {
            case ACKNOWLEDGEMENT:
                System.out.println("[SERVIDOR] " + mensaje.getContenido());
                break;
            case BOARD_UPDATE:
                tableroLocal = mensaje.getTablero();
                mostrarTablero();
                break;
            case PLAYER_JOIN:
                System.out.println("[SERVIDOR] " + mensaje.getContenido());
                break;
            case PLAYER_LEFT:
                System.out.println("[SERVIDOR] " + mensaje.getContenido());
                break;
            case DICE_ROLL:
                System.out.println("[JUEGO] " + mensaje.getContenido());
                break;
            case PLAYER_MOVED:
                System.out.println("[MOVIMIENTO] " + mensaje.getContenido());
                break;
            default:
                System.out.println("[MENSAJE] " + mensaje.getContenido());
        }
    }
    
    /**
     * Display current board state
     */
    private void mostrarTablero() {
        if (tableroLocal != null) {
            System.out.println("\n=== ESTADO DEL TABLERO ===");
            System.out.println("Tablero: " + tableroLocal.getNombre());
            System.out.println("Jugadores conectados: " + tableroLocal.getNumJugadores());
            System.out.println();
            
            for (Jugador j : tableroLocal.getJugadores()) {
                Casilla casilla = j.getCasillaActual();
                String nombreCasilla = casilla != null ? casilla.getNombre() : "Desconocida";
                System.out.printf("- %s: $%d (Casilla: %s)\n", 
                    j.getNombre(), j.getDinero(), nombreCasilla);
            }
            System.out.println("===========================\n");
        }
    }
    
    /**
     * Display menu and handle user input
     */
    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;
        
        System.out.println("\n=== MARIO POORTY - CLIENTE ===");
        System.out.println("Bienvenido " + miJugador.getNombre());
        
        while (!salir && conectado) {
            System.out.println("\n--- MENÚ ---");
            System.out.println("1. Tirar dado");
            System.out.println("2. Ver tablero");
            System.out.println("3. Ver mi información");
            System.out.println("4. Desconectar");
            System.out.print("Selecciona una opción: ");
            
            String opcion = scanner.nextLine();
            
            switch (opcion) {
                case "1":
                    tirarDado();
                    break;
                case "2":
                    mostrarTablero();
                    break;
                case "3":
                    System.out.println(miJugador);
                    break;
                case "4":
                    salir = true;
                    desconectar();
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
        
        scanner.close();
    }
    
    /**
     * Inner class to receive messages from server
     */
    private class RecibidorMensajes extends Thread {
        private GameClient cliente;
        
        public RecibidorMensajes(GameClient cliente) {
            this.cliente = cliente;
        }
        
        @Override
        public void run() {
            try {
                while (conectado) {
                    GameMessage mensaje = (GameMessage) input.readObject();
                    cliente.procesarMensaje(mensaje);
                }
            } catch (EOFException e) {
                System.out.println("Conexión cerrada por el servidor");
                conectado = false;
            } catch (IOException | ClassNotFoundException e) {
                if (conectado) {
                    System.err.println("Error receiving message: " + e.getMessage());
                }
                conectado = false;
            }
        }
    }
    
    /**
     * Main method to run the client
     */
    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 5000;
        String nombre = "Jugador";
        
        if (args.length > 0) {
            host = args[0];
        }
        if (args.length > 1) {
            try {
                puerto = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Using default port 5000");
            }
        }
        if (args.length > 2) {
            nombre = args[2];
        }
        
        GameClient cliente = new GameClient(host, puerto, nombre);
        
        if (cliente.conectar()) {
            cliente.mostrarMenu();
        } else {
            System.out.println("No se pudo conectar al servidor");
        }
    }
}
