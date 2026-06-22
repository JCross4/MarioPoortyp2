package GUI;

import java.awt.Color; 
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Models.Tablero.Board;
import Models.Tablero.Casilla;
import Models.Tablero.Pieza;

public class BoardDrawer {
    private JPanel panelBoard;
    private int anchoCasilla;
    private int altoCasilla;
    private int casillasDibujadas;
    private Board tablero;

    public BoardDrawer(JPanel panel, int anchoCasilla, int altoCasilla, int casillasDibujadas, Board tablero){
        panelBoard = panel;
        this.altoCasilla = altoCasilla;
        this.anchoCasilla = anchoCasilla;
        this.casillasDibujadas = casillasDibujadas;
        this.tablero = tablero;
    }


    //Dibujar tablero
    public void dibujarTablero(){
        Point posicionActual = new Point(0,0);
        for (Casilla casilla : tablero.getCasillas()){
            dibujarCasilla(casilla, posicionActual);
            posicionActual = obtenerSiguientePosicionCasilla(posicionActual);
        }
        panelBoard.repaint();
    }

    public void dibujarCasilla(Casilla casilla, Point posicion){
        //Todo tablero debe empezar en 0,0
        JLabel nuevaCasilla = crearLabel("", posicion, anchoCasilla, altoCasilla);
        String tipoCasilla = "";
        if (casilla.getTipo().equals("comodin")){
            tipoCasilla = casilla.getTipoComodin().getNombre();
        }
        else{
            tipoCasilla = casilla.getTipoJuego().getNombre();
        }
        nuevaCasilla.setText(casilla.getNumero() + 1 + " \n" + tipoCasilla);
        nuevaCasilla.setFont(new java.awt.Font("Arial", 1, 10));
        nuevaCasilla.setBackground(obtenerColor(casilla));
        nuevaCasilla.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        nuevaCasilla.setOpaque(true);
        panelBoard.add(nuevaCasilla);
        casillasDibujadas+=1;
    }

    

    public JLabel crearLabel(String texto, Point posicion, int width, int height){
        JLabel nuevoLabel = new JLabel(texto);
        nuevoLabel.setBounds(posicion.x, posicion.y, width, height);
        return nuevoLabel;
    }

    public Point obtenerSiguientePosicionCasilla(Point posicionActual){
        if (casillasDibujadas<tablero.getTamaño()/4){
            posicionActual.setLocation(posicionActual.getX() + anchoCasilla, posicionActual.getY());
        } else if (casillasDibujadas <= tablero.getTamaño() / 2) {
            posicionActual.setLocation(posicionActual.getX(), posicionActual.getY() + altoCasilla);
        } /*else if (casillasDibujadas == server.getTablero().getTamaño() / 2) {
            posicionActual.setLocation(posicionActual.getX() - 10, posicionActual.getY());
        }*/
        else if (casillasDibujadas < (tablero.getTamaño() - tablero.getTamaño() / 4) && posicionActual.x != 0) {
            posicionActual.setLocation(posicionActual.getX() - anchoCasilla, posicionActual.getY());
        } else {
            posicionActual.setLocation(posicionActual.getX(), posicionActual.getY() - altoCasilla);
        }
        //System.out.println("X= " + posicionActual.x + "Y= " + posicionActual.y);
        return posicionActual;
    }

    
    
    public Color obtenerColor(Casilla casilla){
        Color color;
        switch (casilla.getTipo()) {
            case "juego":
                color = Color.GREEN;
                switch (casilla.getTipoJuego().getNombre()) {
                    /*GATO("Gato"),
        SOPA_LETRAS("Sopa de letras"),
        MEMORY_PATH("Memory path"),
        SUPER_BROS_MEMORY("Super Bro's Memory"),
        CATCH_THE_CAT("Catch the Cat"),
        BOMBER_MARIO("Bomber Mario"),
        GUESS_THE_WHO("Guess the Who"),
        COLLECT_THE_COINS("Collect the Coins"),
        MARIO_CARDS("Mario cards"); */
                    case "Gato":
                        
                        break;
                    case "Sopa de letras":
                        break;
                    case "Memory path":
                        break;
                    case "Super Bro's Memory":
                        break;
                    case "Catch the Cat":
                        break;
                    case "Bomber Mario":
                        break;
                    case "Guess the Who":
                        break;
                    case "Collect the Coins":
                        break;
                    case "Mario cards":
                        break;
                    default:
                        break;
                }
                break;
            case "comodin":
                switch (casilla.getTipoComodin().getNombre()) {
                    /*CARCEL("Carcel"),
        TUBO_1("Tubo"),
        TUBO_2("Tubo"),
        TUBO_3("Tubo"),
        ESTRELLA("Estrella"),
        FLOR_FUEGO("Flor de fuego"),
        FLOR_HIELO("Flor de hielo"),
        COLA("Cola"); */
                    case "Carcel":
                        
                        break;
                    case "Tubo":
                        break;
                    case "Estrella":
                        break;
                    case "Flor de fuego":
                        break;
                    case "Flor de hielo":
                        break;
                    case "Cola":
                        break;
                    default:
                        break;
                }
                color = Color.BLUE;
                break;
            default:
                color = Color.RED;
                break;
        }
        return color;
    }

    //Dibujar piezas
    public Point obtenerPuntoNumeroCasilla(int numero){
        Point puntoResult = new Point(0, 0);
        numero-=1;
        if (numero<tablero.getTamaño()/4){
            puntoResult.setLocation(anchoCasilla*numero, 0);
        } else if (numero <= tablero.getTamaño() / 2) {
            puntoResult.setLocation(anchoCasilla*(tablero.getTamaño()/4-1), altoCasilla*((numero-tablero.getTamaño()/4)+1));
        }else if (numero < (tablero.getTamaño() - tablero.getTamaño()/4 - 1)) {
            puntoResult.setLocation((anchoCasilla*(tablero.getTamaño()/4-1)) - (anchoCasilla*(numero - tablero.getTamaño() / 2)), altoCasilla*((tablero.getTamaño()/4)+2));
        } else {
            puntoResult.setLocation(0, altoCasilla*(tablero.getTamaño()/4) - altoCasilla*(numero - (tablero.getTamaño() - tablero.getTamaño()/4)));
        }
        return puntoResult;
    }

    public JLabel dibujarPiezaEnClientePropio(Point posicion, Pieza pieza){
        JLabel nuevaPieza = crearLabel("", posicion, anchoCasilla-10, altoCasilla-10);
        nuevaPieza.setBackground(pieza.getColor());
        nuevaPieza.setOpaque(true);
        panelBoard.add(nuevaPieza);
        panelBoard.setComponentZOrder(nuevaPieza, 0);
        panelBoard.repaint();
        pieza.setLabel(nuevaPieza);
        return nuevaPieza;
    }

    public JLabel dibujarPiezaEnServer(Point posicion, Pieza pieza){
        JLabel nuevaPieza = crearLabel("", posicion, anchoCasilla-10, altoCasilla-10);
        nuevaPieza.setBackground(pieza.getColor());
        nuevaPieza.setOpaque(true);
        panelBoard.add(nuevaPieza);
        panelBoard.setComponentZOrder(nuevaPieza, 0);
        panelBoard.repaint();
        return nuevaPieza;
    }

    public void moverPiezaExistente(Point posicion, JLabel label){
        label.setBounds(posicion.x, posicion.y, anchoCasilla-10, altoCasilla-10);
        panelBoard.repaint();
    }
}
