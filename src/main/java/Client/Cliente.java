package Client;

import GUI.ClientFrame;
import GUI.GatoPantalla;
import GUI.SuperBrosMemoryPantalla;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JLabel;

import Models.Juegos.BomberMarioJuego;
import Models.Juegos.CatchTheCatJuego;
import Models.Juegos.CollectTheCoinsJuego;
import Models.Juegos.GatoJuego;
import Models.Juegos.GuessWhoJuego;
import Models.Juegos.Juego;
import Models.Juegos.MarioCardsJuego;
import Models.Juegos.MemoryPathJuego;
import Models.Juegos.SuperBrosMemoryJuego;
import Models.Juegos.SopaLetrasJuego;
import Models.Messages.Message;
import Models.Messages.MessageMemory;
import Models.Messages.MessageBoard;
import Models.Messages.MessageComodin;
import Models.Messages.MessageGato;
import Models.Messages.MessageMarioCards;
import Models.Messages.MessagePieza;
import Models.Messages.MessagePiezasDisponibles;
import Models.Messages.MessageRequest;
import Models.Tablero.Board;
import Models.Tablero.Casilla;
import Models.Tablero.Pieza;

public class Cliente {
    //Cliente/Player
    
    //TODO: Orden inicial, ganar exacto



    private final int PORT = 35501;
    private final String IP = "localhost";
    private String nombre;
    private Socket socket;
    private Player player;
    private Pieza[] piezasTotales = new Pieza[10];
    ClientFrame pantalla;
    ThreadCliente threadCliente;
    private ObjectOutputStream writerStream;
    private Board tablero;
    private GatoPantalla gatoPantalla;
    private SuperBrosMemoryPantalla memoryPantalla;
    private Juego juegoActual;
    private MarioCardsJuego marioCardsJuego;
    private ArrayList<JLabel> labelsOtrosPlayers = new ArrayList<>();
    private ArrayList<String> nombresOtrosPlayers = new ArrayList<>();
    private boolean turnoActual = false;
    private String currentPlayer = "";
    private boolean ultimoJuegoGanado = true;
    private String log = "";

    
    

    public Cliente(ClientFrame pantalla) {
        this.pantalla = pantalla;
        this.player = new Player();
        this.connect();
        solicitarPiezasDisponiblesServer();
    }
    
    private void connect(){
        try {
            this.socket = new Socket(IP, PORT);
            writerStream = new ObjectOutputStream(socket.getOutputStream());
            writerStream.flush();
            threadCliente = new ThreadCliente(this, socket);
            threadCliente.start();
            this.nombre = pantalla.obtenerNombre();
            escribirMensaje(new Message("Name", this.nombre, "", this.nombre));
            //solicitarPiezasDisponiblesServer();
        } catch (IOException ex) {
            
        }
    }

    public void enviarMensajeATodos(String texto){
        Message mensaje = new Message("broadcast", getNombre(), "todos", texto);
        escribirMensaje(mensaje);
    }

