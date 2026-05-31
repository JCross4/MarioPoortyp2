package GUI;

import java.awt.Color;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Models.Board;
import Models.Casilla;

public class BoardDrawer {
    private JPanel panelBoard;
    private int sizeCasilla;
    private int casillasDibujadas;
    private Board tablero;

    public BoardDrawer(JPanel panel, int sizeCasilla, int casillasDibujadas, Board tablero){
        panelBoard = panel;
        this.sizeCasilla = sizeCasilla;
        this.casillasDibujadas = casillasDibujadas;
        this.tablero = tablero;
    }

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
        JLabel nuevaCasilla = crearLabel("", posicion, sizeCasilla, sizeCasilla);
        nuevaCasilla.setBackground(obtenerColor(casilla.getTipo()));
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
            posicionActual.setLocation(posicionActual.getX() + sizeCasilla, posicionActual.getY());
        } else if (casillasDibujadas <= tablero.getTamaño() / 2) {
            posicionActual.setLocation(posicionActual.getX(), posicionActual.getY() + sizeCasilla);
        } /*else if (casillasDibujadas == server.getTablero().getTamaño() / 2) {
            posicionActual.setLocation(posicionActual.getX() - 10, posicionActual.getY());
        }*/
        else if (casillasDibujadas < (tablero.getTamaño() - tablero.getTamaño() / 4) && posicionActual.x != 0) {
            posicionActual.setLocation(posicionActual.getX() - sizeCasilla, posicionActual.getY());
        } else {
            posicionActual.setLocation(posicionActual.getX(), posicionActual.getY() - sizeCasilla);
        }
        System.out.println("X= " + posicionActual.x + "Y= " + posicionActual.y);
        return posicionActual;
    }
    
    public Color obtenerColor(String tipo){
        Color color;
        switch (tipo) {
            case "normal":
                color = Color.GREEN;
                break;
            case "especial":
                color = Color.BLUE;
                break;
            case "trampa":
                color = Color.RED;
                break;
            case "premio":
                color = Color.PINK;
                break;
            default:
                color = Color.GREEN;
                break;
        }
        return color;
    }
}
