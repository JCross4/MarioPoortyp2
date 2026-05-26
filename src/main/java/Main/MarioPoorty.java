package Main;

/**
 * MarioPoorty - Board Game with Socket Communication
 * A multiplayer board game where players connect through sockets
 */
public class MarioPoorty 
{
    public static void main( String[] args )
    {
        System.out.println("=== MARIO POORTY ===");
        System.out.println("Multiplayer Board Game with Socket Communication");
        System.out.println();
        
        if (args.length == 0) {
            printUsage();
            return;
        }
        
        String mode = args[0].toLowerCase();
        
        switch(mode) {
            case "server":
                runServer(args);
                break;
            case "client":
                runClient(args);
                break;
            case "demo":
                runDemo();
                break;
            default:
                System.out.println("Modo desconocido: " + mode);
                printUsage();
        }
    }
    
    /**
     * Run the game server
     */
    private static void runServer(String[] args) {
        int puerto = 5000;
        if (args.length > 1) {
            try {
                puerto = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Puerto inválido, usando 5000");
            }
        }
        
        System.out.println("Iniciando servidor en puerto " + puerto);
        GameServer servidor = new GameServer(puerto);
        servidor.iniciar();
    }
    
    /**
     * Run the game client
     */
    private static void runClient(String[] args) {
        String host = "localhost";
        int puerto = 5000;
        String nombre = "Jugador";
        
        if (args.length > 1) {
            host = args[1];
        }
        if (args.length > 2) {
            try {
                puerto = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.out.println("Puerto inválido, usando 5000");
            }
        }
        if (args.length > 3) {
            nombre = args[3];
        }
        
        System.out.println("Conectando como: " + nombre);
        System.out.println("Servidor: " + host + ":" + puerto);
        
        GameClient cliente = new GameClient(host, puerto, nombre);
        
        if (cliente.conectar()) {
            cliente.mostrarMenu();
        } else {
            System.out.println("No se pudo conectar al servidor");
        }
    }
    
    /**
     * Run a local demo without server/client
     */
    private static void runDemo() {
        System.out.println("=== DEMO LOCAL ===\n");
        
        // Create board
        Tablero tablero = new Tablero(40, "Demo Board");
        System.out.println("Tablero creado: " + tablero);
        System.out.println();
        
        // Add players
        Jugador j1 = new Jugador(1, "Mario");
        Jugador j2 = new Jugador(2, "Luigi");
        Jugador j3 = new Jugador(3, "Peach");
        
        tablero.agregarJugador(j1);
        tablero.agregarJugador(j2);
        tablero.agregarJugador(j3);
        
        System.out.println("Jugadores agregados:");
        for (Jugador j : tablero.getJugadores()) {
            System.out.println("  - " + j);
        }
        System.out.println();
        
        // Simulate game moves
        System.out.println("=== SIMULACIÓN DE MOVIMIENTOS ===\n");
        
        for (int turno = 0; turno < 3; turno++) {
            System.out.println("--- Turno " + (turno + 1) + " ---");
            for (Jugador j : tablero.getJugadores()) {
                if (j.isActivo()) {
                    int tirada = (int)(Math.random() * 6) + 1;
                    System.out.println(j.getNombre() + " tiró: " + tirada);
                    tablero.moverJugador(j, tirada);
                    System.out.println("  Posición: " + j.getPosicionTablero() + 
                                     " | Dinero: $" + j.getDinero() + 
                                     " | Casilla: " + j.getCasillaActual().getNombre());
                }
            }
            System.out.println();
        }
        
        // Final state
        System.out.println("=== ESTADO FINAL ===");
        for (Jugador j : tablero.getJugadores()) {
            System.out.println(j);
        }
    }
    
    /**
     * Print usage information
     */
    private static void printUsage() {
        System.out.println("Uso:");
        System.out.println("  java MarioPoorty server [puerto]");
        System.out.println("    - Inicia el servidor de juego");
        System.out.println("    - puerto: (opcional, default: 5000)");
        System.out.println();
        System.out.println("  java MarioPoorty client [host] [puerto] [nombre]");
        System.out.println("    - Conecta como cliente al servidor");
        System.out.println("    - host: (opcional, default: localhost)");
        System.out.println("    - puerto: (opcional, default: 5000)");
        System.out.println("    - nombre: nombre del jugador (opcional, default: Jugador)");
        System.out.println();
        System.out.println("  java MarioPoorty demo");
        System.out.println("    - Ejecuta una demostración local sin servidor");
        System.out.println();
        System.out.println("Ejemplos:");
        System.out.println("  java MarioPoorty server");
        System.out.println("  java MarioPoorty server 5001");
        System.out.println("  java MarioPoorty client localhost 5000 Mario");
        System.out.println("  java MarioPoorty demo");
    }
}