    public void escribirMensaje(Message msg){
        try {
            writerStream.writeObject(msg);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void crearTecladoRecibido(MessageBoard messageTeclado){
        setTablero(messageTeclado.getTablero());
        getPantalla().dibujarTablero(getTablero());
    }

    public void solicitarPiezasDisponiblesServer(){
        Message request = new MessageRequest("request", "", "", "", "piezasDisponibles");
        escribirMensaje(request);
        //System.out.println("Piezas solicitadas");
    }

    public void enviarPiezasDisponiblesActualizadas(){
        MessagePiezasDisponibles piezasActualizadas = new MessagePiezasDisponibles("piezas", this.nombre, "server", "piezas", piezasTotales);
        escribirMensaje(piezasActualizadas);
    }

    public void enviarMovimientoPieza(Player player){
        MessagePieza piezaMovimiento = new MessagePieza(this.getNombre(),"todos", player.getPosicion(), player.getPieza(), getNombre());
        escribirMensaje(piezaMovimiento);
    }

    public void habilitarPiezaSeleccionada(){
        for (int i = 0; i<piezasTotales.length; i++){
            if (piezasTotales[i].getNombre().equals(player.getPieza().getNombre())){
                setPiezasTotales(habilitarPieza(i));
                break;
            }
        }
    }

    public void deshabilitarPiezaSeleccionada(){
        for (int i = 0; i<piezasTotales.length; i++){
            if (piezasTotales[i].getNombre() == player.getPieza().getNombre()){
                setPiezasTotales(eliminarPieza(i));
                break;
            }
        }
    }

    public String[] obtenerStringPiezasDisponibles(){
        String[] stringPiezasDisponibles = new String[cantidadPiezasDisponibles()];
        Pieza[] piezasDisponibles = obtenerPiezasDisponibles();
        for (int i = 0; i<cantidadPiezasDisponibles(); i++){
            stringPiezasDisponibles[i] = piezasDisponibles[i].getNombre();
        }
        return stringPiezasDisponibles;
    }

    public int cantidadPiezasDisponibles(){
        int cantidad = 0;
        for (int i = 0; i<piezasTotales.length; i++){
            if (piezasTotales[i].isDisponible()){
                cantidad+=1;
            }
        }
        return cantidad;
    }

    public Pieza[] obtenerPiezasDisponibles(){
        ArrayList<Pieza> piezasDisponibles = new ArrayList<>();
        for (int i = 0; i<piezasTotales.length; i++){
            if (piezasTotales[i].isDisponible()){
                piezasDisponibles.add(piezasTotales[i]);
            }
        }
        return piezasDisponibles.toArray(new Pieza[0]);
    } 

    public Pieza[] habilitarPieza(int indice){
        Pieza[] piezasActualizado = piezasTotales;
        for (int i = 0; i<piezasTotales.length; i++){
            if (i == indice){
                piezasActualizado[i].setDisponible(true);
                break;
            }
        }
        return piezasActualizado;
    }

    public Pieza[] eliminarPieza(int indice){
        Pieza[] piezasActualizado = piezasTotales;
        for (int i = 0; i<piezasTotales.length; i++){
            if (i == indice){
                piezasActualizado[i].setDisponible(false);
            }
        }
        return piezasActualizado;
    }

    public Pieza obtenerPiezaDesdeString(String piezaString){
        Pieza piezaSeleccionada = null;
        for (int i = 0; i<piezasTotales.length; i++){
            if (piezasTotales[i].getNombre() == piezaString){
                piezaSeleccionada = piezasTotales[i];
            }
        }
        return piezaSeleccionada;
    }


    //Player
    public void realizarLanzamientoDeDados(){
        
            int[] resultTirada = getPlayer().realizarTirada();
            String tiradaString = getPlayer().obtenerStringTirada(resultTirada);
            int tiradaInt = getPlayer().obtenerMovimientosTirada(resultTirada);
            actualizarLabelsDados(resultTirada);
            actualizarPosicion(tiradaInt);
            Message m = new Message("Broadcast", getNombre(), "", "La tirada resulta en: " + tiradaString);
            escribirMensaje(m);
            moverPieza();
        
        
    }

    public void moverPieza(){
        //Enviar movimiento al server ?
        enviarMovimientoPieza(player);
        //MessagePieza movimiento = new MessagePieza(this.nombre, "todos", getPlayer().getPosicion(), getPlayer().getPieza(), getNombre());
        //escribirMensaje(movimiento);
        //System.out.println("El jugador " + this.nombre + " envia su lanzamiento con nombre " + getNombre());
        
    }

    public void actualizarPosicion(int movimiento){
        if (getPlayer().getPosicion()+movimiento>=getTablero().getTamaño()){
            getPlayer().setPosicion(getTablero().getTamaño());
        }
        else{
            getPlayer().setPosicion(getPlayer().getPosicion()+movimiento);
        }
    }


    public void dibujarPiezaRecibida(String nombre, Pieza pieza, int posicion){
        JLabel label = obtenerLabel(nombre);
        if (label == null){
            getPantalla().dibujarPiezaOtroCliente(posicion, pieza, nombre);
            //System.out.println("Primera vez, label no existente, crear y agregar al array");
        }
        else{
            getPantalla().moverPiezaExistenteOtroCliente(posicion, pieza, nombre);
            //System.out.println("Label sí existe, solo mover");
        }
    }

    public JLabel obtenerLabel(String nombreRecibido){
        JLabel label = null;
        for (String nombre : nombresOtrosPlayers){
            if (nombre.equals(nombreRecibido)){
                try {
                    int indice = nombresOtrosPlayers.indexOf(nombre);
                    label = labelsOtrosPlayers.get(indice);
                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println(e.getLocalizedMessage());
                }
            }
        }
        return label;
    }

    public void actualizarLabelsDados(int[] tiradas){
        int total = 0;
        for (int i = 0; i < tiradas.length; i++){
            if (tiradas[i] != 6){
                getPantalla().actualizarLabelDados(i, tiradas[i] + "");
                total+=tiradas[i];
            }
            else{
                getPantalla().actualizarLabelDados(i, "Castigo!");
            }
        }
        getPantalla().actualizarLabelDadosTotal(total);
    }

    public void checkCasillaActual(){
        Casilla casillaActual = getTablero().getCasillas().get(getPlayer().getPosicion()-1);
        //System.out.println("El tipo de la casilla actual es: " + casillaActual.getTipo());
        switch (casillaActual.getTipo().toLowerCase()) {
            case "juego":
                checkTipoJuegoCasillaActual(casillaActual);
                break;
            case "comodin":
                checkTipoComodinCasillaActual(casillaActual);
                break;
            default:
                //System.out.println("Ningun tipo");
                break;
        }
    }

    public void checkTipoComodinCasillaActual(Casilla casillaActual){
        getPantalla().mostrarMensaje("El comodín es de tipo: " + casillaActual.getTipoComodin().getNombre() + "\n" + casillaActual.getTipoComodin().getDescripcion());
        String jugadorObjetivo = "";
        log += "Cayó en comodín de tipo: " + casillaActual.getTipoComodin().getNombre() + "\n";
        switch (casillaActual.getTipoComodin().getNombre().toLowerCase()) {
            case "carcel":
                // Si el jugador cae en ella, perderá 2 turnos para lanzar el dado
                getPlayer().setTurnosPendientes(getPlayer().getTurnosPendientes() + 2);
                break;
            case "tubo":
                /*Existen 3 casillas con tubos. Si un 
                jugador cae en una de ellas sucede lo siguiente:
                • Si cae en el primer tubo, pasa al segundo tubo
                • Si cae en el segundo tubo, pasa al tercer tubo
                • Si cae en el tercer tubo, se devuelve al primer tubo.
                */ 
                //System.out.println("Obtener posición actual y comparar con las posiciones de los tubos para determinar a cual tubo ir y mover posicion");
                int posicionSiguienteTubo = getTablero().obtenerPosicionSiguienteTubo(casillaActual);
                //System.out.println("El siguiente tubo está en: " + posicionSiguienteTubo);
                actualizarPosicion(posicionSiguienteTubo-getPlayer().getPosicion());
                getPantalla().moverPiezaPropiaExistente(posicionSiguienteTubo, getPlayer().getPieza(), getNombre());
                getPantalla().mostrarMensaje("Ha sido transportado al siguiente tubo en la casilla " + posicionSiguienteTubo);
                enviarMovimientoPieza(player);
                break;
            case "estrella":
                /*Las estrellas 
                permiten al jugador volver a lanzar los dados 
                inmediantamente, para avanzar más rápido */
                getPantalla().mostrarMensaje("Puede volver a tirar los dados");
                getPantalla().actualizarEstadoBotonTirada(true);
                break;
            case "flor de fuego":
                /* Si el jugador 
                cae en esta casilla podrá hacer que uno de sus 
                contrincantes, vuelva a comenzar el juego */
                //Listar nombres de los otros jugadores (mediante el arreglo?) y al seleccionarlo enviar mensaje
                jugadorObjetivo = getPantalla().obtenerOtroJugador();
                //Enviar mensaje enviandolo a la posicion 1;
                MessageComodin florFuego = new MessageComodin("comodin", this.nombre, jugadorObjetivo, "flor de fuego", "Flor de fuego", jugadorObjetivo);
                escribirMensaje(florFuego);
                break;
            case "flor de hielo":
                /*Si el jugador 
                cae en esta casilla podrá hacer que uno de sus 
                contrincantes quede congelado, sin tirar los dados, 
                por dos turnos*/
                //Listar nombres de los otros jugadores (mediante el arreglo?) y al seleccionarlo enviar mensaje
                jugadorObjetivo = getPantalla().obtenerOtroJugador();
                MessageComodin florHielo = new MessageComodin("comodin", this.nombre, jugadorObjetivo, "flor de hielo", "Flor de hielo", jugadorObjetivo);
                escribirMensaje(florHielo);
                break;
            case "cola":
                /* lo que permite al 
                jugador que caiga en ella es saltar  a la casilla que 
                que desee en un rango de +-3. Es decir, avanzar o 
                retroceder en cualesquiera de las 3 casillas 
                próximas o anteriores.
                */
                //Listar las opciones y actualizar la posicion actual (?)
                int intCantCasillas = getPantalla().obtenerCasillasCola();
                actualizarPosicion(intCantCasillas);
                getPantalla().moverPiezaPropiaExistente(getPlayer().getPosicion(), getPlayer().getPieza(), getNombre());
                getPantalla().mostrarMensaje("Se ha movido " + intCantCasillas + " casillas");
                enviarMovimientoPieza(player);
                break;
            default:
                getPantalla().mostrarMensaje("Ningun comodin");
                break;
        }
    }

    public void checkTipoJuegoCasillaActual(Casilla casillaActual){
        log += "Cayó en juego de tipo: " + casillaActual.getTipoJuego().getNombre() + "\n";
        getPantalla().mostrarMensaje("El juego es de tipo: " + casillaActual.getTipoJuego().getNombre() + "\n" + casillaActual.getTipoJuego().getDescripcion());
        switch (casillaActual.getTipoJuego()) {
            /*GATO("Gato", "Debe jugar contra otro de los jugadores (aleatorio). \r\n" + //
                        "• Si el jugador del turno gana podrá lanzar los dados en su siguiente turno, si no, vuelve a jugar"),
        SOPA_LETRAS("Sopa de letras", "El jugador tendrá 2 minutos para encontrar las 4 palabras y ganar.\r\n" + //
                        "• Si el jugador del turno gana podrá lanzar los dados en su siguiente turno, sino, vuelve a jugar la misma casilla"),
          Memory path: Debe generar una matriz de 6x3 y aleatoriamente seleccionar 
            una casilla en cada una de las 6 filas, la cual será el camino que 
            debe completar el jugador en 3 intentos sin equivocarse en el 
            camino. Si logra llegar al final del camino, gana.
            • Si en uno de los intentos no logra llegar a la meta, debe volver al 
            inicio del juego.
            • Si el jugador del turno gana podrá lanzar los dados en su 
            siguiente turno, sino, vuelve a jugar la misma casilla.
            • RECOMENDACIÓN: mostrar la solución en consola o 
            aumentar la cantidad de intentos, es un juego difícil de 
            solucionar.

                    Super bros memory: Genere un juego de memoria de 3x6, donde se tendrán 9 pares de imágenes y deben descubrirse y hacerse pares. Este juego debe jugarse vs. otro jugador y ganará el jugador que más pares logra en la partida.
            • Si el jugador del turno gana podrá lanzar los dados en su siguiente turno, sino, vuelve a jugar el mismo juego, misma casilla. El contrincante no tiene implicaciones.

                    Catch the cat: El juego consiste en encerrar al gato, impidiendo 
            que este pueda moverse. 
            • El gato inicia en medio de una matriz de 11x11 y se 
            mueve 1 espacio a la vez, cada que el jugador 
            bloquea una casilla. El gato intenta moverse hacia 
            fuera el panel, hacia los bordes, el más cercano no 
            bloqueado. 
            • Se gana si se encierra al gato, se pierde si el gato 
            escapa por los bordes

                    Bomber mario: En una matriz de 10x10, 15x15, 20x20 (aleatorio) se esconderá un tesoro que ocupará 4 casillas. El jugador tiene 7 bombas para encontrar el tesoro. 
            • Hay varios tipos de bombas:
            • Simples: explotan solo una casilla
            • Dobles: explotan 2x2
            • Cruz: explotan 3 y 3 formando una cruz, es decir, 5 casilllas explotan.
            • Linea: explota una línea horizontal o vertical de 4 Casillas.
            • Las bombas al explotar descubren el contenido de las casillas. 
            • Las bombas explotan las Casillas más contiguas a la casilla donde se colocaron. Aleatoriamente la dirección.
            • Se gana cuando se descubren con bombas, las 4 casillas donde se encuentra el tesoro. Se pierde si no se logran descubrir las 4 casillas del tesoro

                    Guess who: En una matriz de 10x10 va a esconder una 
            imagen de 1 de 15 personajes. 
            • El juego dará al jugador entre 4 y 8 celdas 
            aleatorias para descubrir la imagen. 
            • Después de descubrir todas las celdas que 
            tiene opción, debe escoger cuál contiene el personaje 
            escondido. 
            • Si acierta gana

                    Collect the coins: En una matriz 25 x 25: llénela de igual cantidad de monedas buenas que de monedas malas, colóquelas aleatoriamente en la matriz. 
            • Las monedas, tanto buenas como malas, cada una tendrá un valor entre +-1 y +-10 (aleatorio). Las monedas buenas el valor positivo, las monedas malas negativo.
            • El jugador tendrá 30 segundos, 45 segundos o 60 segundos (aleatorio) para seleccionar cuantas casillas quiera y pueda. Cada celda tendrá una moneda, al final se suman todas las monedas de la colecta y si el valor es positivo, gana el juego.

                    Mario cards: Este juego consiste en que todos los jugadores seleccionarán una carta 
            distinta entre 52 (baraja inglesa), donde el 2 será el menor valor y el As, el 
            mayor. Los mazos tendrán también orden, en caso de que dos o más 
            jugadores empaten en el valor obtenido, de mayor a menor, los mazos 
            son: corazones, trébol, picas, rombo.
            • El juego lo gana quien obtenga la carta mayor. Si el que gana es el jugador 
            que cayó en la casilla, podrá lanzar dados el siguiente turno que le toque, 
            sino, repite el juego.
         */
            case GATO:
                String nombreContrincante = obtenerOtroJugadorAleatorio();
                //Iniciar gato mediante interface
                if (nombreContrincante == null || nombreContrincante.isEmpty()) {
                    getPantalla().mostrarMensaje("No hay otros jugadores disponibles para jugar Gato.");
                } else {
                    getPantalla().mostrarMensaje("Iniciando Tic Tac Toe contra " + nombreContrincante + "...");
                    //GatoJuego gato = new GatoJuego(this.nombre, nombreContrincante, "X");
                    //gato.iniciar();
                    iniciarGato(nombreContrincante);
                }
                break;

            case SOPA_LETRAS:
                //Arreglar pantalla sopa letras, mostrar sopa de letras y cuadro input juntos, no mostrar solucion
                SopaLetrasJuego sopaLetras = new SopaLetrasJuego(this);
                juegoActual = sopaLetras;
                sopaLetras.iniciar();
                break;

            case MEMORY_PATH:
                //Mostrar camino preliminar, mostrar casillas recorridas en el intento
                MemoryPathJuego memoryPath = new MemoryPathJuego(this);
                juegoActual = memoryPath;
                memoryPath.iniciar();
                break;

            case SUPER_BROS_MEMORY:
                //Dos players
                String nombreContrincanteMemory = getPantalla().obtenerOtroJugador();
                if (nombreContrincanteMemory == null || nombreContrincanteMemory.isEmpty()) {
                    getPantalla().mostrarMensaje("No hay otros jugadores disponibles para Super Bros Memory.");
                } else {
                    getPantalla().mostrarMensaje("Iniciando Super Bros Memory contra " + nombreContrincanteMemory + "...");
                    iniciarSuperBrosMemory(nombreContrincanteMemory);
                }
                break;

            case CATCH_THE_CAT:
                // Iniciar Catch the Cat (single player)
                //Mostrar tablero siempre
                CatchTheCatJuego catchGame = new CatchTheCatJuego(this);
                juegoActual = catchGame;
                catchGame.iniciar();
                break;

            case BOMBER_MARIO:
                // Iniciar Bomber Mario (single player)
                BomberMarioJuego bomberGame = new BomberMarioJuego(this);
                juegoActual = bomberGame;
                bomberGame.iniciar();
                break;

            case GUESS_THE_WHO:
                //Single player
                GuessWhoJuego guessWhoGame = new GuessWhoJuego(this);
                juegoActual = guessWhoGame;
                guessWhoGame.iniciar();
                break;

            case COLLECT_THE_COINS:
                // Iniciar Collect the Coins (single player)
                // Mostrar tablero, disponibles y no disponibles
                CollectTheCoinsJuego collectGame = new CollectTheCoinsJuego(this);
                juegoActual = collectGame;
                collectGame.iniciar();
                break;

            case MARIO_CARDS:
                //Multiplayer all players
                iniciarMarioCards();
                break;
            default:
                
                break;
        }
    }

    public void iniciarGato(String jugadorContrincante){
        if (jugadorContrincante == null || jugadorContrincante.isEmpty()) {
            return;
        }
        this.juegoActual = new GatoJuego(this.nombre, jugadorContrincante, "X");
        this.gatoPantalla = new GatoPantalla(getPantalla(), this, true, "X", (GatoJuego) juegoActual);
        this.gatoPantalla.setVisible(true);

        MessageGato inicioGato = new MessageGato(
                "Gato",
                getNombre(),
                jugadorContrincante,
                "Inicio del juego Gato",
                (GatoJuego) juegoActual,
                "start",
                -1,
                "O",
                "         ",
                "started"
        );
        escribirMensaje(inicioGato);
        //.out.println("Gato enviado");
    }

    public void iniciarSuperBrosMemory(String jugadorContrincante){
        if (jugadorContrincante == null || jugadorContrincante.isEmpty()) {
            return;
        }
        this.juegoActual = new SuperBrosMemoryJuego(this.getNombre(), jugadorContrincante);
        SuperBrosMemoryJuego memoryGame = (SuperBrosMemoryJuego) this.juegoActual;
        this.memoryPantalla = new SuperBrosMemoryPantalla(getPantalla(), this, memoryGame);

        MessageMemory inicioMemory = new MessageMemory(
                "Memory",
                getNombre(),
                jugadorContrincante,
                "Inicio del juego Super Bros Memory",
                memoryGame,
                "start",
                -1,
                "Empieza el juego",
                false
        );
        escribirMensaje(inicioMemory);
        this.memoryPantalla.setVisible(true);
    }

    public void recibirMensajeMemory(MessageMemory mensajeMemory) {
        if (mensajeMemory == null) {
            return;
        }
        if ("start".equals(mensajeMemory.getAction())) {
            getPantalla().mostrarMensaje("Has sido retado a Super Bros Memory por " + mensajeMemory.emisor + ".");
            this.juegoActual = mensajeMemory.getJuego();
            this.memoryPantalla = new SuperBrosMemoryPantalla(getPantalla(), this, mensajeMemory.getJuego());
            this.memoryPantalla.setVisible(true);
            return;
        }
        if (memoryPantalla != null) {
            memoryPantalla.receiveOpponentMessage(mensajeMemory);
        }
    }

    public void recibirMensajeGato(MessageGato mensajeGato){
        //System.out.println("Mensaje gato recibido");
        if (mensajeGato == null) {
            return;
        }
        if ("start".equals(mensajeGato.getAction())) {
            this.gatoPantalla = new GatoPantalla(getPantalla(), this, false, mensajeGato.getSymbol(), mensajeGato.getJuego());
            this.gatoPantalla.setVisible(true);
            getPantalla().mostrarMensaje("Has sido retado a Gato por " + mensajeGato.emisor + ".");
            return;
        }
        if (gatoPantalla != null) {
            gatoPantalla.receiveOpponentMessage(mensajeGato);
        }
    }

    public void iniciarMarioCards() {
        if (getNombresOtrosPlayers().size() == 0) {
            getPantalla().mostrarMensaje("No hay otros jugadores disponibles para Mario Cards.");
            return;
        }

        this.marioCardsJuego = new MarioCardsJuego();
        marioCardsJuego.agregarJugador(getNombre());
        
        // Repartir carta al jugador actual
        marioCardsJuego.repartirCarta(getNombre());
        MarioCardsJuego.Carta cartaJugador = marioCardsJuego.obtenerCartaJugador(getNombre());
        
        String cartaInfo = cartaJugador.toString();
        getPantalla().mostrarMensaje("Tu carta es: " + cartaInfo);

        // Enviar invitación a todos los jugadores
        MessageMarioCards inicioMarioCards = new MessageMarioCards(
                "MarioCards",
                getNombre(),
                "todos",
                "Inicio del juego Mario Cards",
                marioCardsJuego,
                "start",
                -1,
                cartaInfo,
                "pending"
        );
        escribirMensaje(inicioMarioCards);
        
        getPantalla().mostrarMensaje("Invitación a Mario Cards enviada a todos los jugadores...");
    }

    public void recibirMensajeMarioCards(MessageMarioCards mensajeMarioCards) {
        if (mensajeMarioCards == null) {
            return;
        }

        if ("start".equals(mensajeMarioCards.getAction())) {
            // Recibir invitación para jugar
            this.marioCardsJuego = mensajeMarioCards.getJuego();
            marioCardsJuego.agregarJugador(getNombre());
            
            // Repartir carta al jugador
            marioCardsJuego.repartirCarta(getNombre());
            MarioCardsJuego.Carta cartaJugador = marioCardsJuego.obtenerCartaJugador(getNombre());
            
            String cartaInfo = cartaJugador.toString();
            getPantalla().mostrarMensaje("Has sido invitado a Mario Cards por " + mensajeMarioCards.emisor);
            getPantalla().mostrarMensaje("Tu carta es: " + cartaInfo);

            // Enviar la carta de vuelta
            MessageMarioCards respuestaMarioCards = new MessageMarioCards(
                    "MarioCards",
                    getNombre(),
                    mensajeMarioCards.emisor,
                    "Jugador agregado",
                    marioCardsJuego,
                    "carta",
                    -1,
                    cartaInfo,
                    "pending"
            );
            escribirMensaje(respuestaMarioCards);
        } else if ("resultado".equals(mensajeMarioCards.getAction())) {
            // Recibir resultado final
            String resultado = mensajeMarioCards.getResult();
            getPantalla().mostrarMensaje(resultado);
        }
    }

    public boolean checkCasillaFinal(){
        return getPlayer().getPosicion() == getTablero().getTamaño();
    }

    public String obtenerOtroJugadorAleatorio(){
        String nombreJugador = "";
        if (getNombresOtrosPlayers().size()>0){
            //Seleccionar un nombre aleatorio del arreglo de nombresOtrosPlayers
            Random rand = new Random();
            int indiceAleatorio = rand.nextInt(getNombresOtrosPlayers().size());
            nombreJugador = getNombresOtrosPlayers().get(indiceAleatorio);
            getPantalla().agregarMensaje("El jugador objetivo seleccionado aleatoriamente es: " + nombreJugador);
        }
        else{
            getPantalla().agregarMensaje("No hay otros jugadores");
        }
        return nombreJugador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ClientFrame getPantalla() {
        return pantalla;
    }

    public void setPantalla(ClientFrame pantalla) {
        this.pantalla = pantalla;
    }

    public Board getTablero() {
        return tablero;
    }

    public void setTablero(Board tablero) {
        this.tablero = tablero;
    }

    public Pieza[] getPiezasTotales() {
        return piezasTotales;
    }

    public void setPiezasTotales(Pieza[] piezasTotales) {
        this.piezasTotales = piezasTotales;
    }

    public ArrayList<JLabel> getLabelsOtrosPlayers() {
        return labelsOtrosPlayers;
    }

    public void setLabelsOtrosPlayers(ArrayList<JLabel> labelsOtrosPlayers) {
        this.labelsOtrosPlayers = labelsOtrosPlayers;
    }

    public ArrayList<String> getNombresOtrosPlayers() {
        return nombresOtrosPlayers;
    }

    public void setNombresOtrosPlayers(ArrayList<String> nombresOtrosPlayers) {
        this.nombresOtrosPlayers = nombresOtrosPlayers;
    }
    
    public boolean isTurnoActual() {
        return turnoActual;
    }

    public void setTurnoActual(boolean turnoActual) {
        this.turnoActual = turnoActual;
        pantalla.actualizarEstadoBotonTirada(turnoActual);
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
        if (currentPlayer.equals(getNombre())) {
            setTurnoActual(true);
        } else {
            setTurnoActual(false);
        }
    }

    public boolean isUltimoJuegoGanado() {
        return ultimoJuegoGanado;
    }

    public void setUltimoJuegoGanado(boolean ultimoJuegoGanado) {
        this.ultimoJuegoGanado = ultimoJuegoGanado;
    }

    public String getLog() {
        return log;
    }
    
    
}
