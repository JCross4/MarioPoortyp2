package Client;

import GUI.ClientFrame;
import GUI.GatoPantalla;
import GUI.GatoPantalla;
import Models.MessageGato;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Models.Board;
import Models.Casilla;
import Models.GatoJuego;
import Models.Juego;
import Models.Message;
import Models.MessageBoard;
import Models.MessageComodin;
import Models.MessagePieza;
import Models.MessagePiezasDisponibles;
import Models.MessageRequest;
import Models.Pieza;
import Models.SopaLetrasJuego;

public class Cliente {
    //Cliente/Player
    //TODO: piezas, dibujar piezas,-- mover piezas, casillas especiales
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
    private Juego juegoActual;
    private JLabel labelPiezaPlayer;
    private ArrayList<JLabel> labelsOtrosPlayers = new ArrayList<>();
    private ArrayList<String> nombresOtrosPlayers = new ArrayList<>();
    private boolean turnoActual = false;
    private String currentPlayer = "";
    private boolean ultimoJuegoGanado = true;

    
    

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
        System.out.println("Piezas solicitadas");
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
        System.out.println("El jugador " + this.nombre + " envia su lanzamiento con nombre " + getNombre());
        
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
            System.out.println("Primera vez, label no existente, crear y agregar al array");
        }
        else{
            getPantalla().moverPiezaExistenteOtroCliente(posicion, pieza, nombre);
            System.out.println("Label sí existe, solo mover");
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
        System.out.println("El tipo de la casilla actual es: " + casillaActual.getTipo());
        switch (casillaActual.getTipo().toLowerCase()) {
            case "juego":
                checkTipoJuegoCasillaActual(casillaActual);
                break;
            case "comodin":
                checkTipoComodinCasillaActual(casillaActual);
                break;
            default:
                System.out.println("Ningun tipo");
                break;
        }
    }

    public void checkTipoComodinCasillaActual(Casilla casillaActual){
        getPantalla().mostrarMensaje("El comodín es de tipo: " + casillaActual.getTipoComodin().getNombre() + "\n" + casillaActual.getTipoComodin().getDescripcion());
        String jugadorObjetivo = "";
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
                System.out.println("El siguiente tubo está en: " + posicionSiguienteTubo);
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
                MessageComodin florHielo = new MessageComodin("comodin", this.nombre, jugadorObjetivo, "flor de fuego", "Flor de fuego", jugadorObjetivo);
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
        getPantalla().mostrarMensaje("El juego es de tipo: " + casillaActual.getTipoJuego().getNombre() + "\n" + casillaActual.getTipoJuego().getDescripcion());
        switch (casillaActual.getTipoJuego()) {
            /*GATO("Gato", "Debe jugar contra otro de los jugadores (aleatorio). \r\n" + //
                        "• Si el jugador del turno gana podrá lanzar los dados en su siguiente turno, si no, vuelve a jugar"),
        SOPA_LETRAS("Sopa de letras", "El jugador tendrá 2 minutos para encontrar las 4 palabras y ganar.\r\n" + //
                        "• Si el jugador del turno gana podrá lanzar los dados en su siguiente turno, sino, vuelve a jugar la misma casilla"),
        MEMORY_PATH("Memory path", " el camino que debe completar el jugador en 3 intentos sin equivocarse en el \r\n" + //
                        "camino. Si logra llegar al final del camino, gana.\r\n" + //
                        "• Si en uno de los intentos no logra llegar a la meta, debe volver al inicio del juego."),
        SUPER_BROS_MEMORY("Super Bro's Memory", "donde se tendrán 9 pares de imágenes y deben descubrirse y hacerse pares. Este juego debe jugarse vs. otro jugador y ganará el jugador que más pares logra en la partida"),
        CATCH_THE_CAT("Catch the Cat", "El juego consiste en encerrar al gato, impidiendo que este pueda moverse. \r\n" + //
                        "• El gato inicia en medio de una matriz de 11x11 y se mueve 1 espacio a la vez, cada que el jugador \r\n" + //
                        "bloquea una casilla. El gato intenta moverse hacia fuera el panel, hacia los bordes, el más cercano no bloqueado. "),
        BOMBER_MARIO("Bomber Mario", "Se gana cuando se descubren con bombas, las 4 casillas donde se encuentra el tesoro"),
        GUESS_THE_WHO("Guess the Who", "El juego dará al jugador entre 4 y 8 celdas aleatorias para descubrir la imagen. \r\n" + //
                        "• Después de descubrir todas las celdas que tiene opción, debe escoger cuál es el personaje escondido. "),
        COLLECT_THE_COINS("Collect the Coins", "El jugador tendrá 30 segundos, 45 segundos o 60 segundos (aleatorio) 
                            para seleccionar cuantas casillas quiera y pueda. Cada celda tendrá una moneda, 
                            al final se suman todas las monedas de la colecta y si el valor es positivo, gana el juego"),
        MARIO_CARDS("Mario cards", "El juego lo gana quien obtenga la carta mayor");
         */
            case GATO:
                String nombreContrincante = obtenerOtroJugadorAleatorio();
                if (nombreContrincante == null || nombreContrincante.isEmpty()) {
                    getPantalla().mostrarMensaje("No hay otros jugadores disponibles para jugar Gato.");
                } else {
                    getPantalla().mostrarMensaje("Iniciando Tic Tac Toe contra " + nombreContrincante + "...");
                    iniciarGato(nombreContrincante);
                }
                break;
            case SOPA_LETRAS:
                SopaLetrasJuego sopaLetras = new SopaLetrasJuego(this);
                sopaLetras.iniciar();
                break;
            case MEMORY_PATH:
                
                break;
            case SUPER_BROS_MEMORY:
                
                break;
            case CATCH_THE_CAT:
                
                break;
            case BOMBER_MARIO:
                
                break;
            case GUESS_THE_WHO:

                break;
            case COLLECT_THE_COINS:

                break;
            case MARIO_CARDS:

                break;
            default:
                
                break;
        }
    }

    public void iniciarGato(String jugadorContrincante){
        if (jugadorContrincante == null || jugadorContrincante.isEmpty()) {
            return;
        }
        this.juegoActual = new GatoJuego(this.nombre, jugadorContrincante);
        this.gatoPantalla = new GatoPantalla(getPantalla(), this, jugadorContrincante, true, "X", (GatoJuego) juegoActual);
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
    }

    public void recibirMensajeGato(MessageGato mensajeGato){
        if (mensajeGato == null) {
            return;
        }
        if ("start".equals(mensajeGato.getAction())) {
            this.gatoPantalla = new GatoPantalla(getPantalla(), this, mensajeGato.emisor, false, mensajeGato.getSymbol(), mensajeGato.getJuego());
            this.gatoPantalla.setVisible(true);
            getPantalla().mostrarMensaje("Has sido retado a Gato por " + mensajeGato.emisor + ".");
            return;
        }
        if (gatoPantalla != null) {
            gatoPantalla.receiveOpponentMessage(mensajeGato);
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
    
    
}
