package Models.Tablero;
import java.io.Serializable;

public class Casilla implements Serializable{
    
    // Enum para tipos de juegos
    public enum TipoJuego {
        /*Gato: El jugador que caiga en una casilla con el juego de 
Gato, debe jugar contra otro de los jugadores 
(aleatorio). 
• Si el jugador del turno gana podrá lanzar los dados 
en su siguiente turno, si no, vuelve a jugar. 
• El contrincante no tiene implicaciones si gana o 
pierde.
        Sopa de letras: Debe tener un archivo o lista con 100 distintas palabras de al menos 5 letras y de variedad de tamaños (tomadas de un diccionario, texto, etc.)
• Debe seleccionar aleatoriamente 4 de esas palabras y generar una sopa de letras, en un tamaño de 10 x 10, 15x 15, 20x20 casillas, igualmente aleatorio. Las palabras serán ubicadas una horizontal, una vertical, dos diagonales.
• El jugador tendrá 2 minutos para encontrar las 4 palabras y ganar.
• Si el jugador del turno gana podrá lanzar los dados en su siguiente turno, sino, vuelve a jugar la misma casilla

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
tiene opción, debe escoger cuál es el personaje 
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

        GATO("Gato", "Debe jugar contra otro de los jugadores (aleatorio). \r\n" + //
                        "• Si el jugador del turno gana podrá lanzar los dados en su siguiente turno, si no, vuelve a jugar"),
        SOPA_LETRAS("Sopa de letras", "El jugador tendrá 2 minutos para encontrar las 4 palabras y ganar.\r\n" + //
                        "• Si el jugador del turno gana podrá lanzar los dados en su siguiente turno, sino, vuelve a jugar la misma casilla"),
        MEMORY_PATH("Memory path", " el camino que debe completar el jugador en 3 intentos sin equivocarse en el \r\n" + //
                        "camino. Si logra llegar al final del camino, gana.\r\n" + //
                        "• Si en uno de los intentos no logra llegar a la meta, debe volver al inicio del juego."),
        SUPER_BROS_MEMORY("Super Bro's Memory", "donde se tendrán 9 pares de imágenes y deben descubrirse y hacerse pares. \\r\\nEste juego debe jugarse vs. otro jugador y ganará el jugador que más pares logra en la partida"),
        CATCH_THE_CAT("Catch the Cat", "El juego consiste en encerrar al gato, impidiendo que este pueda moverse. \r\n" + //
                        "• El gato inicia en medio de una matriz de 11x11 y se mueve 1 espacio a la vez, cada que el jugador \r\n" + //
                        "bloquea una casilla. El gato intenta moverse hacia fuera el panel, hacia los bordes, el más cercano no bloqueado. "),
        BOMBER_MARIO("Bomber Mario", "Se gana cuando se descubren con bombas, las 4 casillas donde se encuentra el tesoro"),
        GUESS_THE_WHO("Guess the Who", "El juego dará al jugador entre 4 y 8 celdas aleatorias para descubrir la imagen. \r\n" + //
                        "• Después de descubrir todas las celdas que tiene opción, debe escoger cuál es el personaje escondido. "),
        COLLECT_THE_COINS("Collect the Coins", "El jugador tendrá 30 segundos, 45 segundos o 60 segundos (aleatorio) \\r\\npara seleccionar cuantas casillas quiera y pueda. \\r\\nCada celda tendrá una moneda, al final se suman todas las monedas de la colecta y si el valor es positivo, gana el juego"),
        MARIO_CARDS("Mario cards", "El juego lo gana quien obtenga la carta mayor");
        
        private String nombre;
        private String descripcion;
        
        TipoJuego(String nombre, String descripcion) {
            this.nombre = nombre;
            this.descripcion = descripcion;
        }
        
        public String getNombre() {
            return nombre;
        }

        public String getDescripcion(){
            return descripcion;
        }
    }
    
    // Enum para comodines
    public enum TipoComodin {
        CARCEL("Carcel", "¡Has caído en la cárcel! Pierdes tu próximo turno."),
        TUBO_1("Tubo", "Existen 3 casillas con tubos. Si un jugador cae en una de ellas sucede lo siguiente:\r\n" + 
                        "• Si cae en el primer tubo, pasa al segundo tubo\r\n" +
                        "• Si cae en el segundo tubo, pasa al tercer tubo\r\n" +
                        "• Si cae en el tercer tubo, se devuelve al primer tubo."),
        TUBO_2("Tubo", "Existen 3 casillas con tubos. Si un jugador cae en una de ellas sucede lo siguiente:\r\n" + 
                        "• Si cae en el primer tubo, pasa al segundo tubo\r\n" +
                        "• Si cae en el segundo tubo, pasa al tercer tubo\r\n" +
                        "• Si cae en el tercer tubo, se devuelve al primer tubo."),
        TUBO_3("Tubo", "Existen 3 casillas con tubos. Si un jugador cae en una de ellas sucede lo siguiente:\r\n" + 
                        "• Si cae en el primer tubo, pasa al segundo tubo\r\n" +
                        "• Si cae en el segundo tubo, pasa al tercer tubo\r\n" +
                        "• Si cae en el tercer tubo, se devuelve al primer tubo."),
        ESTRELLA("Estrella", "Las estrellas permiten al jugador volver a lanzar los dados inmediatamente, para avanzar más rápido"),
        FLOR_FUEGO("Flor de fuego", "Si el jugador cae en esta casilla podrá hacer que uno de sus contrincantes, vuelva a comenzar el juego"),
        FLOR_HIELO("Flor de hielo", "Si el jugador cae en esta casilla podrá hacer que uno de sus contrincantes quede congelado, sin tirar los dados, por dos turnos"),
        COLA("Cola", "Permite al jugador que caiga en ella saltar a la casilla que que desee en un rango de +-3. Es decir, avanzar o retroceder en cualesquiera de las 3 casillas próximas o anteriores.");
        
        private String nombre;
        private String descripcion;
        
        TipoComodin(String nombre, String descripcion) {
            this.nombre = nombre;
            this.descripcion = descripcion;
        }
        
        public String getNombre() {
            return nombre;
        }

        public String getDescripcion(){
            return descripcion;
        }


    }
    
    //Atributos
    private int numero;
    private String tipo; // "juego", "comodin" o "normal"
    private TipoJuego tipoJuego;
    private TipoComodin tipoComodin;
    private int valor;

    // Constructor para casillas normales o juegos
    public Casilla(int numero, String tipo, int valor){
        this.numero = numero;
        this.tipo = tipo;
        this.valor = valor;
    }
    
    // Constructor para casillas de juego
    public Casilla(int numero, TipoJuego tipoJuego) {
        this.numero = numero;
        this.tipo = "juego";
        this.tipoJuego = tipoJuego;
        this.valor = 0;
    }
    
    // Constructor para casillas de comodin
    public Casilla(int numero, TipoComodin tipoComodin) {
        this.numero = numero;
        this.tipo = "comodin";
        this.tipoComodin = tipoComodin;
        this.valor = 0;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }
    
    public TipoJuego getTipoJuego() {
        return tipoJuego;
    }
    
    public void setTipoJuego(TipoJuego tipoJuego) {
        this.tipoJuego = tipoJuego;
    }
    
    public TipoComodin getTipoComodin() {
        return tipoComodin;
    }
    
    public void setTipoComodin(TipoComodin tipoComodin) {
        this.tipoComodin = tipoComodin;
    }
}
